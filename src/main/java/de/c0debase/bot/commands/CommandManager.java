package de.c0debase.bot.commands;

import com.jagrosh.jdautilities.command.*;
import de.c0debase.bot.core.Codebase;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Set;

public class CommandManager extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(CommandManager.class);

    public CommandManager(final Codebase bot) {
        final Set<Class<? extends Command>> classes = new Reflections("de.c0debase.bot.commands")
                .getSubTypesOf(Command.class);

        final CommandClientBuilder commandClientBuilder  = new CommandClientBuilder();
        commandClientBuilder.setPrefix("!");
        commandClientBuilder.setOwnerId("0");

        for (Class<? extends Command> cmdClass : classes) {
            try {
                final Constructor constructor = cmdClass.getDeclaredConstructors()[0];
                if (constructor.getParameterCount() == 1) {
                    commandClientBuilder.addCommand((Command) constructor.newInstance(bot));
                } else {
                    commandClientBuilder.addCommand((Command) constructor.newInstance());
                }
                logger.info("Registered " + cmdClass.getName());
            } catch (Exception exception) {
                logger.error("Error while registering Command!", exception);
            }
        }

        bot.getJDA().addEventListener(commandClientBuilder.build());
    }

}
