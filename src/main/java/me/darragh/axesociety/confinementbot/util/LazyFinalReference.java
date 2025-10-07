package me.darragh.axesociety.confinementbot.util;

import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

/**
 * A thread-safe lazy-final wrapper.
 * <br/>
 * This delays the initialisation of an object until it is first accessed.
 *
 * @param <T> The type of the object being referenced.
 */
@RequiredArgsConstructor
public class LazyFinalReference<T> {
    private final Supplier<T> supplier;
    private final FinalReference<T> instance = new FinalReference<>();

    public T get() {
        T value = this.instance.get();
        if (value != null) {
            return value;
        }

        synchronized (this.instance) {
            value = this.instance.get();
            if (value == null) {
                value = this.supplier.get();
                this.instance.set(value);
            }
            return value;
        }
    }

    public boolean isSet() {
        return this.instance.isSet();
    }
}
