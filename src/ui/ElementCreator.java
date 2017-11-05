package ui;

/*
 * Liam Byrne (byrneliam2)
 * DVRCalculator
 */

import javax.swing.*;
import java.awt.*;

class ElementCreator {

    static JButton makeButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(135, 35));
        button.setBackground(Color.WHITE);
        return button;
    }

}
