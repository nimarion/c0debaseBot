package de.c0debase.bot.listener.guild;

import de.c0debase.bot.core.Codebase;
import de.c0debase.bot.database.data.CodebaseUser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GuildMemberJoinListener extends ListenerAdapter {

    private static final long PROJECT_ROLE_ID = 361603492642684929L;

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
            logBuilder.appendDescription("Erstelldatum: " + event.getUser().getCreationTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + "\n");
            logBuilder.appendDescription("Standart Avatar: " + (member.getUser().getAvatarUrl() == null) + "\n");
            channel.sendMessage(logBuilder.build()).queue();
        });

        final CodebaseUser codebaseUser = bot.getDataManager().getUserData(guild.getId(), event.getUser().getId());
        final List<Role> roles = new ArrayList<>();
        codebaseUser.getRoles().forEach(roleName -> {
            final Role role = guild.getRoleById(roleName);
            if (role != null && PermissionUtil.canInteract(guild.getSelfMember(), role)) {
                roles.add(role);
            }
        });
        final GuildController guildController = guild.getController();
        guildController.addRolesToMember(member, roles).reason("Autorole").queue(success -> {
            final Role projectRole = event.getJDA().getRoleById(PROJECT_ROLE_ID);
            if(codebaseUser.getLevel() > 2 && !member.getRoles().contains(projectRole)) {
                guildController.addSingleRoleToMember(member, projectRole).queue();
            }
        });
    }
}
