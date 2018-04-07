package de.c0debase.bot.level;

import de.c0debase.bot.CodebaseBot;
import lombok.Getter;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class LevelManager {

    @Getter
    private HashMap<String, LevelUser> levelUsers;

    public LevelManager() {
        this.levelUsers = new HashMap<>();
    }

    public void startInviteChecker() {
        if (System.getenv("BOTCHANNEL") == null) {
            return;
        }
        HashMap<String, Integer> inviteHashMap = new HashMap<>();
        CodebaseBot.getInstance().getExecutorService().scheduleAtFixedRate(() -> CodebaseBot.getInstance().getJda().getGuilds().get(0).getInvites().queue(success -> success.forEach(invite -> {
            if (inviteHashMap.containsKey(invite.getCode()) && invite.getUses() > inviteHashMap.get(invite.getCode())) {
                LevelUser levelUser = CodebaseBot.getInstance().getLevelManager().getLevelUser(invite.getInviter().getId());

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.appendDescription(invite.getInviter().getAsMention() + " vielen Dank das du jemand neues auf c0debase gebracht hast");
                CodebaseBot.getInstance().getJda().getTextChannelById(System.getenv("BOTCHANNEL")).sendMessage(embedBuilder.build()).queue();

                if (levelUser.addXP(100)) {
                    EmbedBuilder levelUpEmbed = new EmbedBuilder();
                    levelUpEmbed.appendDescription(invite.getInviter().getAsMention() + " has leveled up to level " + levelUser.getLevel());
                    CodebaseBot.getInstance().getJda().getTextChannelById(System.getenv("BOTCHANNEL")).sendMessage(levelUpEmbed.build()).queue();
                }
            }
            inviteHashMap.put(invite.getCode(), invite.getUses());
        })), 5, 5, TimeUnit.SECONDS);
    }

    public LevelUser load(String id) {
        if (levelUsers.containsKey(id)) {
            return levelUsers.get(id);
        }
        LevelUser levelUser = new LevelUser(id);
        if (!levelUser.exists()) {
            levelUser.create();
        }
        levelUser.read();
        levelUsers.put(id, levelUser);
        return levelUser;
    }

    public LevelUser getLevelUser(String id) {
        return levelUsers.getOrDefault(id, load(id));
    }

    public List<LevelUser> getLevelUsersSorted() {
        ArrayList<LevelUser> levelUsers = new ArrayList<>(CodebaseBot.getInstance().getLevelManager().getLevelUsers().values());
        levelUsers.sort((o1, o2) -> {
            if (o1.getLevel() != o2.getLevel()) {
                return Double.compare(o2.getLevel(), o1.getLevel());
            } else {
                return Double.compare((o2.getXp() + o2.getLevel() + (1000 * o2.getLevel() * 1.2)), (o1.getXp() + o1.getLevel() + (1000 * o1.getLevel() * 1.2)));
            }
        });
        return levelUsers;
    }

}
