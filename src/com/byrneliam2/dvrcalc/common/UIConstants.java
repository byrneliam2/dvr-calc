package com.byrneliam2.dvrcalc.common;

/*
 * Liam Byrne (byrneliam2)
 * DVRCalculator
 */

public enum UIConstants {

    WIDTH(1152),
    HEIGHT(648);

    private int value;

    UIConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
