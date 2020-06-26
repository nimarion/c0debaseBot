package de.c0debase.bot.database.model;

import de.c0debase.bot.utils.Constants;

import java.util.Collections;
import java.util.List;

public class User {

    private String userID;
    private String guildID;
    private int level;
    private int xp;
    private Double coins;
    private long lastMessage;
    private List<String> roles;

    public static User newUser(final String userId, final String guildId){
        final User user = new User();
        user.setUserID(userId);
        user.setGuildID(guildId);
        user.setRoles(Collections.emptyList());
        user.setLevel(0);
        user.setXp(0);
        user.setCoins(0D);
        return user;
    }

    public boolean addXP(int xp) {
        int morexp = Constants.RANDOM.nextInt(xp - 1 ) + 1;
        this.xp += morexp;
        coins += morexp * 0.03;
        double reach = 1000 * level * 1.2;
        if (this.xp >= reach && reach != 0) {
            this.xp = 0;
            level += 1;
            return true;
        } else if (this.xp >= 1000 && level == 0) {
            level += 1;
            return true;
        }
        return false;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getGuildID() {
        return guildID;
    }

    public void setGuildID(String guildID) {
        this.guildID = guildID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public Double getCoins() {
        return coins;
    }

    public void setCoins(Double coins) {
        this.coins = coins;
    }

    public long getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(long lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
