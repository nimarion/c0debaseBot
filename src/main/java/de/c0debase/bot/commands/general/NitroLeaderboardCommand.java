package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.pagination.Pagination;
import de.c0debase.bot.pagination.paginations.NitroLeaderboard;
import net.dv8tion.jda.api.entities.Message;

public class NitroLeaderboardCommand extends Command {

    public NitroLeaderboardCommand() {
        super("nitroleader", "Listet die Nitro Mitglieder anhand ihres Boost Datum.", Category.GENERAL,
                "nitroleaderboard", "nitro");
    }

    @Override
    public void execute(String[] args, Message message) {
        bot.getPaginationManager().getPaginationByClass(NitroLeaderboard.class)
                .createFirst(Pagination.isDescending(args), message.getTextChannel());
    }

}