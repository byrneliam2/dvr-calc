import com.byrneliam2.dvrcalc.ui.DvrUI;

import javax.swing.*;

/*
 * Liam Byrne (byrneliam2)
 * DVRCalculator
 */
public class Launcher {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DvrUI::new);
    }

}
