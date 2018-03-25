package de.c0debase.bot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private boolean repeat;

    /**
     * Constructs a new TrackScheduler.
     *
     * @param player Audio Player for the guild
     */
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.repeat = false;
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.add(track);
        }
    }

    /**
     * Start the provided track now.
     *
     * @param track The AudioTrack to play
     */
    public void playNow(AudioTrack track) {
        player.startTrack(track, false);
    }

    public void stop() {
        player.stopTrack();
        queue.clear();
    }

    public void clear() {
        queue.clear();
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean value) {
        this.repeat = value;
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    /**
     * Start the next track.
     *
     * @param noInterrupt Whether to only start if nothing else is playing
     */
    public void nextTrack(boolean noInterrupt) {
        player.startTrack(queue.poll(), noInterrupt);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            if (repeat) {
                player.startTrack(track.makeClone(), false);
            } else {
                nextTrack(false);
            }
        }
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        nextTrack(false);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        super.onTrackStart(player, track);
    }
}
