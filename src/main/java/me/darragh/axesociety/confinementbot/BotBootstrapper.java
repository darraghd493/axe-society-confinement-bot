package me.darragh.axesociety.confinementbot;

import lombok.extern.slf4j.Slf4j;
import me.darragh.axesociety.confinementbot.util.FinalReference;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Bootstraps the bot application.
 *
 * @author darraghd493
 * @since 1.0.0
 */
@Slf4j
public class BotBootstrapper {
    private final FinalReference<Bot> BOT_REFERENCE = new FinalReference<>();
    private final Executor EXECUTOR = Executors.newSingleThreadExecutor();

    public void init() {
        Bot bot = BOT_REFERENCE.get();
        if (bot != null) {
            throw new IllegalStateException("Bot has already been initialised.");
        }
        bot = new Bot();
        BOT_REFERENCE.set(bot);
        BOT_REFERENCE.get().init();
    }

    public void launch() {
        EXECUTOR.execute(() -> {
            Bot bot = BOT_REFERENCE.get();
            if (bot == null) {
                throw new IllegalStateException("Bot has not been initialised. Call init() first.");
            }
            try {
                bot.launch();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Bot launch was interrupted.", e);
                if (BotConfig.AUTO_RELAUNCH) {
                    log.warn("Attempting to relaunch the bot...");
                    EXECUTOR.execute(this::launch);
                } else {
                    log.warn("Bot stopped due to interruption. Awaiting external restart or shutdown.");
                }
            }
        });
    }

    public void shutdown() {
        Bot bot = BOT_REFERENCE.get();
        if (bot == null) {
            throw new IllegalStateException("Bot has not been initialised. Call init() first.");
        }
        try {
            bot.shutdown();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt status
            log.error("Bot shutdown was interrupted.", e);
        }
    }
}
