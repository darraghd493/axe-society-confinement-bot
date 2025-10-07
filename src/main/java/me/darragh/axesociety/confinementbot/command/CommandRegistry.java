package me.darragh.axesociety.confinementbot.command;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import me.darragh.axesociety.confinementbot.command.impl.*;
import me.darragh.axesociety.confinementbot.util.LazyFinalReference;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Registers and manages bot commands.
 *
 * @author darraghd493
 * @since 1.0.0
 */
public class CommandRegistry {
    static final Map<String, Command> COMMANDS = new Object2ObjectOpenHashMap<>();
    static final LazyFinalReference<Collection<SlashCommandData>> SLASH_COMMAND_CACHE = new LazyFinalReference<>(
            () -> {
                Set<SlashCommandData> commands = new ObjectOpenHashSet<>();
                commands.addAll(CommandRegistry.getAllCommands().values().stream().map(Command::getCommandData).toList());
                return commands;
            }
    );

    static {
        registerCommand(new ConfineCommand());
        registerCommand(new ReleaseCommand());
        registerCommand(new CoinflipCommand());
        registerCommand(new PingCommand());
        registerCommand(new HelpCommand());
    }

    private static void registerCommand(Command command) {
        COMMANDS.put(command.getName(), command);
    }

    public static Map<String, Command> getAllCommands() {
        return COMMANDS;
    }

    public static Collection<SlashCommandData> getSlashCommands() {
        return SLASH_COMMAND_CACHE.get();
    }
}
