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
    }


}
