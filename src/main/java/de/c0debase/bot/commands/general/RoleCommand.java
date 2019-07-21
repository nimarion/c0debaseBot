package de.c0debase.bot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.c0debase.bot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.internal.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Biosphere
 * @date 23.01.18
 */

public class RoleCommand extends Command {

    private static final List<String> FORBIDDEN;

    static {
        FORBIDDEN = Arrays.asList("Projekt", "Friend", "-_-", "Mute", "@everyone");
    }

    public RoleCommand() {
        this.name = "role";
        this.help = "Weise dir eine Programmiersprache zu";
        this.guildOnly = true;
        this.aliases = new String[]{"roles"};
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (commandEvent.getArgs().isEmpty()) {
            EmbedBuilder embedBuilder = EmbedUtils.getEmbed(commandEvent.getAuthor(), true);
            embedBuilder.setFooter("!role Java,Go,Javascript", commandEvent.getAuthor().getEffectiveAvatarUrl());
            embedBuilder.setTitle("Es gibt diese Rollen:");

            embedBuilder.appendDescription("`!role Java,Go,C#`\n\n");

            for (Role role : commandEvent.getGuild().getRoles()) {
                if (!role.isManaged() && !FORBIDDEN.contains(role.getName()) && PermissionUtil.canInteract(commandEvent.getMember(), role)) {
                    embedBuilder.appendDescription("***" + role.getName() + "***" + "\n");
                }
            }
            commandEvent.reply(embedBuilder.build());
        } else {
            changeRole(String.join(" ", commandEvent.getArgs()).replaceAll(",", " "), commandEvent);
        }
    }

    private void changeRole(final String args, final CommandEvent commandEvent) {
        final List<Role> addRoles = new ArrayList<>();
        final List<Role> removeRoles = new ArrayList<>();
        for (String role : args.split(" ")) {
            if (!role.isEmpty() && !commandEvent.getGuild().getRolesByName(role, true).isEmpty() && !FORBIDDEN.contains(role)) {
                Role rrole = commandEvent.getGuild().getRolesByName(role, true).get(0);
                if (PermissionUtil.canInteract(commandEvent.getGuild().getSelfMember(), rrole) && !rrole.isManaged()) {
                    if (commandEvent.getGuild().getMembersWithRoles(rrole).contains(commandEvent.getMember()) && !removeRoles.contains(rrole)) {
                        removeRoles.add(rrole);
                    } else if (!addRoles.contains(rrole)) {
                        addRoles.add(rrole);
                    }
                }
            }
        }
        final EmbedBuilder embedBuilder = EmbedUtils.getEmbed(commandEvent.getAuthor(), true);
        embedBuilder.setTitle("Rolle(n) geupdatet");
        embedBuilder.appendDescription("Du bist " + addRoles.size() + (addRoles.size() > 1 ? " Rollen " : " Rolle ") + "beigetreten\n");
        embedBuilder.appendDescription("Du hast " + removeRoles.size() + (removeRoles.size() == 1 ? " Rolle " : " Rollen ") + "verlassen");
        commandEvent.getGuild().modifyMemberRoles(commandEvent.getMember(), addRoles, removeRoles).queue(roleUpdate -> commandEvent.reply(embedBuilder.build()));
    }
}
