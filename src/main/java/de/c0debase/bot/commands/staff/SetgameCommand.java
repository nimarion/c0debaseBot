package de.c0debase.bot.commands.staff;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;

public class SetgameCommand extends Command {

    public SetgameCommand() {
        super("setgame", "Ändert den Status des Bots", Category.STAFF, "presence");
    }

    @Override
    public void execute(final String[] args, final Message message) {
        message.getJDA().getPresence().setActivity(Activity.playing(String.join(" ", args)));
    }
}
