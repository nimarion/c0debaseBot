package de.c0debase.bot.listener.guild;

import de.c0debase.bot.Codebase;
import de.c0debase.bot.database.model.User;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
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
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final Guild guild = event.getGuild();
        final Member member = event.getMember();
        embedBuilder.setFooter("@" + member.getUser().getName() + "#" + member.getUser().getDiscriminator(), member.getUser().getEffectiveAvatarUrl());
        embedBuilder.setColor(guild.getSelfMember().getColor());
        embedBuilder.setThumbnail(member.getUser().getEffectiveAvatarUrl());
        embedBuilder.appendDescription("Willkommen auf c0debase " + member.getAsMention() + "\n");
        embedBuilder.appendDescription("— Weise dir eine Rolle mit !role zu\n");
        embedBuilder.appendDescription("— Schaue dir die Regeln in #rules an");


        guild.getTextChannelById(System.getenv("BOTCHANNEL")).sendMessage(embedBuilder.build()).queue();

        guild.getTextChannelsByName("log", true).forEach(channel -> {
            final EmbedBuilder logBuilder = new EmbedBuilder();
            logBuilder.setFooter("@" + member.getUser().getName() + "#" + member.getUser().getDiscriminator(), member.getUser().getEffectiveAvatarUrl());
            logBuilder.setThumbnail(member.getUser().getEffectiveAvatarUrl());
            logBuilder.appendDescription("Erstelldatum: " + event.getUser().getTimeCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + "\n");
            logBuilder.appendDescription("Standart Avatar: " + (member.getUser().getAvatarUrl() == null) + "\n");
            channel.sendMessage(logBuilder.build()).queue();
        });

        final User user = bot.getDatabase().getUserDao().getOrCreateUser(guild.getId(), event.getUser().getId());
        final List<Role> roles = new ArrayList<>();
        user.getRoles().forEach(roleName -> {
            final Role role = guild.getRoleById(roleName);
            if (role != null && PermissionUtil.canInteract(guild.getSelfMember(), role)) {
                roles.add(role);
            }
        });
        guild.modifyMemberRoles(member, roles, Collections.emptyList()).queue(success -> {
            final Role projectRole = event.getJDA().getRoleById(PROJECT_ROLE_ID);
            if (projectRole == null) return;
            if (user.getLevel() > 2 && !member.getRoles().contains(projectRole)) {
                guild.addRoleToMember(member, projectRole).queue();
            }
        });
    }
}
