package me.darragh.axesociety.confinementbot.util;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for handling JDA options.
 *
 * @author darraghd493
 * @since 1.0.0
 */
@UtilityClass
public class OptionUtil {
    public @Nullable User getUserOption(OptionMapping option) {
        try {
            return option.getAsUser();
        } catch (Exception e) {
            return null;
        }
    }
}
