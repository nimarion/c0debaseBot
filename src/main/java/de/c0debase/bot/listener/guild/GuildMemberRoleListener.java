package de.c0debase.bot.listener.guild;

import de.c0debase.bot.CodebaseBot;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * @author Biosphere
 * @date 29.04.18
 */
public class GuildMemberRoleListener extends ListenerAdapter {

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        CodebaseBot.getInstance().getMongoDataManager().getLevelUser(event.getGuild().getId(), event.getUser().getId(), levelUser -> event.getRoles().forEach(role -> {
            if (role != null && !levelUser.getRoles().contains(role.getId())) {
                levelUser.getRoles().add(role.getId());
            }
            CodebaseBot.getInstance().getMongoDataManager().updateLevelUser(levelUser);
        }));
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        CodebaseBot.getInstance().getMongoDataManager().getLevelUser(event.getGuild().getId(), event.getUser().getId(), levelUser -> {
            event.getRoles().forEach(role -> {
                if(role != null){
                    levelUser.getRoles().remove(role.getId());
                }
            });
            CodebaseBot.getInstance().getMongoDataManager().updateLevelUser(levelUser);
        });
    }
}
