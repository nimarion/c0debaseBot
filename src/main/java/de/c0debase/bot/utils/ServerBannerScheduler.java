package de.c0debase.bot.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServerBannerScheduler {

    /**
     * Start the runnable for changing the server banner every 24 hours.
     *
     * @param runnable The {@link ServerBanner} (or {@link Runnable} of the
     *                 ServerBanner) which should call the update.
     */
    public void start(ServerBanner runnable) {
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(runnable, millisTillMidnight() / 1000,
                86400, TimeUnit.SECONDS);
    }

    /**
     * Calculate the remaining milliseconds till midnight.
     *
     * @return remaining time.
     */
    private long millisTillMidnight() {
        return LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli() - System.currentTimeMillis();
    }

}