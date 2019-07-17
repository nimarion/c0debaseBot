package de.c0debase.bot.listener.guild;

import de.c0debase.bot.core.Codebase;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberLeaveListener extends ListenerAdapter {

    public GuildMemberLeaveListener(final Codebase bot) {
        bot.getJDA().addEventListener(this);
    }

    @Override
    public void onGuildMemberLeave(final GuildMemberLeaveEvent event) {
        event.getGuild().getTextChannelsByName("log", true).forEach(channel -> {
            final EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setFooter("@" + event.getMember().getUser().getName() + "#" + event.getMember().getUser().getDiscriminator(), event.getMember().getUser().getEffectiveAvatarUrl());
            embedBuilder.appendDescription(event.getMember().getEffectiveName() + " hat c0debase verlassen");
            channel.sendMessage(embedBuilder.build()).queue();
        });
    }

}
