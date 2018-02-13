package de.c0debase.bot.listener.guild;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class GuildMemberLeaveListener extends ListenerAdapter {

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        super.onGuildMemberLeave(event);
        event.getGuild().getTextChannelsByName("log", true).forEach(channel -> {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setFooter("@" + event.getMember().getUser().getName() + "#" + event.getMember().getUser().getDiscriminator(), event.getMember().getUser().getEffectiveAvatarUrl());
            embedBuilder.appendDescription(event.getMember().getEffectiveName() + " hat c0debase verlassen");
            channel.sendMessage(embedBuilder.build()).queue();
        });
    }

}
