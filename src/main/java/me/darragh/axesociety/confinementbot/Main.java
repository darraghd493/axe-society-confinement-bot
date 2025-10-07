package me.darragh.axesociety.confinementbot;

import lombok.extern.slf4j.Slf4j;

/**
 * Safely handles the bot launch, serving as the main entry point.
 *
 * @author darraghd493
 * @since 1.0.0
 */
@Slf4j
public class Main {
    /**
     * Main entry point for the application.
     *
     * @param args Command line arguments, can include environment variable overrides in the format --VAR_NAME=VALUE.
     */
    public static void main(String[] args) {
        // Load any variables from the args if provided
        for (String arg : args) {
            if (arg.startsWith("--")) {
                String[] parts = arg.substring(2).split("=", 2);
                if (parts.length == 2) {
                    System.setProperty(parts[0], parts[1]);
                }
            }
        }

        // Validate environment variables
        BotConfig.init();
        if (!BotConfig.validate()) {
            System.err.println("One or more required variables are missing.");
            System.err.println("Please ensure the following variables are set:");
            System.err.println("- DISCORD_BOT_TOKEN - The token for your Discord bot.");
            System.err.println("- DISCORD_GUILD_ID - The ID of your Discord server (guild).");
            System.err.println("- DISCORD_WELCOME_CHANNEL_ID - The ID of the welcome channel.");
            System.err.println("- DISCORD_LOGS_CHANNEL_ID - The ID of the logs channel.");
            System.err.println("- DISCORD_CONFINER_IDS - The IDs of confiners, comma-deliminated.");
            System.err.println("- DISCORD_CONFINED_ROLE_ID - The ID of the confined role.");
            System.err.println("? DISCORD_AUTO_RELAUNCH(true) - Whether the bot should auto-relaunch on failure (true/false).");
            System.err.println("This may be done via. environment variables, JVM system properties (-D<name>=<value>, -DVAR_NAME=VALUE), or command line arguments (--<name>=<value>, --VAR_NAME=VALUE).");
            System.exit(1);
        }

        // Initialise and launch the bot
        BotBootstrapper bootstrapper = new BotBootstrapper();
        bootstrapper.init();
        bootstrapper.launch();

        // Ensure the bot is shutdown gracefully on JVM exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown signal received. Attempting graceful shutdown...");
            try {
                bootstrapper.shutdown();
                System.out.println("Bot shut down gracefully.");
            } catch (Exception e) {
                log.error("Error while shutting down bot.", e);
            }
        }));

        // Prevent main thread from exiting immediately
        synchronized (Main.class) {
            try {
                Main.class.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupt status
                System.err.println("Main thread was interrupted, exiting.");
                System.exit(1);
            }
        }
    }
}
