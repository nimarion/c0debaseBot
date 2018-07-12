package de.c0debase.bot.listener.guild;

import de.c0debase.bot.CodebaseBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.time.LocalDateTime;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class GuildMemberLeaveListener extends ListenerAdapter {

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        event.getGuild().getTextChannelsByName("log", true).forEach(channel -> {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setFooter("@" + event.getMember().getUser().getName() + "#" + event.getMember().getUser().getDiscriminator(), event.getMember().getUser().getEffectiveAvatarUrl());
            embedBuilder.appendDescription(event.getMember().getEffectiveName() + " hat c0debase verlassen");
            channel.sendMessage(embedBuilder.build()).queue();
        });
        CodebaseBot.getInstance().getMongoDataManager().getActivity(LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getYear(), event.getGuild().getId(), activity -> {
            activity.setMembers(event.getGuild().getMembers().size());
            CodebaseBot.getInstance().getMongoDataManager().updateActivity(activity);
        });
    }

}
