package me.darragh.axesociety.confinementbot.util;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicReference;

/**
 * A thread-safe final reference wrapper.
 *
 * @param <T> The type of the reference.
 * @author darraghd493
 * @since 1.0.0
 */
public class FinalReference<T> {
    private final AtomicReference<T> reference = new AtomicReference<>();

    public void set(T value) {
        if (!this.reference.compareAndSet(null, value)) {
            throw new IllegalStateException("Cannot set a final reference more than once.");
        }
    }

    public T get() {
        return this.reference.get();
    }

    public boolean isSet() {
        return this.reference.get() != null;
    }
}
