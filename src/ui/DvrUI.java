package ui;

/*
 * Liam Byrne (byrneliam2)
 * DVRCalculator
 */

import impl.DistanceVectorRouter;
import impl.Node;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.util.List;
import java.util.Set;

/**
 * Primary hosting class for the UI component set.
 */
public class DvrUI {

    // Swing components
    private JFrame master;
    private JSplitPane pane;
    private JTextArea text;

    // Other components
    private DistanceVectorRouter router;

    private static final int WIDTH = 1152;
    private static final int HEIGHT = 648;

    public DvrUI() {
        master = new JFrame("DVR Calculator");
        router = new DistanceVectorRouter();
        buildFrame();
    }

    /**
     * Build the master frame on setup.
     */
    private void buildFrame() {
        master.setIconImage(new ImageIcon("res/github.png").getImage());
        master.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        master.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        master.setResizable(false);

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
        // top tool bar and buttons
        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.setFloatable(false);
        JButton load = ElementCreator.makeButton("Load");
        JButton run = ElementCreator.makeButton("Run");
        JButton route = ElementCreator.makeButton("Route");
        JButton edit = ElementCreator.makeButton("Edit");
        load.addActionListener((e) -> router.onLoad());
        toolBar.add(load);
        toolBar.add(run);
        toolBar.add(route);
        toolBar.add(edit);

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

        // side text area
        JScrollPane scroll = new JScrollPane();
        text = new JTextArea();
        DefaultCaret caret = (DefaultCaret) text.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scroll.setViewportView(text);

        // split pane that holds display and text area
        pane = new JSplitPane();
        pane.setTopComponent(initial);
        pane.setBottomComponent(scroll);

        master.add(toolBar, BorderLayout.NORTH);
        master.add(pane, BorderLayout.CENTER);
    }

    /**
     * Draw the graph on the screen.
     */
    private void draw(List<Node> nodes) {
        pane.setTopComponent(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                for (Node n : nodes) {
                    g.setColor(Color.BLACK);
                    g.drawOval(n.getX(), n.getY(), 40, 40);
                    g.setColor(Color.BLUE);
                    g.drawString(n.getKey() + "", n.getX() + 5, n.getY() + 22);

                    g.setColor(Color.RED);

                    // loop on all neighbours
                    Set<Character> keys = n.getNeighbours().keySet();
                    for (char k : keys) {
                        // Search in the list of nodes for this node wth name "s"
                        Node neighbour = new DistanceVectorRouter.DVUtils(nodes).find(k);
                        if (neighbour != null) // there is a neighbour
                        {
                            g.drawLine(n.getX() + 20, n.getY() + 20,
                                    neighbour.getX() + 20, neighbour.getY() + 20);
                            g.drawString(n.getNeighbours().get(k) + "",
                                    n.getX() + ((neighbour.getX() - n.getX())/2),
                                    n.getY() + ((neighbour.getY() - n.getY())/2) + 15);
                        }
                    }
                }
            }
        });
    }

    /**
     * Print a message to the screen.
     * @param message string message
     */
    public void print(String message) {
        text.append(message);
    }

    /* ============================================================================================================== */

    /**
     * Helper inner class for the UI.
     */
    static class ElementCreator {

        /**
         * Create a specialised button for the UI.
         * @param text text to go on the button
         * @return new JButton
         */
        static JButton makeButton(String text) {
            JButton button = new JButton(text);
            button.setPreferredSize(new Dimension(135, 35));
            button.setBackground(Color.WHITE);
            return button;
        }

    }
}
