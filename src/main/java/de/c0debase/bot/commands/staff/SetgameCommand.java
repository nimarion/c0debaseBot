package de.c0debase.bot.commands.staff;

import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class SetgameCommand extends Command {

    public SetgameCommand() {
        super("setgame", "Ändert den Status des Bots", Categorie.STAFF, "presence");
    }

    @Override
    public void execute(String[] args, Message msg) {
        if (msg.getMember().hasPermission(Permission.ADMINISTRATOR) || msg.getMember().hasPermission(Permission.BAN_MEMBERS)) {
            msg.delete().queue();
            CodebaseBot.getInstance().getJda().getPresence().setGame(Game.of(Game.GameType.DEFAULT, String.join(" ", args)));
        }
    }
}
