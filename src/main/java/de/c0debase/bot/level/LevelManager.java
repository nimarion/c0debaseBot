package de.c0debase.bot.level;

import de.c0debase.bot.CodebaseBot;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Invite;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Biosphere
 * @date 23.01.18
 */
@Getter
public class LevelManager {

    @Setter
    private long lastJoin = System.currentTimeMillis();

    public void startInviteChecker() {
        if (System.getenv("BOTCHANNEL") == null) {
            return;
        }
        ConcurrentHashMap<String, Invite> inviteHashMap = new ConcurrentHashMap<>();
        CodebaseBot.getInstance().getExecutorService().scheduleAtFixedRate(() -> CodebaseBot.getInstance().getJda().getGuilds().get(0).getInvites().queue(success -> {

            success.forEach(invite -> {
                if (inviteHashMap.containsKey(invite.getCode()) && invite.getUses() > inviteHashMap.get(invite.getCode()).getUses()) {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setDescription(invite.getInviter().getAsMention() + " vielen Dank das du jemand neues auf c0debase gebracht hast");
                    CodebaseBot.getInstance().getJda().getTextChannelById(System.getenv("BOTCHANNEL")).sendMessage(embedBuilder.build()).queue();

                    CodebaseBot.getInstance().getMongoDataManager().getLevelUser(CodebaseBot.getInstance().getJda().getGuilds().get(0).getId(), inviteHashMap.get(invite).getInviter().getId(), levelUser -> {
                        if (levelUser.addXP(100)) {
                            EmbedBuilder levelUpEmbed = new EmbedBuilder();
                            levelUpEmbed.setDescription(invite.getInviter().getAsMention() + " ist nun Level " + levelUser.getLevel());
                            CodebaseBot.getInstance().getJda().getTextChannelById(System.getenv("BOTCHANNEL")).sendMessage(levelUpEmbed.build()).queue();
                        }
                    });
                }
                inviteHashMap.put(invite.getCode(), invite);
            });
            inviteHashMap.keySet().forEach(invite -> {
                if (!success.contains(inviteHashMap.get(invite)) && ((lastJoin - System.currentTimeMillis()) / 1000) > -7) {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setDescription(inviteHashMap.get(invite).getInviter().getAsMention() + " vielen Dank das du jemand neues auf c0debase gebracht hast");
                    CodebaseBot.getInstance().getJda().getTextChannelById(System.getenv("BOTCHANNEL")).sendMessage(embedBuilder.build()).queue();
                    CodebaseBot.getInstance().getMongoDataManager().getLevelUser(CodebaseBot.getInstance().getJda().getGuilds().get(0).getId(), inviteHashMap.get(invite).getInviter().getId(), levelUser -> {
                        if (levelUser.addXP(100)) {
                            EmbedBuilder levelUpEmbed = new EmbedBuilder();
                            levelUpEmbed.setDescription(inviteHashMap.get(invite).getInviter().getAsMention() + " ist nun Level " + levelUser.getLevel());
                            CodebaseBot.getInstance().getJda().getTextChannelById(System.getenv("BOTCHANNEL")).sendMessage(levelUpEmbed.build()).queue();
                        }
                    });
                    inviteHashMap.remove(invite);
                }
            });
        }), 5, 5, TimeUnit.SECONDS);
    }



}
