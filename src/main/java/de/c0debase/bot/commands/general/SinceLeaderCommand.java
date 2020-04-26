package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.pagination.paginations.SinceLeaderboard;
import net.dv8tion.jda.api.entities.Message;

public class SinceLeaderCommand extends Command {

    public SinceLeaderCommand() {
        super("sinceleader", "Listet die Discorduser anhand ihres Beitrittdatums zu diesem Discord.", Category.GENERAL, "sinceleaderboard");

    }

    @Override
    public void execute(String[] args, Message message) {
        boolean descending = true;
        if (args.length > 0)
            if (args[0].equalsIgnoreCase("asc") || args[0].equalsIgnoreCase("ascending") || args[0].equalsIgnoreCase("aufsteigend"))
                descending = false;
        bot.getPaginationManager().getPaginationByClass(SinceLeaderboard.class).createFirst(descending, message.getTextChannel());
    }

}
