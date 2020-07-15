package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.pagination.Pagination;
import de.c0debase.bot.pagination.paginations.SinceLeaderboard;
import net.dv8tion.jda.api.entities.Message;

public class SinceLeaderCommand extends Command {

    public SinceLeaderCommand() {
        super("sinceleader", "Listet die Discorduser anhand ihres Beitrittdatums zu diesem Discord.", Category.GENERAL,
                "sinceleaderboard");
    }

    @Override
    public void execute(String[] args, Message message) {
        bot.getPaginationManager().getPaginationByClass(SinceLeaderboard.class)
                .createFirst(Pagination.isDescending(args), message.getTextChannel());
    }

}
