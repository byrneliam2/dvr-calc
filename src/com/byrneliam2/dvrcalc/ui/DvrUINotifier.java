package com.byrneliam2.dvrcalc.ui;

/*
 * Liam Byrne (byrneliam2)
 * DVRCalculator
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Subject abstraction that allows subclasses to send notifications to listeners
 * at any given point. The listeners are stored in an ordered collection and cre updated
 * in that order.
 */
public abstract class DvrUINotifier {

    private List<DvrUIListener> listeners;

    public DvrUINotifier() {
        this.listeners = new ArrayList<>();
    }

    protected void addListener(DvrUIListener l) {
        listeners.add(l);
    }

    protected void updateListeners(DvrUINotifier notifier, Object... args) {
        for (DvrUIListener l : listeners)
            l.update(notifier, args);
    }
}
