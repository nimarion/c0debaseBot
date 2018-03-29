package de.c0debase.bot.listener.message;

import com.vdurmont.emoji.EmojiManager;
import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.level.LevelUser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

/**
 * @author Biosphere
 * @date 23.01.18
 */

public class MessageReceiveListener extends ListenerAdapter {

    private static final Pattern VALID_HEX_CODE = Pattern.compile("#([0-9a-f]{3}|[0-9a-f]{6}|[0-9a-f]{8})");
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);
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

        if (event.getTextChannel().getName().equalsIgnoreCase("feedback")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.GREEN);
            embedBuilder.setFooter("@" + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), event.getAuthor().getEffectiveAvatarUrl());
            embedBuilder.setTitle("Feedback");
            embedBuilder.setDescription(event.getMessage().getContentDisplay());
            event.getMessage().delete().queue();
            event.getTextChannel().sendMessage(embedBuilder.build()).queue(success -> {
                success.addReaction(EmojiManager.getForAlias("thumbsup").getUnicode()).queue();
                success.addReaction(EmojiManager.getForAlias("thumbsdown").getUnicode()).queue();
            });
            return;
        }

        if (event.getMessage().getContentRaw().startsWith(event.getGuild().getSelfMember().getAsMention())) {
            event.getTextChannel().sendMessage("hi").queue();
            return;
        }

        if (event.getMessage().getTextChannel().getName().equalsIgnoreCase("bot") && event.getMessage().getContentRaw().startsWith("!")) {
            CodebaseBot.getInstance().getCommandManager().execute(event.getMessage());
        } else {
            LevelUser levelUser = CodebaseBot.getInstance().getLevelManager().getLevelUser(event.getAuthor().getId());
            float time = (System.currentTimeMillis() - levelUser.getLastMessage()) / 1000;
            if (time >= 50.0f || levelUser.getLastMessage() == 0L) {
                levelUser.setLastMessage(System.currentTimeMillis());
                if (levelUser.addXP(50)) {
                    EmbedBuilder levelUpEmbed = new EmbedBuilder();
                    levelUpEmbed.appendDescription(event.getAuthor().getAsMention() + " has leveled up to level " + levelUser.getLevel());
                    event.getTextChannel().sendMessage(levelUpEmbed.build()).queue();
                }
            }
        }
        if (VALID_HEX_CODE.matcher(event.getMessage().getContentRaw()).matches()) {
            previewColor(event.getMessage());
        }
    }

    private void previewColor(Message message) {
        message.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setTitle("Color-Code-Preview")
                        .setDescription("Farbcode Vorschau für: " + message.getContentRaw())
                        .setImage("https://dummyimage.com/250x250/" + message.getContentRaw().replace("#", "") + "/" + message.getContentRaw().replace("#", "") + ".png")
                        .setColor(hex2Rgb(message.getContentRaw())).build()
        ).queue(message1 -> {

        });
        return;
    }

    private Color hex2Rgb(String colorStr) {
        return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16), Integer.valueOf(colorStr.substring(5, 7), 16));
    }

}
