package de.c0debase.bot.commands;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import org.reflections.Reflections;

import java.util.*;

/**
 * @author Biosphere
 * @date 23.01.18
 */

public class CommandManager {

    private ArrayList<Command> availableCommands = new ArrayList<>();

    public CommandManager() {
        Set<Class<? extends Command>> classes = new Reflections("de.c0debase.bot.commands").getSubTypesOf(Command.class);
        classes.forEach(cmdClass -> {
            try {
                registerCommands(cmdClass.newInstance());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }


    public void execute(Message msg) {
        String[] arguments = msg.getContentRaw().split(" ");
        for (Command command : this.availableCommands) {
            if (command.getCategory().equals(Command.Category.STAFF) && !msg.getMember().hasPermission(Permission.BAN_MEMBERS)) {
                continue;
            }
            if (("!" + command.getCommand()).equalsIgnoreCase(arguments[0])) {
                command.execute(Arrays.copyOfRange(arguments, 1, arguments.length), msg);
            } else {
                for (String alias : command.getAliases()) {
                    if (("!" + alias).equalsIgnoreCase(arguments[0])) {
                        command.execute(Arrays.copyOfRange(arguments, 1, arguments.length), msg);
                    }
                }
            }
        }
    }

    private void registerCommands(Command command) {
        if (!availableCommands.contains(command)) {
            availableCommands.add(command);
        }
    }

    public List<Command> getAvailableCommands() {
        return Collections.unmodifiableList(availableCommands);
    }
}
