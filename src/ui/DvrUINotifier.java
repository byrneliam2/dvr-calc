package ui;

/*
 * Liam Byrne (byrneliam2)
 * DVRCalculator
 */

import java.util.ArrayList;
import java.util.List;

public abstract class DvrUINotifier {

    private List<DvrUIListener> listeners;

    public DvrUINotifier() {
        this.listeners = new ArrayList<>();
    }

    protected synchronized void addListener(DvrUIListener l) {
        listeners.add(l);
    }

    protected synchronized void updateListeners(DvrUINotifier notifier, Object... args) {
        for (DvrUIListener l : listeners)
            l.update(notifier, args);
    }
}
