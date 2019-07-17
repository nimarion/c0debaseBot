package de.c0debase.bot.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Icon;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ServerBanner implements Runnable {

    private final Guild guild;
    private URL unsplashUrl;

    public ServerBanner(final Guild guild) {
        this(guild, 335434);
    }

    public ServerBanner(final Guild guild, final int collectionId) {
        this.guild = guild;
        try {
            this.unsplashUrl = new URL("https://source.unsplash.com/collection/" + collectionId + "/960x540");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates / replaces the current server banner.
     *
     * @param icon The new server banner.
     */
    public void setServerBanner(final Icon icon) {
        if (!guild.getFeatures().contains("BANNER")) {
            throw new UnsupportedOperationException("Your guild can not have a banner!");
        }
        if (icon == null)
            return;
        guild.getManager().setBanner(icon).queue();
    }

    /**
     * Get the banner url of the provided guild.
     *
     * @return The banner url, if present. Otherwise null.
     */
    public String getBannerUrl() {
        if (!guild.getFeatures().contains("BANNER")) {
            throw new UnsupportedOperationException("Your guild can not have a banner!");
        }
        return guild.getBannerUrl();
    }

    /**
     * Get a random image from the provided (or default / fallback) collection (id).
     *
     * @return Image as {@link Icon} or null, if the requests failed.
     */
    private Icon getRandomUnsplashPicture() {
        try {
            final HttpsURLConnection httpsURLConnection = (HttpsURLConnection) this.unsplashUrl.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            try (final InputStream inputStream = httpsURLConnection.getInputStream()) {
                // something went wrong
                if (inputStream == null) {
                    return null;
                }
                return Icon.from(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method is called by {@link ServerBannerScheduler#start(ServerBanner)} to frequently update the guilds banner.
     */
    @Override
    public void run() {
        if (this.unsplashUrl == null) return;
        this.setServerBanner(getRandomUnsplashPicture());
    }
}