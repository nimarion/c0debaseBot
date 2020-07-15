package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.pagination.Pagination;
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
        bot.getPaginationManager().getPaginationByClass(LevelLeaderboard.class)
                .createFirst(Pagination.isDescending(args), message.getTextChannel());
    }
}
