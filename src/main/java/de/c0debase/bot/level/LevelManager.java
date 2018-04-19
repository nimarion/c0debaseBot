package de.c0debase.bot.level;

import de.c0debase.bot.CodebaseBot;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Invite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Biosphere
 * @date 23.01.18
 */
@Getter
public class LevelManager {

    private HashMap<String, LevelUser> levelUsers;
    @Setter
    private long lastJoin = System.currentTimeMillis();

    public LevelManager() {
        this.levelUsers = new HashMap<>();
    }

    public void startInviteChecker() {
        if (System.getenv("BOTCHANNEL") == null) {
            return;
        }
        ConcurrentHashMap<String, Invite> inviteHashMap = new ConcurrentHashMap<>();
        CodebaseBot.getInstance().getExecutorService().scheduleAtFixedRate(() -> CodebaseBot.getInstance().getJda().getGuilds().get(0).getInvites().queue(success -> {

            success.forEach(invite -> {
                if (inviteHashMap.containsKey(invite.getCode()) && invite.getUses() > inviteHashMap.get(invite.getCode()).getUses()) {
                    LevelUser levelUser = CodebaseBot.getInstance().getLevelManager().getLevelUser(invite.getInviter().getId());

                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setDescription(invite.getInviter().getAsMention() + " vielen Dank das du jemand neues auf c0debase gebracht hast");
                    CodebaseBot.getInstance().getJda().getTextChannelById(System.getenv("BOTCHANNEL")).sendMessage(embedBuilder.build()).queue();

                    if (levelUser.addXP(100)) {
                        EmbedBuilder levelUpEmbed = new EmbedBuilder();
                        levelUpEmbed.setDescription(invite.getInviter().getAsMention() + " ist nun Level " + levelUser.getLevel());
                        CodebaseBot.getInstance().getJda().getTextChannelById(System.getenv("BOTCHANNEL")).sendMessage(levelUpEmbed.build()).queue();
                    }
                }
                inviteHashMap.put(invite.getCode(), invite);
            });
            inviteHashMap.keySet().forEach(invite -> {
                if (!success.contains(inviteHashMap.get(invite)) && ((lastJoin - System.currentTimeMillis()) / 1000) > -7) {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setDescription(inviteHashMap.get(invite).getInviter().getAsMention() + " vielen Dank das du jemand neues auf c0debase gebracht hast");
                    CodebaseBot.getInstance().getJda().getTextChannelById(System.getenv("BOTCHANNEL")).sendMessage(embedBuilder.build()).queue();
                    LevelUser levelUser = CodebaseBot.getInstance().getLevelManager().getLevelUser(inviteHashMap.get(invite).getInviter().getId());


                    if (levelUser.addXP(100)) {
                        EmbedBuilder levelUpEmbed = new EmbedBuilder();
                        levelUpEmbed.setDescription(inviteHashMap.get(invite).getInviter().getAsMention() + " ist nun Level " + levelUser.getLevel());
                        CodebaseBot.getInstance().getJda().getTextChannelById(System.getenv("BOTCHANNEL")).sendMessage(levelUpEmbed.build()).queue();
                    }
                    inviteHashMap.remove(invite);
                }
            });
        }), 5, 5, TimeUnit.SECONDS);
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
