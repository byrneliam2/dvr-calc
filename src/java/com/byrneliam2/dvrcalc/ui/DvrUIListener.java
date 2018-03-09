package com.byrneliam2.dvrcalc.ui;

/*
 * Liam Byrne (byrneliam2)
 * DVRCalculator
 */

/**
 * Custom observer implementation that makes up half of the listener/notifier system.
 */
public interface DvrUIListener {

    /**
     * Receiving point for notifications from a given subject. The notification may carry
     * a selection of object arguments. The subject that triggered the update is referenced
     * through a parameter.
     * @param notifier subject that triggered the update
     * @param args object arguments
     */
    void update(DvrUINotifier notifier, Object... args);

}
