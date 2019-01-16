package de.c0debase.bot.listener.message;

import com.vdurmont.emoji.EmojiManager;
import de.c0debase.bot.core.Codebase;
import de.c0debase.bot.database.data.CodebaseUser;
import de.c0debase.bot.utils.Constants;
import de.c0debase.bot.utils.StringUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.jodah.expiringmap.ExpiringMap;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MessageReceiveListener extends ListenerAdapter {

    private static final long PROJECT_ROLE_ID = 361603492642684929L;
    private static final long LOG_CHANNEL_ID = 389448715599085569L;

    private final Codebase bot;
    private final Map<Member, String> lastMessage;
    private final List<String> gifs = Arrays.asList(
            "https://media.giphy.com/media/5VKbvrjxpVJCM/giphy.gif",
            "https://media.giphy.com/media/4cUCFvwICarHq/giphy.gif",
            "https://media.giphy.com/media/1ym5LJ17vp77BL8X5O/giphy.gif",
            "https://media.giphy.com/media/KI9oNS4JBemyI/giphy.gif",
            "https://media.giphy.com/media/l1CC9FjH54QhYHExq/source.gif",
            "https://media.giphy.com/media/2gYhkl6mLIYZxpMve1/giphy.gif",
            "https://media.giphy.com/media/kmU72Ms75Zhlu/giphy.gif",
            "https://media.giphy.com/media/xHMIDAy1qkzNS/giphy.gif",
            "https://media.giphy.com/media/yJFeycRK2DB4c/giphy.gif",
            "https://media.giphy.com/media/cbb8zL5wbNnfq/giphy.gif",
            "https://media.giphy.com/media/aLdiZJmmx4OVW/giphy.gif",
            "https://media.giphy.com/media/qPcX2mzk3NmjC/giphy.gif",
            "https://media.giphy.com/media/kjCFOUT3ZIlAA/giphy.gif",
            "https://media.giphy.com/media/ZisaVxhbs1iDK/giphy.gif"
    );

    private static final long DISCUSSION_CHANNEL_ID = 361606003386613761L;

    public MessageReceiveListener(final Codebase bot) {
        this.bot = bot;
        final ExpiringMap.Builder<Object, Object> mapBuilder = ExpiringMap.builder();
        mapBuilder.expiration(30, TimeUnit.SECONDS).build();
        lastMessage = mapBuilder.build();
        bot.getJDA().addEventListener(this);
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        final Member member = event.getMember();
        final TextChannel channel = event.getChannel();

        if(StringUtils.containtsURL(event.getMessage().getContentRaw()) && member.getRoles().isEmpty()){
            final long creation = ChronoUnit.DAYS.between(event.getAuthor().getCreationTime(), LocalDateTime.now().atOffset(ZoneOffset.UTC));
            final boolean default_avatar =  event.getAuthor().getAvatarUrl() == null;
            if((creation < 7) && default_avatar){
                final EmbedBuilder logBuilder = new EmbedBuilder();
                logBuilder.setFooter("@" + member.getUser().getName() + "#" + member.getUser().getDiscriminator(), member.getUser().getEffectiveAvatarUrl());
                logBuilder.setThumbnail(member.getUser().getEffectiveAvatarUrl());
                logBuilder.appendDescription("Erstelldatum: " + member.getUser().getCreationTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + "\n");
                logBuilder.appendDescription("Nachricht: " + event.getMessage().getContentRaw() + "\n");
                event.getGuild().getTextChannelById(LOG_CHANNEL_ID).sendMessage(logBuilder.build()).queue();
                event.getMessage().delete().queue();
                return;
            }
        }

        if (channel.getTopic() != null && channel.getTopic().contains("\uD83D\uDCCC")) {
            createPoll(event.getMessage());
            return;
        }

        if (lastMessage.containsKey(member) && lastMessage.get(member).equalsIgnoreCase(event.getMessage().getContentRaw()) && event.getMessage().getAttachments().isEmpty()) {
            event.getMessage().delete().queue();
            return;
        }
        lastMessage.put(member, event.getMessage().getContentRaw());

        final CodebaseUser codebaseUser = bot.getDataManager().getUserData(event.getGuild().getId(), event.getAuthor().getId());
        final float time = (System.currentTimeMillis() - codebaseUser.getLastMessage()) / 1000;
        if (time >= 50.0f) {
            if (codebaseUser.addXP(50)) {
                final EmbedBuilder levelUpEmbed = new EmbedBuilder();
                final int newLevel = codebaseUser.getLevel();
                levelUpEmbed.appendDescription(event.getAuthor().getAsMention() + " ist nun Level " + newLevel);
                if(channel.getIdLong() != DISCUSSION_CHANNEL_ID){
                    levelUpEmbed.setImage(gifs.get(Constants.RANDOM.nextInt(gifs.size())));
                }

                channel.sendMessage(levelUpEmbed.build()).queue();
                if (newLevel == 3) {
                    event.getGuild().getController().addSingleRoleToMember(event.getMember(), event.getJDA().getRoleById(PROJECT_ROLE_ID)).queue();
                }
            }
            codebaseUser.setLastMessage(System.currentTimeMillis());
            bot.getDataManager().updateUserData(codebaseUser);
        }
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.RED);
        embedBuilder.appendDescription("Private Nachrichten sind deaktiviert");
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    private void createPoll(final Message message){
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setFooter("@" + message.getAuthor().getName() + "#" + message.getAuthor().getDiscriminator(), message.getAuthor().getEffectiveAvatarUrl());
        embedBuilder.setTitle("Poll");
        embedBuilder.setDescription(message.getContentDisplay());
        message.delete().queue();
        message.getChannel().sendMessage(embedBuilder.build()).queue(sentMessage -> {
            sentMessage.addReaction(EmojiManager.getForAlias("thumbsup").getUnicode()).queue();
            sentMessage.addReaction(EmojiManager.getForAlias("thumbsdown").getUnicode()).queue();
        });
    }

}
