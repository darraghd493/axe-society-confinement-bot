package me.darragh.axesociety.confinementbot.command.impl;

import me.darragh.axesociety.confinementbot.command.Command;
import me.darragh.axesociety.confinementbot.command.CommandRegistry;
import me.darragh.axesociety.confinementbot.util.LazyFinalReference;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * Determines the Discord API latency.
 *
 * @author darraghd493
 * @since 1.0.0
 */
public class PingCommand extends Command {
    public PingCommand() {
        super("ping", "Determines the Discord API latency.");
    }

    @Override
    public void handleInteraction(SlashCommandInteractionEvent event) {
        long time = System.currentTimeMillis();
        event.reply("Pong!").queue(response -> {
            long restLatency = System.currentTimeMillis() - time,
                    gatewayLatency = event.getJDA().getGatewayPing();
            response.editOriginalFormat("Pong! 🏓\nAPI Latency: %dms\nGateway Latency: %dms", restLatency, gatewayLatency).queue();
        });
    }
}
