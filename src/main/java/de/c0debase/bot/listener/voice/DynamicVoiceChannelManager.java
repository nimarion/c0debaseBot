package de.c0debase.bot.listener.voice;

import com.frequal.romannumerals.Converter;
import de.c0debase.bot.utils.StringUtils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author LGA1151 (https://github.com/LGA1151)
 */
public class DynamicVoiceChannelManager extends ListenerAdapter {

    private static final ExecutorService executorService;
    private static final ReentrantLock lock;
    private static final Converter romanNumeralsConverter;

    private final String channelName;

    static {
        executorService = Executors.newCachedThreadPool();
        lock = new ReentrantLock();
        romanNumeralsConverter = new Converter();
    }

    public DynamicVoiceChannelManager(final String channelName) {
        this.channelName = channelName;
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
                final Guild guild = channel.getGuild();
                if (!channel.getName().startsWith(channelName)) {
                    return;
                }
                final List<VoiceChannel> voiceChannels = guild.getVoiceChannelCache().stream()
                        .filter(voiceChannel -> voiceChannel.getName().startsWith(channelName)).collect(
                                Collectors.toList());
                final boolean freeVoiceChannels = voiceChannels.stream()
                        .anyMatch(voiceChannel -> voiceChannel.getMembers().isEmpty());

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
                    final Role publicRole = guild.getPublicRole();
                    final Channel createdChannel = parent.createVoiceChannel(
                            StringUtils.replaceLast(oldName, oldName.replaceFirst(this.channelName, "").trim(), romanNumerals))
                            .addPermissionOverride(publicRole, null, Collections.singleton(Permission.VIEW_CHANNEL))
                            .complete();
                    createdChannel.getManager().setPosition(finalPosition).complete();
                    createdChannel.getPermissionOverride(publicRole).getManager().clear(Permission.VIEW_CHANNEL).complete();
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
                final List<VoiceChannel> voiceChannels = channel.getGuild().getVoiceChannelCache().stream()
                        .filter(voiceChannel -> voiceChannel.getName().startsWith(channelName)).collect(Collectors.toList());
                final List<VoiceChannel> emptyChannels = voiceChannels.stream().filter(
                        voiceChannel -> voiceChannel.getMembers().isEmpty()).collect(Collectors.toList());
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
