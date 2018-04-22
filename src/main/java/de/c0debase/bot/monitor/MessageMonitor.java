package de.c0debase.bot.monitor;

import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.utils.Constants;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Biosphere
 * @date 14.02.18
 */
public class MessageMonitor extends Monitor {

    private final HashMap<String, Integer> messages = new HashMap<>();

    public MessageMonitor() {
        super("MessageMonitor", "Trackt alle Nachrichten", MessageReceivedEvent.class);
        CodebaseBot.getInstance().getMySQL().update("CREATE TABLE IF NOT EXISTS MessageMonitor (DAY VARCHAR(50), MESSAGES int);");

        try (final Connection connection = CodebaseBot.getInstance().getMySQL().getConnection()) {
            ResultSet resultSet = connection.prepareStatement("SELECT * FROM MessageMonitor WHERE DAY='" + Constants.simpleDateFormat.format(new Date()) + "';").executeQuery();
            if (resultSet.next()) {
                messages.put(Constants.simpleDateFormat.format(new Date()), resultSet.getInt("MESSAGES"));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void trigger() {
        String date = Constants.simpleDateFormat.format(new Date());
        if (messages.containsKey(date)) {
            messages.put(date, messages.get(date) + 1);
            CodebaseBot.getInstance().getMySQL().updateAsync("UPDATE MessageMonitor SET MESSAGES='" + messages.get(date) + "' WHERE DAY='" + date + "';");
        } else {
            messages.put(date, 1);
            CodebaseBot.getInstance().getMySQL().update("INSERT INTO MessageMonitor (DAY, MESSAGES) VALUES ('" + date + "'," + 1 + ");");
        }
    }

}
