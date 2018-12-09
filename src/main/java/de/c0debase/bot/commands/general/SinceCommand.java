package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class SinceCommand extends Command {

    public SinceCommand() {
        super("since", "Zeigt wie lang du schon auf dem Server bist", Category.GENERAL);
    }

    @Override
    public void execute(final String[] args, final Message message) {
        final Member member = message.getMentionedMembers().size() == 0 ? message.getMember() : message.getMentionedMembers().get(0);
        message.getTextChannel().sendMessage(getEmbed(message.getGuild(), message.getAuthor())
                .setDescription(member.getAsMention() + " ist seit " + ChronoUnit.DAYS.between(member.getJoinDate(), LocalDateTime.now().atOffset(ZoneOffset.UTC)) + " Tagen auf " + message.getGuild().getName())
                .build()).queue();
    }
}
