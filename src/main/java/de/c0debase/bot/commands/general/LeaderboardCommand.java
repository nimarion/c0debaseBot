package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.pagination.paginations.LevelLeaderboard;
import net.dv8tion.jda.api.entities.Message;

/**
 * @author Biosphere
 * @date 23.01.18
 */

public class LeaderboardCommand extends Command {

    public LeaderboardCommand() {
        super("leaderboard", "Zeigt dir das Leaderboard", Category.GENERAL);
    }

    @Override
    public void execute(final String[] args, final Message message) {
        boolean descending = true;
        if (args.length > 0)
            if (args[0].equalsIgnoreCase("asc") || args[0].equalsIgnoreCase("ascending") || args[0].equalsIgnoreCase("aufsteigend"))
                descending = false;
        bot.getPaginationManager().getPaginationByClass(LevelLeaderboard.class).createFirst(descending, message.getTextChannel());
    }
}
