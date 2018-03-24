package de.c0debase.bot.monitor;

import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.utils.Constants;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Biosphere
 * @date 14.02.18
 */
public class MemberMonitor extends Monitor {

    private final HashMap<String, Integer> joins = new HashMap<>();

    public MemberMonitor() {
        super("MemberMonitor", "Trackt alle neuen Mitglieder", GuildMemberJoinEvent.class);
        CodebaseBot.getInstance().getMySQL().update("CREATE TABLE IF NOT EXISTS MemberMonitor (DAY VARCHAR(50), MEMBER int);");
        try (ResultSet resultSet = CodebaseBot.getInstance().getMySQL().query("SELECT * FROM MemberMonitor WHERE MEMBER='" + Constants.simpleDateFormat.format(new Date()) + "';")) {
            if (resultSet.next()) {
                joins.put(Constants.simpleDateFormat.format(new Date()), resultSet.getInt("MEMBER"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void trigger() {
        String date = Constants.simpleDateFormat.format(new Date());
        if (joins.containsKey(date)) {
            joins.put(date, joins.get(date) + 1);
            CodebaseBot.getInstance().getMySQL().updateAsync("UPDATE MemberMonitor SET MEMBER='" + joins.get(date) + "' WHERE DAY='" + date + "';");
        } else {
            joins.put(date, 1);
            CodebaseBot.getInstance().getMySQL().update("INSERT INTO MemberMonitor (DAY, MEMBER) VALUES ('" + date + "'," + 1 + ");");
        }
    }
}
