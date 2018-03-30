package de.c0debase.bot.listener.voice;

import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.tempchannel.Tempchannel;
import net.dv8tion.jda.core.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.core.events.channel.voice.VoiceChannelDeleteEvent;
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
        if (event.getGuild().getAudioManager().isConnected() && event.getGuild().getAudioManager().getConnectedChannel().getMembers().size() == 1) {
            event.getGuild().getAudioManager().closeAudioConnection();
        }
        CodebaseBot.getInstance().getTempchannels().get(event.getChannelJoined().getId()).onTempchannelJoin(event.getChannelJoined(), event.getMember());
    }


    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        if (event.getChannelLeft().getMembers().contains(event.getGuild().getSelfMember()) && event.getChannelLeft().getMembers().size() == 1) {
            event.getGuild().getAudioManager().closeAudioConnection();
        }
        CodebaseBot.getInstance().getTempchannels().get(event.getChannelJoined().getId()).onTempchannelJoin(event.getChannelJoined(), event.getMember());
        CodebaseBot.getInstance().getTempchannels().get(event.getChannelLeft().getId()).onTempchannelLeave(event.getChannelLeft(), event.getMember());
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        if (event.getGuild().getAudioManager().isConnected() && event.getGuild().getAudioManager().getConnectedChannel().getMembers().size() == 1) {
            event.getGuild().getAudioManager().closeAudioConnection();
        }
        CodebaseBot.getInstance().getTempchannels().get(event.getChannelLeft().getId()).onTempchannelLeave(event.getChannelLeft(), event.getMember());
    }

    @Override
    public void onVoiceChannelCreate(VoiceChannelCreateEvent event) {
        super.onVoiceChannelCreate(event);
        CodebaseBot.getInstance().getTempchannels().put(event.getChannel().getId(), new Tempchannel());
    }

    @Override
    public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
        super.onVoiceChannelDelete(event);
        CodebaseBot.getInstance().getTempchannels().remove(event.getChannel().getId());
    }
}