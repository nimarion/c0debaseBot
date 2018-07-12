package de.c0debase.bot.commands.general;

import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Biosphere
 * @date 07.07.18
 */
public class ActivityCommand extends Command {

    public ActivityCommand() {
        super("activity", "", Category.GENERAL);
    }

    @Override
    public void execute(String[] args, Message msg) {
        CodebaseBot.getInstance().getMongoDataManager().getActivity(LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getYear(), msg.getGuild().getId(), activity -> {
            EmbedBuilder embedBuilder = getEmbed(msg.getGuild(), msg.getAuthor());
            embedBuilder.setTitle("Heutige Statistik");
            embedBuilder.addField("Aktive Nutzer", String.valueOf(activity.getUsers().size()), true);
            embedBuilder.addField("Nachrichten", String.valueOf(activity.getMessages()), true);
            msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
        });
    }
}
