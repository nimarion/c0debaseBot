package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.internal.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoleCommand extends Command {

    private static final List<String> FORBIDDEN;

    static {
        FORBIDDEN = Arrays.asList("Projekt", "Friend", "Mute", "@everyone");
    }

    public RoleCommand() {
        super("role", "Weise dir eine Programmiersprache zu", Category.GENERAL, "rolle", "roles");
    }

    @Override
    public void execute(final String[] args, final Message message) {
        if (args.length == 0) {
            final EmbedBuilder embedBuilder = getEmbed(message.getGuild(), message.getAuthor());
            embedBuilder.setFooter("!role Java,Go,Javascript", message.getMember().getUser().getEffectiveAvatarUrl());
            embedBuilder.appendDescription("__**Es gibt diese Rollen:**__\n\n");

            appendRoles(embedBuilder, message.getGuild());
            appendColors(embedBuilder, message.getGuild());
            message.getTextChannel().sendMessage(embedBuilder.build()).queue();
        } else {
            changeRole(String.join(" ", args).replaceAll(",", " "), message);
        }
    }

    private void changeRole(final String args, final Message message) {
        final List<Role> addRoles = new ArrayList<>();
        final List<Role> removeRoles = new ArrayList<>();
        for (final String role : args.split(" ")) {
            if (isRoleAvailable(role, message.getGuild())) {
                addRolesToList(message.getMember(), role, addRoles, removeRoles);
            } else if (isRoleAvailable("Color-" + role, message.getGuild())) {
                addRolesToList(message.getMember(), "Color-" + role, addRoles, removeRoles);
            }
        }

        message.getGuild().modifyMemberRoles(message.getMember(), addRoles, removeRoles).queue(sucess -> {
            final EmbedBuilder embedBuilder = getEmbed(message.getGuild(), message.getAuthor());
            embedBuilder.setTitle("Rolle(n) geupdatet");
            embedBuilder.appendDescription(
                    "Du bist " + addRoles.size() + (addRoles.size() > 1 ? " Rollen " : " Rolle ") + "beigetreten\n");
            embedBuilder.appendDescription(
                    "Du hast " + removeRoles.size() + (removeRoles.size() == 1 ? " Rolle " : " Rollen ") + "verlassen");
            message.getTextChannel().sendMessage(embedBuilder.build()).queue();
        });
    }

    /**
     * Append all available roles to {@link EmbedBuilder}
     * 
     * @param embedBuilder
     * @param guild
     */
    private void appendRoles(final EmbedBuilder embedBuilder, final Guild guild) {
        for (final Role role : guild.getRoles()) {
            if (!role.isManaged() && !FORBIDDEN.contains(role.getName())
                    && PermissionUtil.canInteract(guild.getSelfMember(), role) && !role.getName().startsWith("Color")) {
                embedBuilder.appendDescription("***" + role.getName() + "***" + "\n");
            }
        }
    }

    /**
     * Append all available color roles to {@link EmbedBuilder}
     * 
     * @param embedBuilder
     * @param guild
     */
    private void appendColors(final EmbedBuilder embedBuilder, final Guild guild) {
        embedBuilder.appendDescription("\n__**Es gibt diese Farben:**__\n\n");
        for (final Role role : guild.getRoles()) {
            if (role.getName().startsWith("Color")) {
                embedBuilder.appendDescription("***" + role.getName().replace("Color-", "") + "***" + "\n");
            }
        }
    }

    /**
     * 
     * @param role
     * @param guild
     * @return if the role can be assigned to a user by the bot
     */
    private boolean isRoleAvailable(final String role, final Guild guild) {
        if (role.isEmpty() || FORBIDDEN.contains(role)) {
            return false;
        }
        final List<Role> roles = guild.getRolesByName(role, true);
        if (roles.isEmpty()) {
            return false;
        }
        return PermissionUtil.canInteract(guild.getSelfMember(), roles.get(0)) && !roles.get(0).isManaged();
    }

    /**
     * Check whether the role must be removed or added
     */
    private static void addRolesToList(final Member member, final String roleName, final List<Role> addList,
            final List<Role> removeList) {
        final Role role = member.getGuild().getRolesByName(roleName, true).get(0);
        if (member.getGuild().getMembersWithRoles(role).contains(member) && !removeList.contains(role)) {
            removeList.add(role);
        } else if (!addList.contains(role)) {
            addList.add(role);
        }
    }
}
