package de.c0debase.bot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.c0debase.bot.utils.minesweeper.Board;

public class MinesweeperCommand extends Command {

    public MinesweeperCommand() {
        this.name = "minesweeper";
        this.help = "Generiert ein Minesweeper-Feld";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        final Board board = new Board();
        final StringBuilder builder = new StringBuilder();

        builder.append("**Minesweeper**\n");
        builder.append(board.toSpoiler()).append("\n");
        builder.append("_Code von NurMarvin#1337_");

        commandEvent.reply(builder.toString());
    }
}
