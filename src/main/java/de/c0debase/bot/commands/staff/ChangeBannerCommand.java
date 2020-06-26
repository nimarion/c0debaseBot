package de.c0debase.bot.commands.staff;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.Codebase;
import de.c0debase.bot.utils.ServerBanner;
import de.c0debase.bot.utils.ServerBannerScheduler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.awt.*;

public class ChangeBannerCommand extends Command {

    // Somehow a bad workaround, I don't have any way to include them other than to extend the interface (which I didn't necessarily want)
    // or to create a static singleton instance ._.
    private ServerBanner serverBanner;

    public ChangeBannerCommand() {
        super("banner", "Force an update for the banner change", Category.STAFF);
    }

    @Override
    public void execute(String[] args, Message message) {
        if (this.serverBanner == null) return;
        if (!bot.getGuild().getFeatures().contains("BANNER")) {
            message.getTextChannel().sendMessage(
                    new EmbedBuilder()
                            .setDescription("Auf diesem Server kann aktuell kein Banner gesetzt werden!")
                            .setColor(Color.RED)
                            .build()
            ).queue();
            return;
        }
        this.serverBanner.run();
        message.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setTitle("Erledigt!")
                        .setDescription("Force-Update des Banners wurde ausgeführt!")
                        .setImage(message.getGuild().getBannerUrl())
                        .setColor(Color.GREEN)
                        .build()
        ).queue();
    }

    /**
     * Cannot initialize the {@link ServerBanner} in constructor, because {@link Command#getBot()} is null at this moment.
     * So we override the setInstance method to initialize the banner service here.
     *
     * @param instance {@link Codebase} instance.
     */
    @Override
    public void setInstance(Codebase instance) {
        super.setInstance(instance);
        this.serverBanner = new ServerBanner(getBot());
        new ServerBannerScheduler().start(this.serverBanner);
    }
}