package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Biosphere
 * @date 23.01.18
 */

public class RoleCommand extends Command {

    public RoleCommand() {
        super("role", "Weise dir eine Programmiersprache zu", Categorie.GENERAL, "rolle");
    }

    @Override
    public void execute(String[] args, Message msg) {
        if (args.length == 0) {
            EmbedBuilder embedBuilder = getEmbed(msg.getGuild(), msg.getAuthor());
            embedBuilder.setFooter("!role Java,Go,Javascript", msg.getMember().getUser().getEffectiveAvatarUrl());
            embedBuilder.setTitle("Es gibt diese Rollen:");

            for (Role role : msg.getGuild().getRoles()) {
                if (!role.getName().equalsIgnoreCase("@everyone") && !role.getName().equalsIgnoreCase("Projekt") && PermissionUtil.canInteract(msg.getGuild().getSelfMember(), role)) {
                    embedBuilder.appendDescription("***" + role.getName() + "***" + "\n");
                }
            }
            msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
        } else {
            changeRole(String.join("", args), msg);
        }
    }

    private void changeRole(String args, Message message) {
        List<Role> addRoles = new ArrayList<>();
        List<Role> removeRoles = new ArrayList<>();
        for (String role : args.split(",")) {
            if (!message.getGuild().getRolesByName(role, true).isEmpty()) {
                Role rrole = message.getGuild().getRolesByName(role, true).get(0);
                if (PermissionUtil.canInteract(message.getGuild().getSelfMember(), rrole)) {
                    if (message.getGuild().getMembersWithRoles(rrole).contains(message.getMember()) && !removeRoles.contains(rrole) && !rrole.getName().equalsIgnoreCase("Projekt")) {
                        removeRoles.add(rrole);
                    } else if (!addRoles.contains(rrole)) {
                        addRoles.add(rrole);
                    }
                }
            }
        }
        EmbedBuilder embedBuilder = getEmbed(message.getGuild(), message.getAuthor());
        embedBuilder.setTitle("Rolle(n) geupdatet");
        embedBuilder.appendDescription("Du bist " + addRoles.size() + (addRoles.size() > 1 ? " Rollen " : " Rolle ") + "beigetreten\n");
        embedBuilder.appendDescription("Du hast " + removeRoles.size() + (removeRoles.size() > 1 ? " Rollen " : " Rolle ") + " Rollen verlassen");
        message.getTextChannel().sendMessage(embedBuilder.build()).queue();
        message.getGuild().getController().addRolesToMember(message.getMember(), addRoles).queue(success -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            message.getGuild().getController().removeRolesFromMember(message.getMember(), removeRoles).queue();
        });
    }
}
