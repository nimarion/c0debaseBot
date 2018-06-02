package de.c0debase.bot.database.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Biosphere
 * @date 23.02.18
 */
@Data
public class LevelUser {

    private String userID, guildID;
    private int level, xp;
    private long lastMessage;
    private ArrayList<String> roles;

    public boolean addXP(int xp) {
        setXp(getXp() + new Random().nextInt(xp));
        double reach = 1000 * getLevel() * 1.2;
        if (getXp() >= reach && reach != 0) {
            setXp(0);
            setLevel(getLevel() + 1);
            return true;
        } else if (getXp() >= 1000 && getLevel() == 0) {
            setLevel(getLevel() + 1);
            return true;
        }
        return false;
    }
}
