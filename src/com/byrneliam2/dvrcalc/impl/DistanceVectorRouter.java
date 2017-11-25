package com.byrneliam2.dvrcalc.impl;

import com.byrneliam2.dvrcalc.ui.DvrUIListener;
import com.byrneliam2.dvrcalc.ui.DvrUINotifier;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.util.*;
import java.io.*;

/*
 * NOTES:
 * File consists of a node, x and y position, number of neighbours and the neighbours/distance
 * in alphabetical order.
 */

/*
 * Liam Byrne (byrneliam2)
 * DVRCalculator
 */
public class DistanceVectorRouter extends DvrUINotifier {

    private List<Node> nodes = new ArrayList<>();
    private List<Node> currentPath = new ArrayList<>();

    // status checkers
    private boolean hasBeenRun = false;
    private boolean hasBeenLoaded = false;

    public DistanceVectorRouter(DvrUIListener... listeners) {
        super();
        for (DvrUIListener l : listeners) addListener(l);
    }

    /**
     * Load the nodes from the topology file and setup the routing table for each node.
     * @return outcome of loading file
     * TODO replace with StAX parsing
     */
    public boolean onLoadX(File file) {
        // clean up on every load
        nodes.clear();
        hasBeenRun = false;

        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNext()) {
                String n = scan.next();
                int x = scan.nextInt();
                int y = scan.nextInt();
                Node node = new Node(n.charAt(0), x, y);
                int count = scan.nextInt(); // the number of neighbouring nodes
                for (int i = 0; i < count; i++)
                    node.addNeighbour(scan.next().charAt(0), scan.nextInt());

                this.nodes.add(node);
            }
            for (Node n : nodes) n.setupRoutingTable(nodes);
            scan.close();

