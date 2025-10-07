package me.darragh.axesociety.confinementbot;

import lombok.experimental.UtilityClass;

import java.util.Arrays;

/**
 * Configuration class for the Confinement Bot.
 *
 * @author darraghd493
 * @since 1.0.0
 */
@UtilityClass
public class BotConfig {
    private static String getVar(String name) {
        String value = System.getProperty(name); // -Dname=value or System.setProperty
        if (value == null) {
            value = System.getenv(name); // environment variable as fallback
        }
        return value;
    }

    @SuppressWarnings("SameParameterValue") // may be used more in future
    private static String getVar(String name, String defaultValue) {
        String value = getVar(name);
        return value != null ? value : defaultValue;
    }

    public static final String BOT_TOKEN = getVar("DISCORD_BOT_TOKEN");
    public static final String GUILD_ID = getVar("DISCORD_GUILD_ID");
    public static final String GUILD_NAME = getVar("DISCORD_GUILD_NAME", "Axe Society");
    public static final String WELCOME_CHANNEL_ID = getVar("DISCORD_WELCOME_CHANNEL_ID");
    public static final String LOGS_CHANNEL_ID = getVar("DISCORD_LOGS_CHANNEL_ID");
    public static final String[] CONFINER_IDS; // initialised in static block
    public static final String CONFINED_ROLE_ID = getVar("DISCORD_CONFINED_ROLE_ID");
    public static final String CONFINED_CHANNEL_ID = getVar("DISCORD_CONFINED_CHANNEL_ID");
    public static final boolean AUTO_RELAUNCH = Boolean.parseBoolean(getVar("DISCORD_AUTO_RELAUNCH", "true"));

    static {
        String csv = getVar("DISCORD_CONFINER_IDS");
        if (csv == null || csv.isBlank()) {
            CONFINER_IDS = new String[0];
        } else {
            CONFINER_IDS = Arrays.stream(csv.split(","))
                    .map(String::trim)
                    .toArray(String[]::new);
        }
    }

    public static void init() {
        // dummy method, used to force static block execution
    }

    public static boolean validate() {
        return BOT_TOKEN != null && GUILD_ID != null && WELCOME_CHANNEL_ID != null && LOGS_CHANNEL_ID != null && CONFINER_IDS.length != 0 && CONFINED_ROLE_ID != null && CONFINED_CHANNEL_ID != null;
    }
}
