package com.byrneliam2.dvrcalc.common;

/*
 * Liam Byrne (byrneliam2)
 * DVRCalculator
 */

public enum UIConstants {

    WIDTH(1152),
    HEIGHT(640),
    BUTTON_WIDTH(135),
    BUTTON_HEIGHT(35),
    EDITOR_BUFFER(10);

    private int value;

    UIConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
