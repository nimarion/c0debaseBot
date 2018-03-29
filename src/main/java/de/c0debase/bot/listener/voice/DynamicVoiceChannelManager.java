package de.c0debase.bot.listener.voice;

import com.frequal.romannumerals.Converter;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LGA1151
 */
public class DynamicVoiceChannelManager extends ListenerAdapter {
    private final String channelName;
    private final Converter romanNumeralsConverter;

    public DynamicVoiceChannelManager(final String channelName) {
        this.channelName = channelName;
        romanNumeralsConverter = new Converter();
    }

    @Override
    public void onGuildVoiceMove(final GuildVoiceMoveEvent event) {
        handleLeave(event.getChannelLeft());
        handleJoin(event.getChannelJoined());
    }

    @Override
    public void onGuildVoiceJoin(final GuildVoiceJoinEvent event) {
        handleJoin(event.getChannelJoined());
    }

    @Override
    public void onGuildVoiceLeave(final GuildVoiceLeaveEvent event) {
        handleLeave(event.getChannelLeft());
    }

    private void handleJoin(final VoiceChannel channel) {
        if (!channel.getName().startsWith(channelName)) {
            return;
        }
        synchronized (this) {
            final java.util.List<VoiceChannel> voiceChannels = channel.getGuild().getVoiceChannelCache().stream()
                    .filter(voiceChannel -> voiceChannel.getName().startsWith(channelName)).collect(
                            Collectors.toList());
            final boolean freeVoiceChannels = voiceChannels.stream()
                    .anyMatch(voiceChannel -> voiceChannel.getMembers().size() == 0);
            if (!freeVoiceChannels) {
                final VoiceChannel lastChannel = voiceChannels.get(voiceChannels.size() - 1);
                final String name = lastChannel.getName();
                final String number = parseNumber(name);
                final String nextNumber;
                try {
                    nextNumber = getNextNumber(number);
                } catch (final ParseException exception) {
                    return; //if a channel matches the name but has no number at the end
                }
                final int position = lastChannel.getPosition();
                lastChannel.getParent().createVoiceChannel(name.replaceFirst(number, nextNumber)).queue(
                        newChannel -> newChannel.getManager().setPosition(position).queue());
            }
        }
    }

    private void handleLeave(final VoiceChannel channel) {
        if (!channel.getName().startsWith(channelName)) {
            return;
        }
        synchronized (this) {
            final List<VoiceChannel> voiceChannels = channel.getGuild().getVoiceChannelCache().stream()
                    .filter(voiceChannel -> voiceChannel.getName().startsWith(channelName)).collect(Collectors.toList());
            final List<VoiceChannel> emptyChannels = voiceChannels.stream().filter(
                    voiceChannel -> voiceChannel.getMembers().size() == 0).collect(Collectors.toList());
            if (emptyChannels.size() > 1) {
                emptyChannels.get(emptyChannels.size() - 1).delete().queue();
            }
        }
    }

    private String parseNumber(final String channelName) {
        return channelName.replaceFirst(this.channelName, "").trim();
    }

    private String getNextNumber(final String number) throws ParseException {
        final int currentChannelNumber = romanNumeralsConverter.toNumber(number);
        return romanNumeralsConverter.toRomanNumerals(currentChannelNumber + 1);
    }
}
