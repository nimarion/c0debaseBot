package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.utils.minesweeper.Board;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

public class MinesweeperCommand extends Command {

    public MinesweeperCommand() {
        super("minesweeper", "Generiert ein Minesweeper-Feld", Category.GENERAL);
    }

    @Override
    public void execute(final String[] args, final Message message) {
        final Board board = new Board();
        final StringBuilder builder = new StringBuilder();

        builder.append("**Minesweeper**\n");
        builder.append(board.toSpoiler()).append("\n");
        builder.append("_Code von NurMarvin#1337_");

        message.getChannel().sendMessage(builder.toString()).queue();
    }
}
