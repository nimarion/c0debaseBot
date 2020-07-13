package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author finreinhard Created on 21.10.2019.
 */
public class SetColorCommand extends Command {

    public SetColorCommand() {
        super("setColor", "Setze Deine persönliche Farbe", Category.GENERAL, "setzeFarbe");
    }

    @Override
    public void execute(String[] args, Message message) {
        if (args.length > 1) {
            message.getTextChannel().sendMessage(
                    "Du kannst nur eine Farbe auswählen, gib bitte daher auch nur ein Parameter an. Benutze !setColor <Farbe>")
                    .queue();
            return;
        }

        if (args.length == 0) {
            listAvailableColors(message);
            return;
        }

        final List<Role> foundRoles = message.getGuild().getRolesByName("Color-" + args[0], true);

        if (foundRoles.size() == 0) {
            listAvailableColors(message);
            return;
        }

        final List<Role> memberColorRoles = message.getMember().getRoles().stream()
                .filter(role -> role.getName().startsWith("Color-")).collect(Collectors.toList());

        message.getTextChannel().sendMessageFormat("Du hast jetzt die Farbe %s.", args[0]).queue();
        message.getGuild().modifyMemberRoles(message.getMember(), foundRoles, memberColorRoles).queue();
    }

    private void listAvailableColors(Message message) {
        final EmbedBuilder embedBuilder = getEmbed(message.getGuild(), message.getAuthor());
        embedBuilder.setFooter("!setColor <Farbe>", message.getMember().getUser().getEffectiveAvatarUrl());

        embedBuilder.appendDescription("__**Es gibt diese Farben:**__\n\n");

        message.getGuild().getRoles().stream().map(Role::getName).filter(roleName -> roleName.startsWith("Color"))
                .forEach(roleName -> embedBuilder
                        .appendDescription(String.format("***%s***\n", roleName.replace("Color-", ""))));

        message.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }
}
