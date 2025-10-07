package me.darragh.axesociety.confinementbot;

import lombok.extern.slf4j.Slf4j;
import me.darragh.axesociety.confinementbot.command.CommandRegistry;
import me.darragh.axesociety.confinementbot.listener.CommandListener;
import me.darragh.axesociety.confinementbot.listener.JoinListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

/**
 * The main bot class.
 * <br/>
 * <b>Note:</b> There is no multi-server support.
 *
 * @author darraghd493
 * @since 1.0.0
 */
@Slf4j
public class Bot {
    private JDA jdaApi;

    /**
     * Initialises and prepares the bot.
     */
    public void init() {
        JDABuilder builder = JDABuilder.createDefault(BotConfig.BOT_TOKEN)
                .enableIntents(GatewayIntent.GUILD_MEMBERS);
        new CommandListener(builder).register();
        new JoinListener(builder).register();
        this.jdaApi = builder.build();
    }

    /**
     * Launches the bot instance.
     */
    public void launch() throws InterruptedException {
        this.jdaApi.awaitReady();
        log.info("Bot is now online!");

        // Update slash commands
        this.jdaApi.updateCommands().addCommands(CommandRegistry.getSlashCommands()).queue(
                success -> log.info("Slash commands have been updated."),
                error -> log.error("Failed to update slash commands.", error)
        );

        // Update presence
        this.jdaApi.getPresence().setActivity(Activity.watching("over %s".formatted(BotConfig.GUILD_NAME)));
    }

    /**
     * Requests a shutdown of the bot instance.
     */
    public void shutdown() throws InterruptedException {
        if (this.jdaApi != null) {
            this.jdaApi.awaitShutdown();
            log.info("Bot has been shut down.");
        }
    }
}
