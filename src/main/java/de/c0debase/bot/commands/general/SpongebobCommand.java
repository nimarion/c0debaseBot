package de.c0debase.bot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.c0debase.bot.utils.Constants;
import de.c0debase.bot.utils.EmbedUtils;

public class SpongebobCommand extends Command {

    public SpongebobCommand() {
        this.name = "spongebob";
        this.help = "Macht einen normalen Satz zu einem lustigen Spongebob Satz";
        this.aliases = new String[]{"mock"};
        this.arguments = "[message]";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (commandEvent.getArgs().isEmpty()) {
            commandEvent.reply(EmbedUtils.getEmbed(commandEvent.getAuthor(), false).setDescription("!spongebob [msg]").build());
        } else {
            StringBuilder builder = new StringBuilder();

            for (char c : commandEvent.getArgs().toCharArray()) {
                if (Constants.RANDOM.nextBoolean()) {
                    builder.append(Character.toUpperCase(c));
                } else {
                    builder.append(Character.toLowerCase(c));
                }
            }
            commandEvent.reply(builder.toString());
        }
    }
}
