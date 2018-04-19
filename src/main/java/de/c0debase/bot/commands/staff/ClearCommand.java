package de.c0debase.bot.commands.staff;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;

import java.util.List;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class ClearCommand extends Command {

    public ClearCommand() {
        super("clear", "Löscht eine bestimmte Anzahl an Nachrichten", Categorie.STAFF);
    }

    @Override
    public void execute(String[] args, Message msg) {
            if (args.length == 0) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setFooter("@" + msg.getMember().getUser().getName() + "#" + msg.getMember().getUser().getDiscriminator(), msg.getMember().getUser().getEffectiveAvatarUrl());
                embedBuilder.setColor(msg.getGuild().getSelfMember().getColor());
                embedBuilder.appendDescription("!clear <Amount>");
                msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
            } else {
                int i = 0;
                try {
                    i = Integer.valueOf(args[0]);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
                List<Message> msgs;
                MessageHistory history = new MessageHistory(msg.getTextChannel());
                msgs = history.retrievePast(i + 1).complete();
                msg.getTextChannel().deleteMessages(msgs).queue();

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setFooter("@" + msg.getMember().getUser().getName() + "#" + msg.getMember().getUser().getDiscriminator(), msg.getMember().getUser().getEffectiveAvatarUrl());
                embedBuilder.setColor(msg.getGuild().getSelfMember().getColor());
                embedBuilder.appendDescription("Es wurden **" + (i) + "** Nachrichten gelöscht");
                msg.getTextChannel().sendMessage(embedBuilder.build()).queue();

            }
    }
}
