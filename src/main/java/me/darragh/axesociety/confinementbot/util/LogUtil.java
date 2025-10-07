package me.darragh.axesociety.confinementbot.util;

import lombok.extern.slf4j.Slf4j;
import me.darragh.axesociety.confinementbot.BotConfig;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

/**
 * Utility class for verbose logging in Discord.
 *
 * @author darraghd493
 * @since 1.0.0
 */
@Slf4j
public class LogUtil {
    public static void enqueueLogMessage(String message, Guild guild) {
        if (guild == null) {
            log.error("Guild is null, cannot send log message.");
            return;
        }
        TextChannel channel = guild.getTextChannelById(BotConfig.LOGS_CHANNEL_ID);
        if (channel == null) {
            log.error("Logs channel is null, cannot send log message.");
            return;
        }
        channel.sendMessage(formatLogMessage(message)).queue(
                success -> {},
                error -> log.error("Failed to send log message to Discord.", error)
        );
    }

    public static String formatLogMessage(String message) {
        return "```" + message + "```";
    }
}
