package de.c0debase.bot.commands.general;

import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Biosphere
 * @date 19.02.18
 */
public class MessageCommand extends Command {

    public MessageCommand() {
        super("messages", "Zeigt die durchnittliche Anzahl von Nachrichten am Tag", Categorie.GENERAL);
    }

    @Override
    public void execute(String[] args, Message msg) {
        EmbedBuilder embedBuilder = getEmbed(msg.getGuild(), msg.getAuthor());
        try {
            ResultSet resultSet = CodebaseBot.getInstance().getMySQL().query("SELECT * FROM MessageMonitor;");
            List<Integer> messageCount = new ArrayList<>();
            while (resultSet.next()) {
                messageCount.add(resultSet.getInt("MESSAGES"));
            }
            int sum = 0;
            for (Integer count : messageCount) {
                sum += count;
            }
            int average = sum / messageCount.size();

            embedBuilder.setDescription("Am Tag werden durchschnittlich " + average + " Nachrichten gesendet");
        } catch (SQLException ex) {
            ex.printStackTrace();
            embedBuilder.setDescription("Der Durchschnitt konnte nicht berechnet werden");
        } finally {
            msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
        }

    }
}
