package de.c0debase.bot.commands.general;

import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.sql.Connection;
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
        try (final Connection connection = CodebaseBot.getInstance().getMySQL().getConnection()) {
            ResultSet resultSet = connection.prepareStatement("SELECT * FROM MessageMonitor;").executeQuery();
            List<Integer> messageCount = new ArrayList<>();
            while (resultSet.next()) {
                messageCount.add(resultSet.getInt("MESSAGES"));
            }
            int sum = 0;
            for (Integer count : messageCount) {
                sum += count;
            }
            embedBuilder.setDescription("Am Tag werden durchschnittlich " + sum / messageCount.size() + " Nachrichten gesendet");
            msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
