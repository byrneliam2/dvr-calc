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
    protected void buildFrame() {
        super.buildFrame();
        master.setResizable(false);
    }

    @Override
    public void buildMainPanel() {
        //
    }

    @Override
    protected void buildToolBar() {
        //
    }
}
