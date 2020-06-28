package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.utils.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SinceCommand extends Command {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    public SinceCommand() {
        super("since", "Zeigt wie lang du schon auf dem Server bist", Category.GENERAL);
    }

    @Override
    public void execute(final String[] args, final Message message) {
        final Member member = message.getMentionedMembers().size() == 0 ? message.getMember()
                : message.getMentionedMembers().get(0);
        final EmbedBuilder embedBuilder = getEmbed(message.getGuild(), message.getAuthor());
        if (args.length > 0 && message.getMentionedMembers().isEmpty() && StringUtils.isInteger(args[0]) && message.getGuild().isLoaded()){
            final Integer since = Integer.valueOf(args[0]);
            if(since < 0){
                embedBuilder.setDescription("!since [0-3000]");
            } else {
                final Integer memberCount = getMemberCoundByDays(since, message.getGuild());
                final float percentage = ((float) memberCount / (float) message.getGuild().getMemberCount()) * 100;
                embedBuilder.setDescription("Es gibt " + memberCount + " Mitglieder welche seit mehr als " + since
                        + " Tagen auf diesem Server sind.\n Das sind " + DECIMAL_FORMAT.format(percentage) + "%");
            }
        } else {
            embedBuilder.setDescription(member.getAsMention() + " ist seit "
                    + ChronoUnit.DAYS.between(member.getTimeJoined(), LocalDateTime.now().atOffset(ZoneOffset.UTC))
                    + " Tagen auf " + message.getGuild().getName());
        }
        message.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }

    private Integer getMemberCoundByDays(final Integer since, final Guild guild) {
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        guild.getMembers().forEach(member -> {
            if (member.hasTimeJoined() && ChronoUnit.DAYS.between(member.getTimeJoined(),
                    LocalDateTime.now().atOffset(ZoneOffset.UTC)) > since) {
                atomicInteger.incrementAndGet();
            } else {
                final Member requestedMember = guild.retrieveMemberById(member.getId(), true).complete();
                if (ChronoUnit.DAYS.between(requestedMember.getTimeJoined(),
                        LocalDateTime.now().atOffset(ZoneOffset.UTC)) > since) {
                    atomicInteger.incrementAndGet();
                }
            }
        });
        return atomicInteger.get();
    }
}
