package me.darragh.axesociety.confinementbot.listener;

import lombok.extern.slf4j.Slf4j;
import me.darragh.axesociety.confinementbot.command.CommandRouter;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * Listens for command events and handles them.
 *
 * @author darraghd493
 * @since 1.0.0
 */
@Slf4j
public class CommandListener extends Listener {
    public CommandListener(JDABuilder builder) {
        super(builder);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        CommandRouter.route(event);
    }
}
