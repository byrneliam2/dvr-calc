package impl;

import ecs100.*;

import java.awt.Color;
import java.text.DecimalFormat;
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
public class DistanceVectorRouter {

    private List<Node> nodes = new ArrayList<>();
    private List<Node> currentPath = new ArrayList<>();

    // status checkers
    private boolean hasBeenRun;
    private boolean hasBeenLoaded;
    private boolean hasNewLink;

    public DistanceVectorRouter() {
        UI.setDivider(0.25);
        UI.setWindowSize(1152, 648);
        UI.getFrame().setTitle("Distance Vector Router");
        UI.initialise();

        UI.addButton("Load Map", this::onLoad);
        UI.addButton("", () -> {});
        UI.addButton("Run", this::onStart).setBackground(Color.GREEN);
        UI.addButton("Add Link", this::onAddLink);
        UI.addButton("Random Route", this::onRoute);

        hasBeenRun = false;
        hasBeenLoaded = false;
        hasNewLink = false;
    }

    /**
     * Run the distance vector routing algorithm.
     */
    private void onStart() {
        if (!hasBeenLoaded) {
            printError("Please load a topology first.");
            return;
        }

        Timer.start();
        for (int i = 0; i < nodes.size(); i++) {
            //1. initialise
            //2. update neighbours
            for (Node node : this.nodes) {
                for (Map.Entry m : node.getNeighbours().entrySet()) {
                    char key = (char) m.getKey();
                    int dist = (int) m.getValue();
                    Node neighbour = new DVUtils(nodes).find(key);
                    assert neighbour != null;
                    // send the table of this node to the neighbour
                    neighbour.updateRoutingTable(node, dist, node.getTable());
                }
            }
        }
        long time = Timer.stop();

        printAll();
        printTime(time);

        hasBeenRun = true;
    }

    /**
     * Add one new node to the graph. The node always appears in the same place with the same key
     * but it has a varying number of neighbours every time.
     */
    private void onAddLink() {
        if (!hasBeenRun) {
            printError("DVR algorithm must be executed\nbefore links can be added.");
            return;
        }
        if (hasNewLink) {
            printError("New link already exists.");
            return;
        }

        // create a new node
        Node q = new Node('Q', 500, 500);
        nodes.add(q);

        // randomly assign it neighbours
        while (q.getNeighbours().isEmpty()) {
            for (Node n : nodes) {
                if (n == q) continue;
                if (Math.random() > 0.5) {
                    int cost = (int) (Math.random() * 10);
                    while (cost == 0) cost = (int) (Math.random() * 10);
                    // the simulateNewLink method combines the addition
                    // of q as a neighbour and resetting its table
                    q.addNeighbour(n.getKey(), cost);
                    n.simulateNewLink(q.getKey(), cost, nodes);
                }
                else {
                    // reset routing table to contain the new node
                    // as a destination
                    n.setupRoutingTable(nodes);
                }
            }
        }
        // set up q's table now that all neighbours are added
        q.setupRoutingTable(nodes);

        // redraw
        UI.clearGraphics();
        onStart();
        draw();

        hasNewLink = true;
    }

    /**
     * Generate a route between two random nodes using the complete routing table.
     */
    private void onRoute() {
        if (!hasBeenRun) {
            printError("DVR algorithm must be executed\nbefore routing can be performed.");
            return;
        }

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
        printPath(n1, n2, route[0]);
    }

    /**
     * Load the nodes from the topology file and setup the routing table for each node.
     */
    private void onLoad() {
        // clean up on every load
        nodes.clear();
        hasBeenRun = false;
        hasNewLink = false;
        UI.clearPanes();

        try {
            Scanner scan = new Scanner(new File(UIFileChooser.open("Select Map File")));
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

            draw();
            printAll();
            hasBeenLoaded = true;
        } catch (IOException | NullPointerException e) {
            UI.println("File Failure: " + e);
        }
    }

    /**
     * Draw the graph on the screen.
     */
    private void draw() {
        for (Node n : this.nodes) {
            UI.setColor(Color.BLACK);
            UI.drawOval(n.getX(), n.getY(), 40, 40);
            UI.setColor(Color.BLUE);
            UI.drawString(n.getKey() + "", n.getX() + 5, n.getY() + 22);

            UI.setColor(Color.RED);

            // loop on all neighbours
            Set<Character> keys = n.getNeighbours().keySet();
            for (char k : keys) {
                // Search in the list of nodes for this node wth name "s"
                Node neighbour = new DVUtils(nodes).find(k);
                if (neighbour != null) // there is a neighbour
                {
                    UI.drawLine(n.getX() + 20, n.getY() + 20,
                            neighbour.getX() + 20, neighbour.getY() + 20);
                    UI.drawString(n.getNeighbours().get(k) + "",
                            n.getX() + ((neighbour.getX() - n.getX())/2),
                            n.getY() + ((neighbour.getY() - n.getY())/2) + 15);
                }
            }
        }
    }

    /**
     * Print the routing table for each node on the screen.
     */
    private void printAll() {
        for (Node node : nodes) {
            RoutingTable table = node.getTable();

            // print the top neighbour line
            UI.println("Node " + node.getKey());
            UI.print("    ");
            for (int i = 0; i < table.neighbourSize(); i++)
                UI.print(table.getNeighbourAt(i) + " ");
            UI.println();

            // print the destination and the link values
            for (int i = 0; i < table.destinationSize(); i++) {

                // print the destination
                UI.print(" " + table.getDestinationAt(i) + ": ");

                // print all values from this row of the table
                int[] row = table.getRow(i);
                for (int j = 0; j < table.neighbourSize(); j++) {
                    String str = row[j] + "";
                    if (row[j] == Integer.MAX_VALUE) str = "/";
                    UI.print(str + " ");
                }
                UI.println();
            }
            UI.println();
        }
        UI.println("-----------------");
    }

    /**
     * Report the time taken to execute the DVR algorithm.
     */
    private void printTime(long time) {
        UI.println("Time: " +
                new DecimalFormat("#.####").format(time/Math.pow(10, 6))
                + " milliseconds.");
    }

    /**
     * Print the current path stored in the currentPath attribute.
     * @param n1 node from
     * @param n2 node to
     * @param cost table cost
     */
    private void printPath(Node n1, Node n2, int cost) {
        UI.println("From node " + n1.getKey() + " to " + n2.getKey());
        UI.println("Total cost from table: " + cost);
        UI.print("Path: ");
        for (Node n : currentPath) {
            UI.print(n.getKey() + " ");
        } UI.println();
        currentPath.clear();
    }

    private void printError(String msg) {
        UI.println(msg);
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
        @SuppressWarnings("WeakerAccess")
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
        public int[] getNextRoute(int[] row) {
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
        public char getShortestRoute(Node node) {
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
        public char getNextRoute(Node node, Node end, int cost, int max) {
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

    /* =================================================================================== */

    private static class Timer {

        private static long time;

        public static void start() {
            time = System.nanoTime();
        }

        public static long stop() {
            long out = System.nanoTime() - time;
            time = 0;
            return out;
        }
    }
}
