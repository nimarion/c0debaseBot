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
        final EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Minesweeper");
        builder.setDescription(board.toSpoiler());
        builder.setFooter("Code von NurMarvin#1337", null);

        message.getChannel().sendMessage(builder.build()).queue();
    }
}
