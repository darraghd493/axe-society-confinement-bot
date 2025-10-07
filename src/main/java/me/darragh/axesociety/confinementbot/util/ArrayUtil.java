package me.darragh.axesociety.confinementbot.util;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for handling array operations.
 *
 * @author darraghd493
 * @since 1.0.0
 */
@UtilityClass
public class ArrayUtil {
    public static <T> boolean contains(T[] array, T value) {
        for (T t : array) {
            if (t.equals(value)) return true;
        }
        return false;
    }
}
