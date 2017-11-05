package ui;

/*
 * Liam Byrne (byrneliam2)
 * DVRCalculator
 */

import javax.swing.*;
import java.awt.*;

public class DvrUI {

    private JFrame master;
    private JSplitPane pane;
    private JPanel display;
    private JTextArea text;

    private static final int WIDTH = 1152;
    private static final int HEIGHT = 648;

    public DvrUI() {
        master = new JFrame("DVR Calculator");
        buildFrame();
    }

    private void buildFrame() {
        master.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        master.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        master.setResizable(false);

        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        toolBar.setFloatable(false);
        JButton load = ElementFactory.makeButton("Load");
        JButton run = ElementFactory.makeButton("Run");
        toolBar.add(load);
        toolBar.add(run);

        display = new JPanel();
        display.setPreferredSize(new Dimension(2*WIDTH/3, HEIGHT));
        text = new JTextArea();

        pane = new JSplitPane();
        pane.setTopComponent(display);
        pane.setBottomComponent(text);

        master.add(toolBar, BorderLayout.NORTH);
        master.add(pane, BorderLayout.CENTER);

        master.pack();
        master.setVisible(true);
    }
}
