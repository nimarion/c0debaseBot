package de.c0debase.bot.listener.guild;

import de.c0debase.bot.Codebase;
import de.c0debase.bot.database.model.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberRoleListener extends ListenerAdapter {

    private final Codebase bot;

    public GuildMemberRoleListener(final Codebase bot) {
        this.bot = bot;
        bot.getJDA().addEventListener(this);
    }

    @Override
    public void onGuildMemberRoleAdd(final GuildMemberRoleAddEvent event) {
        final User user = bot.getDatabase().getUserDao().getOrCreateUser(event.getGuild().getId(),
                event.getUser().getId());
        event.getRoles().forEach(role -> {
            if (role != null && !user.getRoles().contains(role.getId())) {
                user.getRoles().add(role.getId());
            }
            bot.getDatabase().getUserDao().updateUser(user);
        });
    }

    @Override
    public void onGuildMemberRoleRemove(final GuildMemberRoleRemoveEvent event) {
        final User user = bot.getDatabase().getUserDao().getOrCreateUser(event.getGuild().getId(),
                event.getUser().getId());
        event.getRoles().forEach(role -> {
            if (role != null) {
                user.getRoles().remove(role.getId());
            }
            bot.getDatabase().getUserDao().updateUser(user);
        });
    }
}
