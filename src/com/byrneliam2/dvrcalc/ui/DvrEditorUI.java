package com.byrneliam2.dvrcalc.ui;

/*
 * Liam Byrne (byrneliam2)
 * DVRCalculator
 */

import com.byrneliam2.dvrcalc.common.UIConstants;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static com.byrneliam2.dvrcalc.ui.DvrUI.ElementUtilities.*;

/**
 * Standalone editor that can be launched from within the main application. This uses
 * the DvrUI class to generate a slightly different version of the regular UI.
 */
public class DvrEditorUI extends DvrUI {

    private JTextArea editor;
    private JScrollPane scroll;
    private JLabel indicator;

    private File currentFile;

    DvrEditorUI(File currentFile) {
        super("DVR Editor", false);
        this.editor = new JTextArea();
        this.scroll = new JScrollPane();
        this.indicator = new JLabel();
        this.currentFile = currentFile;

        build();
    }

    @Override
    protected void buildFrame() {
        super.buildFrame();
        master.setResizable(false);
    }

    @Override
    public void buildMainPanel() {
        editor.setFont(new Font("Courier New", Font.PLAIN, 14));
        editor.getDocument().addDocumentListener(new DocumentListener() {
             @Override public void insertUpdate(DocumentEvent e) { updateIndicator(); }
             @Override public void removeUpdate(DocumentEvent e) { updateIndicator(); }
             @Override public void changedUpdate(DocumentEvent e) {}
         });

        // Need to shave off some extra height to accommodate the JLabel underneath comfortably
        scroll.setPreferredSize(new Dimension(UIConstants.WIDTH.getValue() - UIConstants.EDITOR_BUFFER.getValue(),
                UIConstants.HEIGHT.getValue() - UIConstants.EDITOR_BUFFER.getValue() * 3));
        DefaultCaret caret = (DefaultCaret) editor.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scroll.setViewportView(editor);
        display.add(scroll, BorderLayout.CENTER);

        /*int caretOffset = txt.getCaretPosition();
        int lineNumber = txt.getLineOfOffset(caretOffset);
        int startOffset = txt.getLineStartOffset(lineNumber);
        int endOffset = txt.getLineEndOffset(lineNumber);*/
        display.add(indicator, BorderLayout.SOUTH);
        updateEditor();
        updateIndicator();
    }

    @Override
    protected void buildButtons() {
        JButton save = giveButton("Save", BUTTON_DEFAULT);

        save.addActionListener((e) -> onSave());
        toolBar.add(save);
    }

    private void updateEditor() {
        if (currentFile != null) {
            try {
                Scanner s = new Scanner(currentFile);
                while (s.hasNextLine()) editor.append(s.nextLine() + "\n");
                s.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateIndicator() {
        indicator.setText(editor.getCaretPosition()+"");
    }

    /* ============================================================================================================== */

    private void onSave() {
        // TODO save to currently open graph file and reload
    }
}
