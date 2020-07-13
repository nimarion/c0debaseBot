package de.c0debase.bot.listener.guild;

import de.c0debase.bot.Codebase;
import de.c0debase.bot.database.model.User;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.utils.PermissionUtil;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuildMemberJoinListener extends ListenerAdapter {

    private static final long PROJECT_ROLE_ID = 408957966998568960L;

    private final Codebase bot;

    public GuildMemberJoinListener(final Codebase bot) {
        this.bot = bot;
        bot.getJDA().addEventListener(this);
    }

    @Override
    public void onGuildMemberJoin(final GuildMemberJoinEvent event) {
        final Member member = event.getMember();

        sendWelcomeMessage(member);
        sendLogMessage(member);
        addRoles(member);
    }

    private void sendWelcomeMessage(final Member member) {
        if (System.getenv("BOTCHANNEL") == null) {
            return;
        }
        final boolean firstJoin = bot.getDatabase().getUserDao().getUser(member.getGuild().getId(),
                member.getId()) == null;
        final EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setFooter("@" + member.getUser().getName() + "#" + member.getUser().getDiscriminator(),
                member.getUser().getEffectiveAvatarUrl());
        embedBuilder.setColor(member.getGuild().getSelfMember().getColor());
        embedBuilder.setThumbnail(member.getUser().getEffectiveAvatarUrl());
        embedBuilder.appendDescription(
                "Willkommen " + (firstJoin ? "" : "zurück ") + "auf c0debase " + member.getAsMention() + "\n");
        embedBuilder.appendDescription("— Weise dir eine Rolle mit !role zu\n");
        embedBuilder.appendDescription("— Schaue dir die Regeln in #rules an");

        member.getGuild().getTextChannelById(System.getenv("BOTCHANNEL")).sendMessage(embedBuilder.build()).queue();
    }

    private void sendLogMessage(final Member member) {
        member.getGuild().getTextChannelsByName("log", true).forEach(channel -> {
            final EmbedBuilder logBuilder = new EmbedBuilder();
            logBuilder.setFooter("@" + member.getUser().getName() + "#" + member.getUser().getDiscriminator(),
                    member.getUser().getEffectiveAvatarUrl());
            logBuilder.setThumbnail(member.getUser().getEffectiveAvatarUrl());
            logBuilder.appendDescription("Erstelldatum: "
                    + member.getUser().getTimeCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + "\n");
            logBuilder.appendDescription("Standard Avatar: " + (member.getUser().getAvatarUrl() == null) + "\n");
            channel.sendMessage(logBuilder.build()).queue();
        });
    }

    private void addRoles(final Member member) {
        final User user = bot.getDatabase().getUserDao().getOrCreateUser(member.getGuild().getId(), member.getId());
        final List<Role> roles = new ArrayList<>();
        user.getRoles().forEach(roleName -> {
            final Role role = member.getGuild().getRoleById(roleName);
            if (role != null && PermissionUtil.canInteract(member.getGuild().getSelfMember(), role)) {
                roles.add(role);
            }
        });
        member.getGuild().modifyMemberRoles(member, roles, Collections.emptyList()).queue(success -> {
            final Role projectRole = member.getJDA().getRoleById(PROJECT_ROLE_ID);
            if (projectRole == null) {
                return;
            }
            if (user.getLevel() > 2 && !member.getRoles().contains(projectRole)) {
                member.getGuild().addRoleToMember(member, projectRole).queue();
            }
        });
    }
}
