package de.c0debase.bot.listener.guild;

import de.c0debase.bot.core.Codebase;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.guild.member.GuildMemberNickChangeEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class GuildMemberNickChangeListener extends ListenerAdapter {

    public GuildMemberNickChangeListener(final Codebase bot) {
        bot.getJDA().addEventListener(this);
    }

    @Override
    public void onGuildMemberNickChange(final GuildMemberNickChangeEvent event) {
        event.getGuild().getTextChannelsByName("log", true).forEach(channel -> {
            final EmbedBuilder logBuilder = new EmbedBuilder();
            logBuilder.setTitle("Nickname geändert");
            logBuilder.appendDescription("Neuer Nickname:" + event.getNewNick() + "\n");
            logBuilder.appendDescription("Alter Nickname: " + event.getPrevNick());
            logBuilder.setFooter("@" + event.getMember().getUser().getName() + "#" + event.getMember().getUser().getDiscriminator(), event.getMember().getUser().getEffectiveAvatarUrl());
            channel.sendMessage(logBuilder.build()).queue();
        });
    }

}
