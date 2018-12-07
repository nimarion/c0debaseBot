package de.c0debase.bot.commands.staff;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.core.Codebase;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class SetgameCommand extends Command {

    public SetgameCommand() {
        super("setgame", "Ändert den Status des Bots", Category.STAFF, "presence");
    }

    @Override
    public void execute(final Codebase bot, final String[] args, final Message message) {
        message.getJDA().getPresence().setGame(Game.of(Game.GameType.DEFAULT, String.join(" ", args)));
    }
}
