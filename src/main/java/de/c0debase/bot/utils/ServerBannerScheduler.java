package de.c0debase.bot.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServerBannerScheduler {

    /**
     * Start the runnable for changing the server banner every 24 hours.
     *
     * @param runnable The {@link ServerBanner} (or {@link Runnable} of the ServerBanner) which should call the update.
     */
    public void start(ServerBanner runnable) {
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(runnable, 0, 24, TimeUnit.HOURS);
    }

}