package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.entities.Member;
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
        super("since", "Zeigt wie lang du schon auf dem Server bist", Category.GENERAL);
    }

    @Override
    public void execute(String[] args, Message msg) {
        Member member = args.length == 0 ? msg.getMember() : searchMember(args[0], msg.getMember());

        msg.getTextChannel().sendMessage(getEmbed(msg.getGuild(), msg.getAuthor())
                .setDescription(member.getAsMention() + " ist seit " + ChronoUnit.DAYS.between(member.getJoinDate(), LocalDateTime.now().atOffset(ZoneOffset.UTC)) + " Tagen auf " + msg.getGuild().getName())
                .build()).queue();
    }
}
