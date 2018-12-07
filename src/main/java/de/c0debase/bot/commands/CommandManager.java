package de.c0debase.bot.commands;

import de.c0debase.bot.core.Codebase;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Biosphere
 * @date 23.01.18
 */

public class CommandManager extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(CommandManager.class);

    private final Codebase bot;
    private List<Command> availableCommands;

    public CommandManager(final Codebase bot) {
        this.bot = bot;
        this.availableCommands = Collections.synchronizedList(new ArrayList<>());
        final Set<Class<? extends Command>> classes = new Reflections("de.c0debase.bot.commands").getSubTypesOf(Command.class);
        classes.forEach(cmdClass -> {
            try {
                registerCommands(cmdClass.newInstance());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        bot.getJDA().addEventListener(this);
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        if (event.isFromType(ChannelType.PRIVATE)) {
            return;
        }
        if (event.getChannel().getName() == "bot" || event.getMessage().getContentRaw().startsWith("!clear")) {
            final String[] arguments = event.getMessage().getContentRaw().split(" ");
            for (Command command : this.availableCommands) {
                if (command.getCategory().equals(Command.Category.STAFF) && !event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
                    continue;
                }
                if (("!" + command.getCommand()).equalsIgnoreCase(arguments[0])) {
                    command.execute(bot, Arrays.copyOfRange(arguments, 1, arguments.length), event.getMessage());
                } else {
                    for (String alias : command.getAliases()) {
                        if (("!" + alias).equalsIgnoreCase(arguments[0])) {
                            command.execute(bot, Arrays.copyOfRange(arguments, 1, arguments.length), event.getMessage());
                        }
                    }
                }
            }
        }
    }


    private void registerCommands(final Command command) {
        if (!availableCommands.contains(command)) {
            availableCommands.add(command);
            logger.info("Registered " + command.getCommand() + " Command");
        }
    }

    public List<Command> getAvailableCommands() {
        return Collections.unmodifiableList(availableCommands);
    }
}
