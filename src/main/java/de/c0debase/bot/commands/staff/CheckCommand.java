package de.c0debase.bot.commands.staff;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.time.format.DateTimeFormatter;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class CheckCommand extends Command {

    public CheckCommand() {
        super("check", "Zeigt einige Informationen über ein Mitglied", Categorie.STAFF);
    }

    @Override
    public void execute(String[] args, Message msg) {
        if (msg.getMember().hasPermission(Permission.ADMINISTRATOR) || msg.getMember().hasPermission(Permission.BAN_MEMBERS)) {
            if (args.length < 1) {
                msg.getTextChannel().sendMessage(getEmbed(msg.getGuild(), msg.getAuthor()).setDescription("!check [id]").build()).queue();
                return;
            }
            String id = args[0];
            try {
                Long.valueOf(id);
            } catch (NumberFormatException exception) {
                msg.getTextChannel().sendMessage(getEmbed(msg.getGuild(), msg.getAuthor()).setDescription("!check [id]").build()).queue();
                return;
            }
            if (msg.getGuild().getMemberById(id) != null) {
                Member member = msg.getGuild().getMemberById(id);
                EmbedBuilder embedBuilder = getEmbed(msg.getGuild(), member.getUser());
                embedBuilder.addField("Erstelldatum: ", member.getUser().getCreationTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), true);
                embedBuilder.addField("Standart Avatar: ", String.valueOf(member.getUser().getAvatarUrl() == null), true);
                embedBuilder.addField("Beitritt: ", member.getJoinDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), true);

                msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
            } else {
                msg.getTextChannel().sendMessage(getEmbed(msg.getGuild(), msg.getAuthor()).setDescription("Mitglied wurde nicht gefunden").build()).queue();
            }
        }
    }
}
