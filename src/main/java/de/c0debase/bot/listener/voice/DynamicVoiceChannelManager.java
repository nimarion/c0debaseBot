package de.c0debase.bot.listener.voice;

import com.frequal.romannumerals.Converter;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
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
    public void onGuildVoiceJoin(final GuildVoiceJoinEvent event) {
        final VoiceChannel channel = event.getChannelJoined();
        if (!channel.getName().startsWith(channelName)) {
            return;
        }
        synchronized (this) {
            final List<VoiceChannel> voiceChannels = event.getGuild().getVoiceChannelCache().stream()
                    .filter(voiceChannel -> voiceChannel.getName().startsWith(channelName)).collect(Collectors.toList());
            final boolean freeVoiceChannels = voiceChannels.stream()
                    .anyMatch(voiceChannel -> voiceChannel.getMembers().size() == 0);
            if (!freeVoiceChannels) {
                final VoiceChannel lastChannel = voiceChannels.get(voiceChannels.size() - 1);
                final String name = lastChannel.getName();
                final String number = name.replaceFirst(channelName, "").trim();
                final String nextNumber;
                try {
                    nextNumber = getNextNumber(name);
                } catch (final ParseException exception) {
                    return; //if a channel matches the name but has no number at the end
                }
                event.getGuild().getController().createVoiceChannel(name.replace(number, nextNumber)).queue();
            }
        }
    }

    @Override
    public void onGuildVoiceLeave(final GuildVoiceLeaveEvent event) {
        final VoiceChannel channel = event.getChannelLeft();
        if (!channel.getName().startsWith(channelName)) {
            return;
        }
        synchronized (this) {
            final List<VoiceChannel> voiceChannels = event.getGuild().getVoiceChannelCache().stream()
                    .filter(voiceChannel -> voiceChannel.getName().startsWith(channelName)).collect(Collectors.toList());
            if (voiceChannels.stream().filter(voiceChannel -> voiceChannel.getMembers().size() == 0).count() > 1) {
                voiceChannels.get(voiceChannels.size() - 1).delete().queue();
            }
        }
    }

    private String getNextNumber(final String number) throws ParseException {
        final int currentChannelNumber = romanNumeralsConverter.toNumber(number);
        return romanNumeralsConverter.toRomanNumerals(currentChannelNumber + 1);
    }
}
