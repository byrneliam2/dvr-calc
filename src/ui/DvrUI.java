package ui;

/*
 * Liam Byrne (byrneliam2)
 * DVRCalculator
 */

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class DvrUI {

    // Swing components
    private JFrame master;
    private JSplitPane pane;
    private JPanel display;
    private JScrollPane scroll;
    private JTextArea text;

    private static final int WIDTH = 1152;
    private static final int HEIGHT = 648;

    public DvrUI() {
        master = new JFrame("DVR Calculator");
        buildFrame();
    }

    /**
     * Build the master frame on setup.
     */
    private void buildFrame() {
        master.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        master.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        master.setResizable(false);

        // top tool bar and buttons
        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.setFloatable(false);
        JButton load = ElementFactory.makeButton("Load");
        JButton run = ElementFactory.makeButton("Run");
        toolBar.add(load);
        toolBar.add(run);

        // graph display
        display = new JPanel();
        display.setPreferredSize(new Dimension(2*WIDTH/3, HEIGHT));

        // side text area
        scroll = new JScrollPane();
        text = new JTextArea();
        DefaultCaret caret = (DefaultCaret) text.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scroll.setViewportView(text);

        // split pane that holds display and text area
        pane = new JSplitPane();
        pane.setTopComponent(display);
        pane.setBottomComponent(scroll);

        master.add(toolBar, BorderLayout.NORTH);
        master.add(pane, BorderLayout.CENTER);

        master.pack();
        master.setVisible(true);
    }

    /**
     * Print a message to the screen.
     * @param message string message
     */
    public void print(String message) {
        text.append(message);
    }
}
