package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.internal.utils.PermissionUtil;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RoleStatsCommand extends Command {

    private static final List<String> FORBIDDEN;
    private static final String DESCRIPTION_PATTERN = "***%s*** (%d)\n";

    static {
        FORBIDDEN = Arrays.asList("Projekt", "Friend", "-_-", "Mute", "@everyone");
    }

    public RoleStatsCommand() {
        super("rolestats", "Zeigt wie viele Mitglieder eine Rolle haben", Category.GENERAL);
    }

    @Override
    public void execute(final String[] args, final Message message) {
        final Guild guild = message.getGuild();
        final EmbedBuilder embedBuilder = getEmbed(guild, message.getAuthor()).setTitle("Rollen Statistiken");
        final Map<Role, Long> roles = guild.getMembers().stream()
                .filter(member -> PermissionUtil.canInteract(guild.getSelfMember(), member))
                .map(member -> member.getRoles())
                .flatMap(stream -> stream.stream()
                        .filter(role -> (!role.isManaged() && !FORBIDDEN.contains(role.getName()))))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        embedBuilder.appendDescription("\n__**Rollen:**__\n\n");
        appendStats(roles, embedBuilder, false);
        embedBuilder.appendDescription("\n__**Farb-Rollen:**__\n\n");
        appendStats(roles, embedBuilder, true);
        message.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }

    private void appendStats(final Map<Role, Long> roles, EmbedBuilder embedBuilder, final boolean color) {
        roles.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .filter(role -> color ? role.getName().startsWith("Color-") : !role.getName().startsWith("Color-"))
                .map(role -> String.format(DESCRIPTION_PATTERN,
                        color ? role.getName().replace("Color-", "") : role.getName(), roles.get(role)))
                .forEach(embedBuilder::appendDescription);
    }

}