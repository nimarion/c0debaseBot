package de.c0debase.bot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.c0debase.bot.utils.EmbedUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class SinceCommand extends Command {

    public SinceCommand() {
        this.name = "since";
        this.help = "Zeigt wie lang du schon auf dem Server bist";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        final Message message = commandEvent.getMessage();
        final Member member = message.getMentionedMembers().size() == 0 ? message.getMember() : message.getMentionedMembers().get(0);
        message.getTextChannel().sendMessage(EmbedUtils.getEmbed(commandEvent.getAuthor(), true)
                .setDescription(member.getAsMention() + " ist seit " + ChronoUnit.DAYS.between(member.getTimeJoined(), LocalDateTime.now().atOffset(ZoneOffset.UTC)) + " Tagen auf " + message.getGuild().getName())
                .build()).queue();
    }
}