            hasBeenLoaded = true;
        } catch (IOException | InputMismatchException e) {
            sendToListeners("File failure: " + e.getClass().getSimpleName());
            return false;
        }
        return true;
    }

    /**
     * Load the nodes from the topology file and setup the routing table for each node.
     * @return outcome of loading file
     */
    public boolean onLoad(File file) {
        nodes.clear();
        hasBeenRun = false;
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader read = factory.createXMLEventReader(new FileReader(file));
            Node node = new Node();
            while (read.hasNext()) {
                XMLEvent event = read.nextEvent();
                // Document should be made up of elements where only the start element and its
                // attributes are relevant. Hence, this loading system assumes this.
                if (event.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    StartElement start = event.asStartElement();
                    String qname = start.getName().getLocalPart();
                    if (qname.equals("node")) {
                        node = new Node();
                        node.setKey(start.getAttributeByName(new QName("key")).getValue().charAt(0));
                        node.setX(Integer.parseInt(start.getAttributeByName(new QName("x")).getValue()));
                        node.setY(Integer.parseInt(start.getAttributeByName(new QName("y")).getValue()));
                    } else if (qname.equals("neighbour")) {
                        node.addNeighbour(start.getAttributeByName(new QName("key")).getValue().charAt(0),
                                Integer.parseInt(start.getAttributeByName(new QName("cost")).getValue()));
                    }
                } else if (event.getEventType() == XMLStreamConstants.END_ELEMENT) {
                    EndElement end = event.asEndElement();
                    String qname = end.getName().getLocalPart();
                    if (qname.equals("node")) nodes.add(node);
                }
            }
            for (Node n : nodes) n.setupRoutingTable(nodes);
            read.close();
            hasBeenLoaded = true;
        } catch (FileNotFoundException | XMLStreamException e) {
            sendToListeners("File failure: " + e.getClass().getSimpleName());
            return false;
        }
        return true;
    }

    public boolean onSave() {
        return false;
    }

    /**
     * Run the distance vector routing algorithm.
     */
    public void onRun() {
        if (isError(!hasBeenLoaded, "Please load a topology first.")) return;

        // for every node (as in Bellman-Ford)
        for (int i = 0; i < nodes.size(); i++) {
            // for every node (literal)
            for (Node node : this.nodes) {
                // for all of its neighbours
                for (Map.Entry m : node.getNeighbours().entrySet()) {
                    char key = (char) m.getKey();
                    int dist = (int) m.getValue();
                    Node neighbour = new DVUtils(nodes).find(key);
                    // send the table of this node to the neighbour
                    neighbour.updateRoutingTable(node, dist, node.getTable());
                }
            }
        }

        /*printAll();*/

        hasBeenRun = true;
    }

    /**
     * Generate a route between two random nodes using the complete routing table.
     */
    public void onRoute() {
        if (isError(!hasBeenRun,
                "DVR algorithm must be executed before routing can be performed."))
            return;

        // get two random nodes
        Random rand = new Random();
        int idx1 = rand.nextInt(nodes.size()), idx2;
        while ((idx2 = rand.nextInt(nodes.size())) == idx1);
        Node n1 = nodes.get(idx1), n2 = nodes.get(idx2);
        // set up routing
        // in n1's table, get the minimum cost route to n2
        RoutingTable n1table = n1.getTable();
        DVUtils util = new DVUtils(nodes);
        int[] row = n1table.getRow(n1table.getDestinationIndex(n2.getKey()));
        int[] route = util.getNextRoute(row);
        // start the route finding
        // three cases:
        // - we need to start by going via the neighbour with the best route
        // - from there, if any of its direct neighbours is n2 and the total cost
        //   is equal to the given cost from the table, go there
        // - otherwise, pick the best route from there and start again from step 2
        // we need to be careful in picking best routes; they are not always the shortest!
        Node current = util.find(n1table.getNeighbourAt(route[1]));
        int cost = n1.getNeighbours().get(current.getKey());
        while (cost < route[0]) {
            currentPath.add(current);
            if (current == n2) return;
            // case two
            if (current.getNeighbours().containsKey(n2.getKey())) {
                if (cost + current.getNeighbours().get(n2.getKey()) != route[0]) continue;
                cost += current.getNeighbours().get(n2.getKey());
                current = n2;
            } else {
                // case three
                char next = util.getNextRoute(current, n2, cost, route[0]);
                //char next = util.getShortestRoute(current);
                cost += current.getNeighbours().get(next);
                current = util.find(next);
            }
        }
        currentPath.add(n2);
        //printPath(n1, n2, route[0]);
    }

    /**
     * Check for errors and return the result of the check.
     */
    private boolean isError(boolean condition, String errorMsg) {
        if (condition) {
            sendToListeners(errorMsg);
            return true;
        } return false;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Node> getCurrentPath() {
        return currentPath;
    }

    private void sendToListeners(Object... args) {
        updateListeners(this, args);
    }

    /* ==================================================================================== */

    /**
     * Utilities class for common functions across the main class and the Node class.
     */
    public static class DVUtils {

        private List<Node> nodes;

        public DVUtils(List<Node> nodes) {
            this.nodes = nodes;
        }

        /**
         * Find the node with the given key and return it, or null if it doesn't exist.
         * @param key key of the node
         */
        public Node find(char key) {
            for (Node n : nodes)
                if (n.getKey() == key)
                    return n;
            return null;
        }

        /**
         * Get the best (that is, the least cost) route from the given row of a routing
         * table.
         * @param row row of the routing table
         * @return array containing the value of the least cost route in position 0 and the index
         * of the route in the row in position 1.
         */
        int[] getNextRoute(int[] row) {
            int val = -1, pos = -1;
            for (int i = 0; i < row.length; i++) {
                if (val == -1 && pos == -1) {
                    val = row[i];
                    pos = i;
                }
                else if (row[i] < val) {
                    val = row[i];
                    pos = i;
                }
            }
            return new int[]{val, pos};
        }

        /**
         * Get the least cost route from the neighbour list of a given node.
         * @param node node to analyse neighbours of
         * @return character key of the best neighbour
         */
        char getShortestRoute(Node node) {
            int val = -1;
            char best = '\u0000';
            for (Map.Entry m : node.getNeighbours().entrySet()) {
                if (val == -1 && best == '\u0000') {
                    val = (int) m.getValue();
                    best = (char) m.getKey();
                }
                else if ((int) m.getValue() < val) {
                    val = (int) m.getValue();
                    best = (char) m.getKey();
                }
            }
            return best;
        }

        /**
         * Get the next path to take from the neighbour list of a given node.
         * @param node node to analyse neighbours of
         * @param end destination node
         * @param cost cost so far
         * @param max maximum cost
         * @return character key of the next neighbour to visit
         */
        char getNextRoute(Node node, Node end, int cost, int max) {
            int val = -1;
            char best = '\u0000';
            for (Map.Entry m : node.getNeighbours().entrySet()) {
                if (val == -1 && best == '\u0000') {
                    val = (int) m.getValue();
                    best = (char) m.getKey();
                }
                else if (inspectRoute(node, (char) m.getKey(), end, cost, max)) {
                    val = (int) m.getValue();
                    best = (char) m.getKey();
                }
            }
            return best;
        }

        /**
         * Inspect the validity of a route. Any path that goes over the maximum cost is immediately
         * invalid. If the current node is the end node, the path is valid.
         * @param current current node in the path
         * @param next next node to visit
         * @param n2 end node
         * @param cost cost so far
         * @param max maximum cost
         */
        private boolean inspectRoute(Node current, char next, final Node n2, int cost, final int max) {
            if (current == n2) return true;
            if (cost > max) return false;
            return inspectRoute(find(next),
                    getShortestRoute(find(next)),
                    n2, cost + current.getNeighbours().get(next),
                    max);
        }
    }
}
