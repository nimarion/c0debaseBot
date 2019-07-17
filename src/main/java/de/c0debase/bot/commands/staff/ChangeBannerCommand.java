package de.c0debase.bot.commands.staff;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.utils.ServerBanner;
import de.c0debase.bot.utils.ServerBannerScheduler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.awt.*;

public class ChangeBannerCommand extends Command {

    // Somehow a bad workaround, I don't have any way to include them other than to extend the interface (which I didn't necessarily want)
    // or to create a static singleton instance ._.
    private final ServerBanner serverBanner;

    public ChangeBannerCommand() {
        super("banner", "Force an update for the banner change", Category.STAFF);
        this.serverBanner = new ServerBanner(getBot().getJDA().getGuilds().get(0));
        new ServerBannerScheduler().start(this.serverBanner);
    }

    @Override
    public void execute(String[] args, Message message) {
        this.serverBanner.run();
        message.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setTitle("Erledigt!")
                        .setDescription("Force-Update des Banners wurde ausgeführt!")
                        .setColor(Color.GREEN)
                        .build()
        ).queue();
    }

}