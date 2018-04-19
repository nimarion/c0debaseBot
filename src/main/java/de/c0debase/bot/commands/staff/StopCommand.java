package de.c0debase.bot.commands.staff;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class StopCommand extends Command {

    public StopCommand() {
        super("stop", "Stoppt den Bot", Categorie.STAFF);
    }

    @Override
    public void execute(String[] args, Message msg) {
            msg.getJDA().shutdownNow();
            Runtime.getRuntime().exit(0);
    }
}
