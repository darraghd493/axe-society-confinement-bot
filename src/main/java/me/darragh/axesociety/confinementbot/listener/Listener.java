package me.darragh.axesociety.confinementbot.listener;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Outlines the core functionality of a listener.
 *
 * @author darraghd493
 * @since 1.0.0
 */
public class Listener extends ListenerAdapter {
    private final JDABuilder builder;

    public Listener(JDABuilder builder) {
        this.builder = builder;
    }

    public void register() {
        this.builder.addEventListeners(this);
    }
}
