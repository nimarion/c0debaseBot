package de.c0debase.bot.listener.voice;

import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * @author Biosphere
 * @date 23.01.18
 */

public class GuildVoiceListener extends ListenerAdapter {


    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        super.onGuildVoiceJoin(event);
        if (event.getGuild().getAudioManager().isConnected() && event.getGuild().getAudioManager().getConnectedChannel().getMembers().size() == 1) {
            event.getGuild().getAudioManager().closeAudioConnection();
        }
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        super.onGuildVoiceMove(event);
        if (event.getChannelLeft().getMembers().contains(event.getGuild().getSelfMember()) && event.getChannelLeft().getMembers().size() == 1) {
            event.getGuild().getAudioManager().closeAudioConnection();
        }
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        super.onGuildVoiceLeave(event);
        if (event.getGuild().getAudioManager().isConnected() && event.getGuild().getAudioManager().getConnectedChannel().getMembers().size() == 1) {
            event.getGuild().getAudioManager().closeAudioConnection();
        }
    }
}
