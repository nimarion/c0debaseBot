package de.c0debase.bot.listener.guild;

import de.c0debase.bot.core.Codebase;
import de.c0debase.bot.database.data.CodebaseUser;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * @author Biosphere
 * @date 29.04.18
 */
public class GuildMemberRoleListener extends ListenerAdapter {

    private final Codebase bot;

    public GuildMemberRoleListener(final Codebase bot) {
        this.bot = bot;
        bot.getJDA().addEventListener(this);
    }

    @Override
    public void onGuildMemberRoleAdd(final GuildMemberRoleAddEvent event) {
        final CodebaseUser codebaseUser = bot.getDataManager().getUserData(event.getGuild().getId(), event.getUser().getId());
        event.getRoles().forEach(role -> {
            if (role != null && !codebaseUser.getRoles().contains(role.getId())) {
                codebaseUser.getRoles().add(role.getId());
            }
            bot.getDataManager().updateUserData(codebaseUser);
        });
    }

    @Override
    public void onGuildMemberRoleRemove(final GuildMemberRoleRemoveEvent event) {
        final CodebaseUser codebaseUser = bot.getDataManager().getUserData(event.getGuild().getId(), event.getUser().getId());
        event.getRoles().forEach(role -> {
            if (role != null) {
                codebaseUser.getRoles().remove(role.getId());
            }
            bot.getDataManager().updateUserData(codebaseUser);
        });
    }
}
