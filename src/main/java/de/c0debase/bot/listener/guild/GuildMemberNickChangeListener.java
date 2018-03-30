package de.c0debase.bot.listener.guild;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.guild.member.GuildMemberNickChangeEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class GuildMemberNickChangeListener extends ListenerAdapter {

    @Override
    public void onGuildMemberNickChange(GuildMemberNickChangeEvent event) {
        event.getGuild().getTextChannelsByName("log", true).forEach(channel -> {
            EmbedBuilder logBuilder = new EmbedBuilder();
            logBuilder.setTitle("Nickname geändert");
            logBuilder.appendDescription("Neuer Nickname:" + event.getNewNick() + "\n");
            logBuilder.appendDescription("Alter Nickname: " + event.getPrevNick());
            logBuilder.setFooter("@" + event.getMember().getUser().getName() + "#" + event.getMember().getUser().getDiscriminator(), event.getMember().getUser().getEffectiveAvatarUrl());
            channel.sendMessage(logBuilder.build()).queue();
        });
    }

}
