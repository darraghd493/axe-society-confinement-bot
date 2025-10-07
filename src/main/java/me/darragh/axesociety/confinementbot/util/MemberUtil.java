package me.darragh.axesociety.confinementbot.util;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Utility class for handling member-related operations.
 *
 * @author darraghd493
 * @since 1.0.0
 */
@Slf4j
public class MemberUtil {
    public static Member retrieveMember(Guild guild, User user) {
        if (guild == null || user == null) {
            return null;
        }

        // Try and fetch the member from cache first
        Member cached = guild.getMember(user);
        if (cached != null) {
            return cached;
        }

        // Perform a blocking retrieval with a timeout
        try {
            return guild.retrieveMember(user)
                    .timeout(5, TimeUnit.SECONDS) // prevent hanging forever
                    .submit()
                    .get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error("Timeout while retrieving member {} in guild {}", user.getId(), guild.getId());
        } catch (Exception e) {
            log.error("Error while retrieving member {} in guild {}", user.getId(), guild.getId(), e);
        }
        return null;
    }
}
