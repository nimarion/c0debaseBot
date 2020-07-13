package de.c0debase.bot.commands;

import de.c0debase.bot.Codebase;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CommandManager extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(CommandManager.class);

    private static final long TEAM_ROLE_ID = 361603492642684929L;

    private final Set<Command> availableCommands;

    public CommandManager(final Codebase bot) {
        this.availableCommands = new HashSet<>();
        final Set<Class<? extends Command>> classes = new Reflections("de.c0debase.bot.commands")
                .getSubTypesOf(Command.class);
        for (Class<? extends Command> cmdClass : classes) {
            try {
                final Command command = cmdClass.getDeclaredConstructor().newInstance();
                command.setInstance(bot);
                if (availableCommands.add(command)) {
                    logger.info("Registered " + command.getCommand() + " Command");
                }
            } catch (Exception exception) {
                logger.error("Error while registering Command!", exception);
            }
        }
        bot.getJDA().addEventListener(this);
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        if (!event.isFromType(ChannelType.TEXT)) {
            return;
        }
        final String content = event.getMessage().getContentRaw();

        if (content.startsWith("!") && (event.getChannel().getName().equals("bot") || content.startsWith("!clear")
                || content.startsWith("!ask") || content.startsWith("!tag"))) {
            final String[] arguments = content.split(" ");
            final String input = arguments[0].replaceFirst("!", "");
            for (Command command : this.availableCommands) {
                if (command.getCategory().equals(Command.Category.STAFF)
                        && !event.getMember().getRoles().contains(event.getJDA().getRoleById(TEAM_ROLE_ID))) {
                    continue;
                }

                if ((command.getCommand()).equalsIgnoreCase(input)) {
                    command.execute(Arrays.copyOfRange(arguments, 1, arguments.length), event.getMessage());
                } else {
                    for (String alias : command.getAliases()) {
                        if (alias.equalsIgnoreCase(input)) {
                            command.execute(Arrays.copyOfRange(arguments, 1, arguments.length), event.getMessage());
                        }
                    }
                }
            }
        }
    }

    public Set<Command> getAvailableCommands() {
        return Collections.unmodifiableSet(availableCommands);
    }
}
