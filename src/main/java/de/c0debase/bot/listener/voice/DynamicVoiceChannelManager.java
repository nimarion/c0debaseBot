package de.c0debase.bot.listener.voice;

import com.frequal.romannumerals.Converter;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author LGA1151
 */
public class DynamicVoiceChannelManager extends ListenerAdapter {
    private final String channelName;
    private final ExecutorService executorService;
    private final ReentrantLock lock;
    private final Converter romanNumeralsConverter;

    public DynamicVoiceChannelManager(final String channelName) {
        this.channelName = channelName;
        executorService = Executors.newCachedThreadPool();
        lock = new ReentrantLock();
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
        executorService.execute(() -> {
            lock.lock();
            try {
                if (!channel.getName().startsWith(channelName)) {
                    return;
                }
                final java.util.List<VoiceChannel> voiceChannels = channel.getGuild().getVoiceChannelCache().stream()
                        .filter(voiceChannel -> voiceChannel.getName().startsWith(channelName)).collect(
                                Collectors.toList());
                final boolean freeVoiceChannels = voiceChannels.stream()
                        .anyMatch(voiceChannel -> voiceChannel.getMembers().size() == 0);

                String oldName = null;
                int numberToCreate = -1;
                int positionToMove = -1;
                Category parent = null;

                if (!freeVoiceChannels) {
                    for (int i = 0; i < voiceChannels.size(); i++) {
                        final VoiceChannel nextChannel = voiceChannels.get(i);
                        final int number;
                        try {
                            number = parseNumber(nextChannel.getName());
                        } catch (final ParseException ignored) {
                            break;
                        }
                        if (number != i + 1) //voice channel with this number missing
                        {
                            oldName = nextChannel.getName();
                            numberToCreate = i + 1;
                            positionToMove = nextChannel.getPosition() - 1;
                            parent = channel.getParent();
                            break;
                        }
                    }

                    if (numberToCreate == -1) {
                        final VoiceChannel lastChannel = voiceChannels.get(voiceChannels.size() - 1);
                        oldName = lastChannel.getName();
                        try {
                            numberToCreate = parseNumber(lastChannel.getName()) + 1;
                        } catch (final ParseException ignored) {
                            return;
                        }
                        positionToMove = lastChannel.getPosition();
                        parent = lastChannel.getParent();
                    }

                    final int finalPosition = positionToMove;
                    final String romanNumerals = romanNumeralsConverter.toRomanNumerals(numberToCreate);
                    parent.createVoiceChannel(oldName.replaceFirst(oldName.replaceFirst(this.channelName, "").trim(), romanNumerals))
                            .complete().getManager().setPosition(finalPosition).complete();
                }

            } finally {
                lock.unlock();
            }
        });
    }

    private void handleLeave(final VoiceChannel channel) {
        executorService.execute(() -> {
            lock.lock();
            try {
                if (!channel.getName().startsWith(channelName)) {
                    return;
                }
                final java.util.List<VoiceChannel> voiceChannels = channel.getGuild().getVoiceChannelCache().stream()
                        .filter(voiceChannel -> voiceChannel.getName().startsWith(channelName)).collect(Collectors.toList());
                final List<VoiceChannel> emptyChannels = voiceChannels.stream().filter(
                        voiceChannel -> voiceChannel.getMembers().size() == 0).collect(Collectors.toList());
                if (emptyChannels.size() > 1) {
                    emptyChannels.get(emptyChannels.size() - 1).delete().complete();

                }
            } finally {
                lock.unlock();
            }
        });
    }

    private int parseNumber(final String channelName) throws ParseException {
        return romanNumeralsConverter.toNumber(channelName.replaceFirst(this.channelName, "").trim());
    }
}
