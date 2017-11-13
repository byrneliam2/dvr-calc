package com.byrneliam2.dvrcalc.editor;

/*
 * Liam Byrne (byrneliam2)
 * DVRCalculator
 */

import com.byrneliam2.dvrcalc.common.UIConstants;
import com.byrneliam2.dvrcalc.ui.DvrUI;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class DvrEditor extends DvrUI {

    private JTextArea editor;

    public DvrEditor() {
        super("DVR Editor", false);
        editor = new JTextArea();

        build();
    }

    @Override
    protected void buildFrame() {
        super.buildFrame();
        master.setResizable(false);
    }

    @Override
    public void buildMainPanel() {
        editor.setPreferredSize(new Dimension(UIConstants.WIDTH.getValue(), UIConstants.HEIGHT.getValue()));
        editor.setFont(new Font("Courier New", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane();
        DefaultCaret caret = (DefaultCaret) editor.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scroll.setViewportView(editor);
        display.add(scroll, BorderLayout.CENTER);
    }

    @Override
    protected void buildButtons() {
        //
    }
}
