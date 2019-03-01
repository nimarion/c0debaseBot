package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.utils.Constants;
import net.dv8tion.jda.core.entities.Message;

public class SpongebobCommand extends Command {

    public SpongebobCommand() {
        super("spongebob", "Macht einen normalen Satz zu einem lustigen Spongebob Satz", Category.GENERAL);
    }

    @Override
    public void execute(String[] args, Message message) {
        if(args.length == 0){
            message.getChannel().sendMessage(
                    getEmbed(message.getGuild(), message.getAuthor()).setDescription("!spongebob [msg]").build())
                    .queue();
        } else {
            String spongebob = "";
            for(String splitted : String.join(" ", args).split("")){
                if(Constants.RANDOM.nextBoolean()){
                    spongebob+= splitted.toUpperCase();
                } else {
                    spongebob+= splitted.toLowerCase();
                }
            }
            message.getChannel().sendMessage(spongebob).queue();
        }
    }
}
