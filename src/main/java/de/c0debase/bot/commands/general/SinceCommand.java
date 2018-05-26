package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.entities.Message;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

/**
 * @author Biosphere
 * @date 24.03.18
 */
public class SinceCommand extends Command {

    public SinceCommand() {
        super("since", "Zeigt wie lang du schon auf dem Server bist", Categorie.GENERAL);
    }

    @Override
    public void execute(String[] args, Message msg) {
        msg.getTextChannel().sendMessage(getEmbed(msg.getGuild(), msg.getAuthor())
                .setDescription(msg.getMentionedMembers().stream().findFirst().orElse(msg.getMember()).getAsMention() + " ist seit " + ChronoUnit.DAYS.between(msg.getMentionedMembers().stream().findFirst().orElse(msg.getMember()).getJoinDate(), LocalDateTime.now().atOffset(ZoneOffset.UTC)) + " Tagen auf " + msg.getGuild().getName())
                .build()).queue();
    }
}
