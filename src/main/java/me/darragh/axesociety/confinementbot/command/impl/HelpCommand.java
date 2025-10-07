package me.darragh.axesociety.confinementbot.command.impl;

import me.darragh.axesociety.confinementbot.command.Command;
import me.darragh.axesociety.confinementbot.command.CommandRegistry;
import me.darragh.axesociety.confinementbot.util.LazyFinalReference;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * Provides users with information about available commands.
 *
 * @author darraghd493
 * @since 1.0.0
 */
public class HelpCommand extends Command {
    private final LazyFinalReference<String> message = new LazyFinalReference<>(
            () -> {
                StringBuilder sb = new StringBuilder("**Available Commands:**\n");
                for (Command command : CommandRegistry.getAllCommands().values()) {
                    sb.append(String.format("`/%s` - %s\n", command.getName(), command.getDescription()));
                }
                return sb.toString();
            }
    );

    public HelpCommand() {
        super("help", "Provides information about available commands.");
    }

    @Override
    public void handleInteraction(SlashCommandInteractionEvent event) {
        event.reply(this.message.get()).setEphemeral(true).queue();
    }
}
