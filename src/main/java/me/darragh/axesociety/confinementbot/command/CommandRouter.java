package me.darragh.axesociety.confinementbot.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Routes commands to their respective handlers.
 *
 * @author darraghd493
 * @since 1.0.0
 */
public class CommandRouter {
    private static final ExecutorService COMMAND_EXECUTOR = Executors.newFixedThreadPool(
            Math.min(2, Runtime.getRuntime().availableProcessors() * 2 - 1)
    );

    public static void route(SlashCommandInteractionEvent event) {
        Command command = CommandRegistry.COMMANDS.get(event.getName());
        if (command == null) {
            event.reply("Unknown command. Use `/help` to see the list of available commands.")
                    .setEphemeral(true)
                    .queue();
        } else {
            // Submit command handling to the thread pool
            COMMAND_EXECUTOR.submit(() -> {
                try {
                    command.handleInteraction(event);
                } catch (Exception e) {
                    event.reply("An error occurred while executing the command.")
                            .setEphemeral(true)
                            .queue();
                    e.printStackTrace();
                }
            });
        }
    }

    public static void shutdown() {
        COMMAND_EXECUTOR.shutdown();
    }
}
