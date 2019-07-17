package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.utils.Constants;
import net.dv8tion.jda.api.entities.Message;

public class SpongebobCommand extends Command {

    public SpongebobCommand() {
        super("spongebob", "Macht einen normalen Satz zu einem lustigen Spongebob Satz", Category.GENERAL, "mock");
    }

    @Override
    public void execute(String[] args, Message message) {
        if(args.length == 0){
            message.getChannel().sendMessage(
                    getEmbed(message.getGuild(), message.getAuthor()).setDescription("!spongebob [msg]").build())
                    .queue();
        } else {
            StringBuilder builder = new StringBuilder();

            for(char c : String.join(" ", args).toCharArray()){
                if(Constants.RANDOM.nextBoolean()){
                    builder.append(Character.toUpperCase(c));
                } else {
                    builder.append(Character.toLowerCase(c));
                }
            }
            message.getChannel().sendMessage(builder.toString()).queue();
        }
    }
}
