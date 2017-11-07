package ui;

/*
 * Liam Byrne (byrneliam2)
 * DVRCalculator
 */

import impl.DistanceVectorRouter;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

/**
 * Primary hosting class for the UI component set.
 */
public class DvrUI {

    // Swing components
    private JFrame master;
    private JPanel display;
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

        // top tool bar and buttons
        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.setFloatable(false);
        JButton load = ElementCreator.makeButton("Load");
        JButton run = ElementCreator.makeButton("Run");
        JButton route = ElementCreator.makeButton("Route");
        JButton edit = ElementCreator.makeButton("Edit");
        toolBar.add(load);
        toolBar.add(run);
        toolBar.add(route);
        toolBar.add(edit);

        // graph display
        display = new JPanel();
        display.setPreferredSize(new Dimension(2*WIDTH/3, HEIGHT));

        // side text area
        JScrollPane scroll = new JScrollPane();
        text = new JTextArea();
        DefaultCaret caret = (DefaultCaret) text.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scroll.setViewportView(text);

        // split pane that holds display and text area
        JSplitPane pane = new JSplitPane();
        pane.setTopComponent(display);
        pane.setBottomComponent(scroll);

        master.add(toolBar, BorderLayout.NORTH);
        master.add(pane, BorderLayout.CENTER);

        master.pack();
        master.setVisible(true);
    }

    private void addMainPanel() {

    }

    public void paint() {

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
