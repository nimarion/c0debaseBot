package de.c0debase.bot.listener.guild;

import de.c0debase.bot.CodebaseBot;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * @author Biosphere
 * @date 29.04.18
 */
public class GuildMemerRoleListener extends ListenerAdapter {

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        CodebaseBot.getInstance().getMongoDataManager().getLevelUser(event.getGuild().getId(), event.getUser().getId(), levelUser -> event.getRoles().forEach(role -> {
            if (!levelUser.getRoles().contains(role.getName())) {
                levelUser.getRoles().add(role.getName());
            }
            CodebaseBot.getInstance().getMongoDataManager().updateLevelUser(levelUser);
        }));
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        CodebaseBot.getInstance().getMongoDataManager().getLevelUser(event.getGuild().getId(), event.getUser().getId(), levelUser -> {
            event.getRoles().forEach(role -> levelUser.getRoles().remove(role.getName()));
            CodebaseBot.getInstance().getMongoDataManager().updateLevelUser(levelUser);
        });
    }
}
