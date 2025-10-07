package me.darragh.axesociety.confinementbot.command.impl;

import lombok.extern.slf4j.Slf4j;
import me.darragh.axesociety.confinementbot.BotConfig;
import me.darragh.axesociety.confinementbot.command.Command;
import me.darragh.axesociety.confinementbot.util.ArrayUtil;
import me.darragh.axesociety.confinementbot.util.LazyFinalReference;
import me.darragh.axesociety.confinementbot.util.MemberUtil;
import me.darragh.axesociety.confinementbot.util.OptionUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import static me.darragh.axesociety.confinementbot.BotMessages.*;

/**
 * Confines a user to gambling (the confinement channel).
 *
 * @author darraghd493
 * @since 1.0.0
 */
@Slf4j
public class ConfineCommand extends Command {
    private static final LazyFinalReference<String> GUILD_CHECK_MESSAGE = new LazyFinalReference<>(
            () -> "This command can only be used in %s.".formatted(BotConfig.GUILD_NAME)
    );

    private static final String SUCCESS_MESSAGE = "%s has been confined to gambling! 🤑🫰⚠️";
    private static final String FAILURE_MESSAGE = "An error occurred while trying to confine the user to gambling.";

    public ConfineCommand() {
        super("confine", "Confines a user to gambling.");
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
        // TODO: high-level option object-based wrapper
        OptionMapping option = event.getOption("user");
        User user;
        if (option == null || option.getType() != OptionType.USER || (user = OptionUtil.getUserOption(option)) == null) {
            event.reply(INVALID_COMMAND_SPECIFIED_MESSAGE).setEphemeral(true).queue();
            return;
        }

        // Check if the command invoker has permission to confine
        if (!ArrayUtil.contains(BotConfig.CONFINER_IDS, event.getUser().getId())) {
            event.reply(INVALID_PERMISSIONS_MESSAGE).setEphemeral(true).queue();
            return;
        }

        // Check if the confined role exists in the guild
        Role confinedRole = guild.getRoleById(BotConfig.CONFINED_ROLE_ID);
        if (confinedRole == null) {
            event.reply(CONFINED_ROLE_MISSING_MESSAGE).setEphemeral(true).queue();
            log.warn("Confined role with ID {} does not exist in guild {}.", BotConfig.CONFINED_ROLE_ID, guild.getId());
            return;
        }

        // Check if the user is the command invoker
        if (user.getId().equals(event.getUser().getId())) {
            event.reply(CONFINED_SELF_ATTEMPT_MESSAGE).setEphemeral(true).queue();
            return;
        }

        // Check if the user is a bot
        if (user.isBot()) {
            event.reply(CONFINED_BOT_ATTEMPT_MESSAGE).setEphemeral(true).queue();
            return;
        }

        // Check if the member exists in the guild
        Member targetMember = MemberUtil.retrieveMember(guild, user);
        if (targetMember == null) {
            event.reply(INVALID_GUILD_MEMBER_MESSAGE).setEphemeral(true).queue();
            return;
        }

        // Check if the member is already confined
        if (targetMember.getRoles().stream().anyMatch(role -> role.getId().equals(BotConfig.CONFINED_ROLE_ID))) {
            event.reply(CONFINED_ALREADY_CONFINED_MESSAGE).setEphemeral(true).queue();
            return;
        }

        // Check if the command invoker can confine the member
        // TODO: Use something more accurate than canInteract (like role hierarchy comparison)
        if (!member.canInteract(targetMember)) {
            event.reply(CONFINED_USER_CANNOT_CONFINE_MESSAGE).setEphemeral(true).queue();
            return;
        }

        // Check if the bot can confine the member
        if (!guild.getSelfMember().canInteract(targetMember)) {
            event.reply(CONFINED_BOT_CANNOT_CONFINE_MESSAGE).setEphemeral(true).queue();
            return;
        }

        // Give the confined role to the member
        guild.addRoleToMember(targetMember, confinedRole).queue(
                success -> event.reply(SUCCESS_MESSAGE.formatted(user.getAsMention())).queue(),
                error -> {
                    event.reply(FAILURE_MESSAGE).setEphemeral(true).queue();
                    log.error("Failed to confine user {} (ID: {}) in guild {}.", user.getAsTag(), user.getId(), guild.getId(), error);
                }
        );
    }

    @Override
    public SlashCommandData getCommandData() {
        return super.getCommandData().addOption(OptionType.USER, "user", "The user to confine", true);
    }
}
