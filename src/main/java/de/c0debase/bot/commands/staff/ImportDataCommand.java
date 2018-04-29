package de.c0debase.bot.commands.staff;

import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Biosphere
 * @date 29.04.18
 */
public class ImportDataCommand extends Command {

    public ImportDataCommand() {
        super("importdata", "Move everything into MongoDB", Categorie.STAFF);
    }

    @Override
    public void execute(String[] args, Message msg) {
        for (Member member : msg.getGuild().getMembers()) {
            try (final Connection connection = CodebaseBot.getInstance().getMySQL().getConnection()) {
                ResultSet resultSet = connection.prepareStatement("SELECT * FROM Users WHERE ID=" + member.getUser().getId() + ";").executeQuery();
                if (resultSet.next()) {
                    CodebaseBot.getInstance().getMongoDataManager().getLevelUser(member.getGuild().getId(), member.getUser().getId(), levelUser -> {
                        try {
                            levelUser.setXp(resultSet.getInt("XP"));
                            levelUser.setLevel(resultSet.getInt("LEVEL"));
                            member.getRoles().forEach(role -> levelUser.getRoles().add(role.getName()));
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                        }
                        CodebaseBot.getInstance().getMongoDataManager().updateLevelUser(levelUser);
                    });
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }
}
