package de.c0debase.bot.listener.voice;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
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
        triggerTempchannel(event.getChannelJoined(), event.getMember());
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        super.onGuildVoiceMove(event);
        if (event.getChannelLeft().getMembers().contains(event.getGuild().getSelfMember()) && event.getChannelLeft().getMembers().size() == 1) {
            event.getGuild().getAudioManager().closeAudioConnection();
        }
        triggerTempchannel(event.getChannelLeft(), event.getMember());
        triggerTempchannel(event.getChannelJoined(), event.getMember());

    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        super.onGuildVoiceLeave(event);
        if (event.getGuild().getAudioManager().isConnected() && event.getGuild().getAudioManager().getConnectedChannel().getMembers().size() == 1) {
            event.getGuild().getAudioManager().closeAudioConnection();
        }
        triggerTempchannel(event.getChannelLeft(), event.getMember());
    }

    private void triggerTempchannel(VoiceChannel voiceChannel, Member member) {
        final TextChannel textChannel = member.getGuild().getTextChannelsByName("temp-" + voiceChannel.getName().toLowerCase(), true).isEmpty() ? null : member.getGuild().getTextChannelsByName("temp-" + voiceChannel.getName().toLowerCase(), true).get(0);
        if (textChannel == null) {
            return;
        }
        if (textChannel.getPermissionOverride(member) != null) {
            textChannel.getPermissionOverride(member).delete().complete();
        }
        if (voiceChannel.getMembers().contains(member)) {
            textChannel.createPermissionOverride(member).setAllow(Permission.MESSAGE_READ).queue();
        } else if (textChannel.getPermissionOverride(member) != null) {
            textChannel.getPermissionOverride(member).delete().queue();
        }
    }
}