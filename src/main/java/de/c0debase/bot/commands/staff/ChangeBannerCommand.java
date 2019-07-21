package de.c0debase.bot.commands.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.c0debase.bot.core.Codebase;
import de.c0debase.bot.utils.EmbedUtils;
import de.c0debase.bot.utils.ServerBanner;
import de.c0debase.bot.utils.ServerBannerScheduler;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class ChangeBannerCommand extends Command {

    // Somehow a bad workaround, I don't have any way to include them other than to extend the interface (which I didn't necessarily want)
    // or to create a static singleton instance ._.
    private ServerBanner serverBanner;

    public ChangeBannerCommand(final Codebase instance) {
        this.name = "banner";
        this.help = "Force an update for the banner change";
        this.requiredRole = "Team";
        this.guildOnly = true;
        this.serverBanner = new ServerBanner(instance);
        new ServerBannerScheduler().start(this.serverBanner);
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (!commandEvent.getGuild().getFeatures().contains("BANNER")) {
            final EmbedBuilder embedBuilder = EmbedUtils.getEmbed(commandEvent.getAuthor(), false);
            embedBuilder.setDescription("Auf diesem Server kann aktuell kein Banner gesetzt werden!");
            commandEvent.reply(embedBuilder.build());
            return;
        }
        if (this.serverBanner == null) return;
        this.serverBanner.run();
        final EmbedBuilder embedBuilder = EmbedUtils.getEmbed(commandEvent.getAuthor(), true);
        embedBuilder.setDescription("Force-Update des Banners wurde ausgeführt!");
        embedBuilder.setImage(commandEvent.getGuild().getBannerUrl());
        embedBuilder.setColor(Color.GREEN);
        commandEvent.reply(embedBuilder.build());
    }

}