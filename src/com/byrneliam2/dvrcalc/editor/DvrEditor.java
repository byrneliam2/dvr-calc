package com.byrneliam2.dvrcalc.editor;

/*
 * Liam Byrne (byrneliam2)
 * DVRCalculator
 */

import com.byrneliam2.dvrcalc.ui.DvrUI;

import javax.swing.*;

public class DvrEditor extends DvrUI {

    public DvrEditor() {
        super("DVR Editor", false);
    }

    @Override
    protected void buildFrame(boolean terminate) {
        super.buildFrame(terminate);
        master.setResizable(false);
    }

    @Override
    public void buildMainPanel() {
        //
    }

    @Override
    protected void buildToolBar(JToolBar toolBar) {
        super.buildToolBar(toolBar);
    }
}
