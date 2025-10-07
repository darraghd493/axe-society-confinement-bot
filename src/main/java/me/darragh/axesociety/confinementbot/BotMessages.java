package me.darragh.axesociety.confinementbot;

/**
 * Stores commonly used bot messages.
 *
 * @author darraghd493
 * @since 1.0.0
 */
public class BotMessages {
    public static final String INVALID_PERMISSIONS_MESSAGE = "You do not have permission to use this command.";
    public static final String INVALID_COMMAND_SPECIFIED_MESSAGE = "Invalid command specified.";
    public static final String INVALID_GUILD_MEMBER_MESSAGE = "The specified user is not a member of this guild.";

    //region Confinement Messages
    public static final String CONFINED_ROLE_MISSING_MESSAGE = "The confined role does not exist in this guild.";
    public static final String CONFINED_SELF_ATTEMPT_MESSAGE = "You can't confine yourself! 🤦‍♂️";
    public static final String CONFINED_BOT_ATTEMPT_MESSAGE = "Bots cannot be confined.";
    public static final String CONFINED_ALREADY_CONFINED_MESSAGE = "This user is already confined.";
    public static final String CONFINED_USER_CANNOT_CONFINE_MESSAGE = "You cannot confine this user.";
    public static final String CONFINED_BOT_CANNOT_CONFINE_MESSAGE = "The bot cannot confine this user."; // imaginary restrictions - LOL!
    public static final String RELEASE_NOT_CONFINED_MESSAGE = "This user is not confined!";
    public static final String COINFLIP_COOLDOWN_MESSAGE = "You have already used this command 3 times in the last 24 hours. Please try again later.";
    //endregion
}
