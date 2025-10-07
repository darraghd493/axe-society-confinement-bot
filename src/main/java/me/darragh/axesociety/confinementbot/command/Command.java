package me.darragh.axesociety.confinementbot.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

/**
 * Represents a command in the confinement bot.
 *
 * @author darraghd493
 * @since 1.0.0
 */
@Getter
@RequiredArgsConstructor
public abstract class Command {
    private final String name, description;

    public abstract void handleInteraction(SlashCommandInteractionEvent event);

    public SlashCommandData getCommandData() {
        return Commands.slash(this.getName(), this.getDescription());
    }
}
