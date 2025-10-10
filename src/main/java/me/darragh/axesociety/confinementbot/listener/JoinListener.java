package me.darragh.axesociety.confinementbot.listener;

import lombok.extern.slf4j.Slf4j;
import me.darragh.axesociety.confinementbot.BotConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.internal.requests.restaction.MessageCreateActionImpl;

/**
 * Listens for join events and handles them.
 * <br/>
 * <ul>
 *     <li>Prevents the bot from joining unsupported servers</li>
 *     <li>Welcomes users to the server</li>
 * </ul>
 *
 * @author darraghd493
 * @since 1.0.0
 */
@Slf4j
public class JoinListener extends Listener {
    private static final String WELCOME_TITLE = ":wave: %s, welcome to Axe Society!";
    private static final String WELCOME_DESCRIPTION = "Enjoy your stay! Please read the rules and have fun!";
    private static final String WELCOME_GIF = "https://cdn.discordapp.com/attachments/1400147568402628743/1424352311613853716/attachment.gif";

    public JoinListener(JDABuilder builder) {
        super(builder);
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        if (!event.getGuild().getId().equals(BotConfig.GUILD_ID)) {
            event.getGuild().leave().queue();
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        TextChannel channel = event.getGuild().getTextChannelById(BotConfig.WELCOME_CHANNEL_ID);
        if (channel == null) {
            log.error("Welcome channel (id: {}) not found!", BotConfig.WELCOME_CHANNEL_ID);
            return;
        }

        User user = event.getUser();
        Member member = event.getMember();

        MessageEmbed embed = new EmbedBuilder()
                .setTitle(WELCOME_TITLE.formatted(user.getEffectiveName()))
                .setDescription(WELCOME_DESCRIPTION)
                .setImage(WELCOME_GIF)
                .setAuthor(user.getAsTag(), null, user.getEffectiveAvatarUrl())
                .setTimestamp(member.getTimeJoined())
                .setColor(0x3498db)
                .build();

        MessageCreateAction action = new MessageCreateActionImpl(channel);
        action.setContent(user.getAsMention());
        action.setEmbeds(embed);
        action.queue();
    }
}
