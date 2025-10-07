package me.darragh.axesociety.confinementbot.command.impl;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import lombok.extern.slf4j.Slf4j;
import me.darragh.axesociety.confinementbot.BotConfig;
import me.darragh.axesociety.confinementbot.command.Command;
import me.darragh.axesociety.confinementbot.util.*;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.List;
import java.util.Map;

import static me.darragh.axesociety.confinementbot.BotMessages.*;

/**
 * Gambles a user's confinement with a coinflip.
 *
 * @author darraghd493
 * @since 1.0.0
 */
@Slf4j
public class CoinflipCommand extends Command {
    private static final LazyFinalReference<String> GUILD_CHECK_MESSAGE = new LazyFinalReference<>(
            () -> "This command can only be used in %s.".formatted(BotConfig.GUILD_NAME)
    );

    private static final String SUCCESS_MESSAGE = "🪙 The coin landed on %s. You guessed correct - you are FREE! 🥳🎉";
    private static final String FAILURE_MESSAGE = "🪙 The coin landed on %s. You guessed wrong, you remain confined. 😂🫵";
    private static final String ERROR_MESSAGE = "An error occurred while trying to release you."; // you - for regular members

    private static final Map<Long, List<Long>> COOLDOWN_MAP = new Long2ObjectOpenHashMap<>();

    public CoinflipCommand() {
        super("coinflip", "Gambles your confinement.");
    }

    @Override
    public void handleInteraction(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();

        if (guild == null || member == null || !guild.getId().equals(BotConfig.GUILD_ID)) {
            event.reply(GUILD_CHECK_MESSAGE.get()).setEphemeral(true).queue();
            return;
        }

        // Fetch options
        OptionMapping option = event.getOption("guess");
        if (option == null || option.getType() != OptionType.STRING) {
            event.reply(INVALID_COMMAND_SPECIFIED_MESSAGE).setEphemeral(true).queue();
            return;
        }

        // Check if the confined role exists in the guild
        Role confinedRole = guild.getRoleById(BotConfig.CONFINED_ROLE_ID);
        if (confinedRole == null) {
            event.reply(CONFINED_ROLE_MISSING_MESSAGE).setEphemeral(true).queue();
            log.warn("Confined role with ID {} does not exist in guild {}.", BotConfig.CONFINED_ROLE_ID, guild.getId());
            return;
        }

        // Check if the member is not confined
        if (member.getRoles().stream().noneMatch(role -> role.getId().equals(BotConfig.CONFINED_ROLE_ID))) {
            event.reply(RELEASE_NOT_CONFINED_MESSAGE).setEphemeral(true).queue();
            return;
        }

        // Check if the member has used the command 3x in the last 24 hours
        List<Long> usageTimestamps = COOLDOWN_MAP.getOrDefault(member.getIdLong(), new LongArrayList());
        long currentTime = System.currentTimeMillis();
        usageTimestamps.removeIf(timestamp -> currentTime - timestamp > 24 * 60 * 60 * 1000L);
        if (usageTimestamps.size() >= 3) {
            event.reply(COINFLIP_COOLDOWN_MESSAGE).setEphemeral(true).queue();
            return;
        }
        usageTimestamps.add(currentTime);
        COOLDOWN_MAP.put(member.getIdLong(), usageTimestamps);

        // Perform the coinflip
        String guess = option.getAsString();
        boolean result = Math.random() < 0.5D;
        String side = result ? "heads" : "tails";

        if (!guess.equalsIgnoreCase(side)) {
            event.reply(FAILURE_MESSAGE.formatted(side)).queue();
            return;
        }

        // Remove the confined role from the member
        User user = event.getUser();
        guild.removeRoleFromMember(member, confinedRole).queue(
                success -> {
                    event.reply(SUCCESS_MESSAGE.formatted(side)).queue();
                    LogUtil.enqueueLogMessage("User %s (ID: %s) has been released from confinement in guild %s by winning a coinflip (guessed %s, landed on %s).".formatted(user.getAsTag(), user.getId(), guild.getId(), guess, side), guild);
                },
                error -> {
                    event.reply(ERROR_MESSAGE).setEphemeral(true).queue();
                    log.error("Failed to confine user {} (ID: {}) in guild {}.", user.getAsTag(), user.getId(), guild.getId(), error);
                }
        );
    }

    @Override
    public SlashCommandData getCommandData() {
        return super.getCommandData().addOptions(
                new OptionData(OptionType.STRING, "guess", "Your guess: Heads or Tails", true)
                        .addChoice("Heads", "heads")
                        .addChoice("Tails", "tails")
        );
    }
}
