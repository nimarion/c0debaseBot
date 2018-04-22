package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Biosphere
 * @date 21.04.18
 */
public class RoleStatsCommand extends Command {

    private static final List<String> FORBIDDEN;

    static {
        FORBIDDEN = Arrays.asList("Projekt", "Friend", "-_-", "Mute", "@everyone");
    }

    public RoleStatsCommand() {
        super("rolestats", "Zeigt wie viele Mitglieder eine Rolle haben", Categorie.GENERAL);
    }

    @Override
    public void execute(String[] args, Message msg) {
        EmbedBuilder embedBuilder = getEmbed(msg.getGuild(), msg.getAuthor());
        embedBuilder.setTitle("Rollen Statistiken");

        List<Role> roles = new ArrayList<>(msg.getGuild().getRoles());
        roles.sort((o1, o2) -> (int) (msg.getGuild().getMembers().stream().filter(member -> member.getRoles().contains(o2)).count() - msg.getGuild().getMembers().stream().filter(member -> member.getRoles().contains(o1)).count()));

        for (Role role : roles) {
            if (!role.isManaged() && !FORBIDDEN.contains(role.getName()) && PermissionUtil.canInteract(msg.getGuild().getSelfMember(), role)) {
                embedBuilder.appendDescription("***" + role.getName() + "*** (" + msg.getGuild().getMembers().stream().filter(member -> member.getRoles().contains(role)).count() + ")\n");
            }
        }
        msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }
}
