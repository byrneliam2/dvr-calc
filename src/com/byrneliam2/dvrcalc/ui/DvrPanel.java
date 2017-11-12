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
import java.util.List;
import java.util.Set;

/**
 * Custom JPanel for displaying DVR data.
 */
public class DvrPanel extends JPanel {

    private List<Node> nodes;

    DvrPanel(List<Node> nodes) {
        super();
        this.nodes = nodes;

        //this.setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // setup graphics object
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Arial", Font.BOLD, 14));

        for (Node n : nodes) {
            // draw all links first
            g2.setColor(Color.BLACK);
            // loop on all neighbours
            Set<Character> keys = n.getNeighbours().keySet();
            for (char k : keys) {
                // TODO make links only draw once
                // Search in the list of nodes for this node wth name "s"
                Node neighbour = new DistanceVectorRouter.DVUtils(nodes).find(k);
                if (neighbour != null) // there is a neighbour
                {
                    g2.drawLine(n.getX() + 20, n.getY() + 20,
                            neighbour.getX() + 20, neighbour.getY() + 20);
                    g2.drawString(n.getNeighbours().get(k) + "",
                            n.getX() + ((neighbour.getX() - n.getX())/2),
                            n.getY() + ((neighbour.getY() - n.getY())/2) + 15);
                }
            }
        }

        for (Node n : nodes) {
            // draw nodes over top
            g2.setColor(Color.WHITE);
            g2.fillOval(n.getX(), n.getY(), Node.BOUNDS, Node.BOUNDS);
            g2.setColor(Color.BLACK);
            g2.drawOval(n.getX(), n.getY(), Node.BOUNDS, Node.BOUNDS);
            g2.drawString(n.getKey() + "", n.getX() + Node.BOUNDS/2 - g.getFont().getSize()/4,
                    n.getY() + Node.BOUNDS/2 + g.getFont().getSize()/4);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(UIConstants.WIDTH.getValue(), UIConstants.HEIGHT.getValue());
    }
}
