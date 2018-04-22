package de.c0debase.bot.level;

import de.c0debase.bot.CodebaseBot;
import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 * @author Biosphere
 * @date 23.01.18
 */

@Getter
public class LevelUser {

    private final String id;
    private int level, xp;
    @Setter
    private long lastMessage = 0L;

    public LevelUser(String id) {
        this.id = id;
    }

    public void create() {
        if (!exists()) {
            level = 0;
            xp = 0;
            CodebaseBot.getInstance().getMySQL().update("INSERT INTO Users (ID, XP, LEVEL) VALUES (" + id + "," + xp + ", " + level + ");");
        }
    }

    /**
     * @return if the user has reached a new level
     */
    public boolean addXP(int xp) {
        setXP(getXp() + new Random().nextInt(xp));
        double reach = 1000 * getLevel() * 1.2;
        if (getXp() >= reach && reach != 0) {
            setXP(0);
            setLevel(getLevel() + 1);
            return true;
        } else if (getXp() >= 1000 && getLevel() == 0) {
            setLevel(getLevel() + 1);
            return true;
        }
        return false;
    }

    public void read() {
        if (exists()) {
            try (final Connection connection = CodebaseBot.getInstance().getMySQL().getConnection()) {
                ResultSet resultSet = connection.prepareStatement("SELECT * FROM Users WHERE ID=" + id + ";").executeQuery();
                if (resultSet.next()) {
                    xp = resultSet.getInt("XP");
                    level = resultSet.getInt("LEVEL");
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        } else {
            create();
            read();
        }
    }

    public void setLevel(int level) {
        this.level = level;
        CodebaseBot.getInstance().getMySQL().updateAsync("UPDATE Users SET LEVEL='" + level + "' WHERE ID='" + id + "';");
        if (level >= 3 && !CodebaseBot.getInstance().getJda().getGuilds().get(0).getRolesByName("Projekt", true).isEmpty()) {
            CodebaseBot.getInstance().getJda().getGuilds().get(0).getController().addRolesToMember(CodebaseBot.getInstance().getJda().getGuilds().get(0).getMemberById(id), CodebaseBot.getInstance().getJda().getGuilds().get(0).getRolesByName("Projekt", true).get(0)).queue();
        }
    }

    public void setXP(int xp) {
        this.xp = xp;
        CodebaseBot.getInstance().getMySQL().updateAsync("UPDATE Users SET XP='" + xp + "' WHERE ID='" + id + "';");
    }

    public boolean exists() {
        try (final Connection connection = CodebaseBot.getInstance().getMySQL().getConnection()) {
            ResultSet resultSet = connection.prepareStatement("SELECT * FROM Users WHERE id='" + id + "';").executeQuery();
            return resultSet.next() && resultSet.getString("XP") != null;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }
}
