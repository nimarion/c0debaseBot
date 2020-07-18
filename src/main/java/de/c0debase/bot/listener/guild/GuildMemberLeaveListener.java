package de.c0debase.bot.listener.guild;

import de.c0debase.bot.Codebase;
import de.c0debase.bot.utils.DiscordUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberLeaveListener extends ListenerAdapter {

    public GuildMemberLeaveListener(final Codebase bot) {
        bot.getJDA().addEventListener(this);
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        event.getGuild().getTextChannelsByName("log", true).forEach(channel -> {
            final EmbedBuilder embedBuilder = DiscordUtils.getDefaultEmbed(event.getMember());
            embedBuilder.appendDescription(event.getMember().getEffectiveName() + " hat c0debase verlassen");
            channel.sendMessage(embedBuilder.build()).queue();
        });
    }

}
