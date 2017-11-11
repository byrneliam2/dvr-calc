package com.byrneliam2.dvrcalc.ui;

/*
 * Liam Byrne (byrneliam2)
 * DVRCalculator
 */

import com.byrneliam2.dvrcalc.impl.DistanceVectorRouter;
import com.byrneliam2.dvrcalc.impl.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * Primary hosting class for the UI component set.
 */
public class DvrUI implements DvrUIListener {

    // Swing components
    private JFrame master;
    private JPanel display;
    private JTextArea text;

    // Other components
    private DistanceVectorRouter router;

    static final int WIDTH = 1152;
    static final int HEIGHT = 648;

    public DvrUI() {
        master = new JFrame("DVR Calculator");
        router = new DistanceVectorRouter(this);
        buildFrame();
    }

    /**
     * Build the master frame on setup.
     */
    private void buildFrame() {
        master.setIconImage(new ImageIcon("res/github.png").getImage());
        master.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        master.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //master.setResizable(false);

        buildMainPanel();

        master.pack();
        master.setVisible(true);
    }

    /**
     * Build the main panel that fills the rest of the frame. This method is purposely
     * package private so that other UI elements can extend this class and override this
     * method to add its own components.
     */
    @SuppressWarnings("WeakerAccess")
    void buildMainPanel() {
        // top tool bar
        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.setFloatable(false);
        buildToolBar(toolBar);

        // initial display on the graph side
        JPanel initial = new JPanel();
        JLabel begin = new JLabel();
        initial.setPreferredSize(new Dimension(2*WIDTH/3, HEIGHT));
        initial.setLayout(new BorderLayout());
        begin.setFont(new Font("Arial", Font.BOLD, 36));
        begin.setText("Please load a topology.");
        begin.setForeground(Color.LIGHT_GRAY);
        begin.setHorizontalAlignment(SwingConstants.CENTER);
        initial.add(begin, BorderLayout.CENTER);

        /*// side text area
        JScrollPane scroll = new JScrollPane();
        text = new JTextArea();
        DefaultCaret caret = (DefaultCaret) text.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scroll.setViewportView(text);*/

        display = new JPanel();
        display.add(initial, BorderLayout.CENTER);

        master.add(toolBar, BorderLayout.NORTH);
        master.add(display, BorderLayout.CENTER);
    }

    /**
     * Build the tool bar at the top of the frame. This is mostly adding buttons and binding
     * animation timers to them.
     * @param toolBar tool bar to build
     */
    private void buildToolBar(JToolBar toolBar) {
        JButton load = ElementUtilities.giveButton("Load", ElementUtilities.BUTTON_DEFAULT);
        JButton run = ElementUtilities.giveButton("Run", ElementUtilities.BUTTON_DEFAULT);
        JButton route = ElementUtilities.giveButton("Route", ElementUtilities.BUTTON_DEFAULT);
        JButton edit = ElementUtilities.giveButton("Graph Editor", ElementUtilities.BUTTON_SECONDARY);

        Timer tload = ElementUtilities.giveFlashingAnimation(load, Color.GREEN);
        Timer trun = ElementUtilities.giveFlashingAnimation(run, Color.GREEN);
        tload.start();
        load.addActionListener((e) -> {
            tload.stop();
            trun.stop();
            ElementUtilities.returnToDefaultColour(load);
            onLoad();
            trun.start();
        });
        run.addActionListener((e) -> {
            trun.stop();
            ElementUtilities.returnToDefaultColour(run);
            onRun();
        });
        route.addActionListener((e) -> onRoute());
        toolBar.add(load);
        toolBar.add(run);
        toolBar.add(route);
        toolBar.add(edit);
    }

    private void onLoad() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setApproveButtonText("Load");
        fileChooser.setDialogTitle("Select a topology to load");
        if (fileChooser.showOpenDialog(master) == JFileChooser.APPROVE_OPTION) {
            router.onLoad(fileChooser.getSelectedFile());
            onDraw(router.getNodes());
        }
    }

    private void onRun() {
        router.onRun();
    }

    private void onRoute() {
        router.onRoute();
    }

    /**
     * Draw the graph on the screen.
     */
    private void onDraw(List<Node> nodes) {
        display.removeAll();
        display.add(new DvrPanel(nodes), BorderLayout.CENTER);
        display.revalidate();
        display.repaint();
    }

    /**
     * Print the routing table for each node on the screen.
     */
    /*private void printAll() {
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

    *//**
     * Report the time taken to execute the DVR algorithm.
     *//*
    private void printTime(long time) {
        UI.println("Time: " +
                new DecimalFormat("#.####").format(time/Math.pow(10, 6))
                + " milliseconds.");
    }

    *//**
     * Print the current path stored in the currentPath attribute.
     * @param n1 node from
     * @param n2 node to
     * @param cost table cost
     *//*
    private void printPath(Node n1, Node n2, int cost) {
        UI.println("From node " + n1.getKey() + " to " + n2.getKey());
        UI.println("Total cost from table: " + cost);
        UI.print("Path: ");
        for (Node n : currentPath) {
            UI.print(n.getKey() + " ");
        } UI.println();
        currentPath.clear();
    }*/

    /**
     * Print a message to the screen.
     * @param message string message
     */
    private void print(String message) {
        text.append(message);
    }

    public Rectangle getFrameBounds() {
        return master.getBounds();
    }

    @Override
    public void update(DvrUINotifier notifier, Object... args) {
        for (Object o : args) {
            print(o + "\n");
        }
    }

    /* ============================================================================================================== */

    /**
     * Helper inner class for the UI.
     */
    static class ElementUtilities {

        private static final Color BUTTON_DEFAULT = Color.WHITE;
        private static final Color BUTTON_SECONDARY = Color.LIGHT_GRAY;

        /**
         * Create a specialised button for the UI.
         * @param text text to go on the button
         * @return new JButton
         */
        static JButton giveButton(String text, Color col) {
            JButton button = new JButton(text);
            button.setPreferredSize(new Dimension(135, 35));
            button.setBackground(col);
            return button;
        }

        /**
         * Give a component a flashing animation that changes every second.
         * @param j component
         * @return new Swing timer
         */
        static Timer giveFlashingAnimation(JComponent j, Color col) {
            return new Timer(1000, new ActionListener() {
                boolean flash = true;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (flash)
                        j.setBackground(col);
                    else j.setBackground(BUTTON_DEFAULT);
                    flash = !flash;
                }
            });
        }

        /**
         * Reset a component's colour to default.
         * @param j component
         */
        static void returnToDefaultColour(JComponent j) {
            j.setBackground(BUTTON_DEFAULT);
        }
    }
}
