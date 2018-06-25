package de.c0debase.bot.listener.message;

import ai.api.AIServiceException;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import com.vdurmont.emoji.EmojiManager;
import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.utils.Constants;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.jodah.expiringmap.ExpiringMap;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Biosphere
 * @date 23.01.18
 */

public class MessageReceiveListener extends ListenerAdapter {

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

    public MessageReceiveListener(){
        final ExpiringMap.Builder<Object, Object> mapBuilder = ExpiringMap.builder();
        mapBuilder.expiration(30, TimeUnit.SECONDS).build();
        lastMessage = mapBuilder.build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        if (event.isFromType(ChannelType.PRIVATE)) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.RED);
            embedBuilder.appendDescription("Private Nachrichten sind deaktiviert");
            event.getPrivateChannel().sendMessage(embedBuilder.build()).queue();
            return;
        }

        if (event.getMessage().getContentRaw().startsWith("!clear")) {
            CodebaseBot.getInstance().getCommandManager().execute(event.getMessage());
            return;
        }

        if (event.getTextChannel().getName().equalsIgnoreCase("projekte")) {
            event.getMessage().addReaction(EmojiManager.getForAlias("thumbsup").getUnicode()).queue();
            event.getMessage().addReaction(EmojiManager.getForAlias("thumbsdown").getUnicode()).queue();
            return;
        }

        if (event.getTextChannel().getTopic() != null && event.getTextChannel().getTopic().contains("\uD83D\uDCCC")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.GREEN);
            embedBuilder.setFooter("@" + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), event.getAuthor().getEffectiveAvatarUrl());
            embedBuilder.setTitle("Poll");
            embedBuilder.setDescription(event.getMessage().getContentDisplay());
            event.getMessage().delete().queue();
            event.getTextChannel().sendMessage(embedBuilder.build()).queue(success -> {
                success.addReaction(EmojiManager.getForAlias("thumbsup").getUnicode()).queue();
                success.addReaction(EmojiManager.getForAlias("thumbsdown").getUnicode()).queue();
            });
            return;
        }

        if (event.getMessage().getContentRaw().startsWith(event.getGuild().getSelfMember().getAsMention()) && CodebaseBot.getInstance().getAiDataService() != null) {
            try {
                AIRequest request = new AIRequest(event.getMessage().getContentRaw().replace(event.getGuild().getSelfMember().getAsMention(), ""));
                AIResponse response = CodebaseBot.getInstance().getAiDataService().request(request);

                if (response.getStatus().getCode() == 200) {
                    if (response.getResult().getFulfillment().getSpeech().trim().isEmpty()) {
                        event.getChannel().sendMessage("Leider kann ich nicht verstehen, was du von mir möchtest.").queue();
                    } else {
                        event.getChannel().sendMessage(response.getResult().getFulfillment().getSpeech().replace("@everyone", "@ everyone").replace("@here", "@ here")).queue();
                    }
                }
            } catch (AIServiceException e) {
                e.printStackTrace();
            }
        }

        if (event.getMessage().getTextChannel().getName().equalsIgnoreCase("bot") && event.getMessage().getContentRaw().startsWith("!")) {
            CodebaseBot.getInstance().getCommandManager().execute(event.getMessage());
        } else {
            if(lastMessage.containsKey(event.getMember()) && lastMessage.get(event.getMember()).equalsIgnoreCase(event.getMessage().getContentRaw())){
                event.getMessage().delete().queue();
                return;
            } else {
                lastMessage.put(event.getMember(), event.getMessage().getContentRaw());
            }
            CodebaseBot.getInstance().getMongoDataManager().getLevelUser(event.getGuild().getId(), event.getAuthor().getId(), levelUser -> {
                float time = (System.currentTimeMillis() - levelUser.getLastMessage()) / 1000;
                if (time >= 50.0f) {
                    if (levelUser.addXP(50)) {
                        EmbedBuilder levelUpEmbed = new EmbedBuilder();
                        levelUpEmbed.appendDescription(event.getAuthor().getAsMention() + " ist nun Level " + levelUser.getLevel());
                        levelUpEmbed.setImage(gifs.get(Constants.RANDOM.nextInt(gifs.size())));
                        event.getTextChannel().sendMessage(levelUpEmbed.build()).queue();
                    }
                    levelUser.setLastMessage(System.currentTimeMillis());
                    CodebaseBot.getInstance().getMongoDataManager().updateLevelUser(levelUser);
                }
            });
        }
    }

}
