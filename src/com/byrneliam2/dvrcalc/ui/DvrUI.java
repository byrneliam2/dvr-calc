package com.byrneliam2.dvrcalc.ui;

/*
 * Liam Byrne (byrneliam2)
 * DVRCalculator
 */

import com.byrneliam2.dvrcalc.common.UIConstants;
import com.byrneliam2.dvrcalc.impl.DistanceVectorRouter;
import com.byrneliam2.dvrcalc.impl.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * A basic user interface that comprises a top tool bar and a main display area.
 */
public class DvrUI implements DvrUIListener {

    // Swing components
    JFrame master;
    JToolBar toolBar;
    JPanel display;

    // Other components
    protected DistanceVectorRouter router;
    private boolean terminates;

    /**
     * @param title title for the frame
     * @param terminates determines whether exiting this frame terminates the application
     */
    public DvrUI(String title, boolean terminates) {
        this.master = new JFrame(title);
        this.toolBar = new JToolBar();
        this.display = new JPanel();
        this.router = new DistanceVectorRouter(this);
        this.terminates = terminates;
    }

    /**
     * Start the building sequence to set up the UI. This involves, in order:
     * <li>- building the frame ({@link #buildFrame()}) </li>
     * <li>- building the tool bar ({@link #buildToolBar()}) </li>
     * <li>- building the buttons ({@link #buildButtons()}) </li>
     * <li>- building the display ({@link #buildMainPanel()}) </li>
     * <li>- completing the setup by adding and packing components ({@link #finishBuild()}) </li>
     * <br/>
     * These methods can be overridden by subclasses to achieve a different UI build.
     */
    public void build() {
        buildFrame();
        buildToolBar();
        buildButtons();
        buildMainPanel();
        finishBuild();
    }

    /**
     * Build the master frame on setup. By default, this sets the size of the window to be that as defined
     * by the product of WIDTH and HEIGHT in UIConstants. This calculation is applied to the display however,
     * not the frame, otherwise the frame's decorations are counted in the size.
     */
    protected void buildFrame() {
        master.setIconImage(new ImageIcon("resources/github.png").getImage());
        master.setDefaultCloseOperation(terminates ? WindowConstants.EXIT_ON_CLOSE : WindowConstants.DISPOSE_ON_CLOSE);

        display.setPreferredSize(new Dimension(UIConstants.WIDTH.getValue(), UIConstants.HEIGHT.getValue()));
    }

    /**
     * Build the tool bar at the top of the frame.
     */
    protected void buildToolBar() {
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.setFloatable(false);
    }

    /**
     * Build and add buttons to the tool bar.
     */
    protected void buildButtons() {
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
            if (onLoad()) trun.start();
        });
        run.addActionListener((e) -> {
            trun.stop();
            ElementUtilities.returnToDefaultColour(run);
            onRun();
        });
        route.addActionListener((e) -> onRoute());
        edit.addActionListener((e) -> new DvrEditorUI());
        toolBar.add(load);
        toolBar.add(run);
        toolBar.add(route);
        toolBar.add(edit);
    }

    /**
     * Build the main panel that fills the rest of the frame. This method is purposely
     * open so that other UI elements can extend this class and override this
     * method to add its own components.
     */
    protected void buildMainPanel() {
        // initial display on the graph side
        JPanel initial = new JPanel();
        JLabel begin = new JLabel();
        initial.setPreferredSize(new Dimension(UIConstants.WIDTH.getValue(),
                UIConstants.HEIGHT.getValue()));
        initial.setLayout(new BorderLayout());
        begin.setFont(new Font("Arial", Font.BOLD, 36));
        begin.setText("Please load a topology.");
        begin.setForeground(Color.LIGHT_GRAY);
        begin.setHorizontalAlignment(SwingConstants.CENTER);
        initial.add(begin, BorderLayout.CENTER);

        display.add(initial, BorderLayout.CENTER);
    }

    /**
     * Add all components and perform the operations needed to make the frame visible.
     */
    protected void finishBuild() {
        master.add(toolBar, BorderLayout.NORTH);
        master.add(display, BorderLayout.CENTER);
        master.pack();
        master.setVisible(true);
    }

    /* ============================================================================================================== */

    private boolean onLoad() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setApproveButtonText("Load");
        fileChooser.setDialogTitle("Select a topology to load");
        if (fileChooser.showOpenDialog(master) == JFileChooser.APPROVE_OPTION) {
            if (!router.onLoad(fileChooser.getSelectedFile())) return false;
            onDraw(router.getNodes());
            return true;
        }
        return false;
    }

    private void onRun() {
        router.onRun();
    }

    private void onRoute() {
        router.onRoute();
    }

    private void onDraw(List<Node> nodes) {
        display.removeAll();
        display.add(new DvrPanel(nodes), BorderLayout.CENTER);
        display.revalidate();
        display.repaint();
    }

//    /**
//     * Print the routing table for each node on the screen.
//     */
//    private void printAll() {
//        for (Node node : nodes) {
//            RoutingTable table = node.getTable();
//
//            // print the top neighbour line
//            UI.println("Node " + node.getKey());
//            UI.print("    ");
//            for (int i = 0; i < table.neighbourSize(); i++)
//                UI.print(table.getNeighbourAt(i) + " ");
//            UI.println();
//
//            // print the destination and the link values
//            for (int i = 0; i < table.destinationSize(); i++) {
//
//                // print the destination
//                UI.print(" " + table.getDestinationAt(i) + ": ");
//
//                // print all values from this row of the table
//                int[] row = table.getRow(i);
//                for (int j = 0; j < table.neighbourSize(); j++) {
//                    String str = row[j] + "";
//                    if (row[j] == Integer.MAX_VALUE) str = "/";
//                    UI.print(str + " ");
//                }
//                UI.println();
//            }
//            UI.println();
//        }
//        UI.println("-----------------");
//    }
//
//    /**
//     * Print the current path stored in the currentPath attribute.
//     * @param n1 node from
//     * @param n2 node to
//     * @param cost table cost
//     */
//    private void printPath(Node n1, Node n2, int cost) {
//        UI.println("From node " + n1.getKey() + " to " + n2.getKey());
//        UI.println("Total cost from table: " + cost);
//        UI.print("Path: ");
//        for (Node n : currentPath) {
//            UI.print(n.getKey() + " ");
//        } UI.println();
//        currentPath.clear();
//    }

    public Rectangle getDisplayBounds() {
        return display.getBounds();
    }

    @Override
    public void update(DvrUINotifier notifier, Object... args) {
        for (Object o : args) {
            if (o instanceof String)
                JOptionPane.showMessageDialog(master, o, "Warning", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /* ============================================================================================================== */

    /**
     * Helper inner class for the UI.
     */
    static class ElementUtilities {

        static final Color BUTTON_DEFAULT = Color.WHITE;
        static final Color BUTTON_SECONDARY = Color.LIGHT_GRAY;

        /**
         * Create a specialised button for the UI.
         * @param text text to go on the button
         * @return new JButton
         */
        static JButton giveButton(String text, Color col) {
            JButton button = new JButton(text);
            button.setPreferredSize(new Dimension(UIConstants.BUTTON_WIDTH.getValue(),
                    UIConstants.BUTTON_HEIGHT.getValue()));
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
