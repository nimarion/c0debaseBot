package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.utils.StringUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class ServerinfoCommand extends Command {

    public ServerinfoCommand() {
        super("serverinfo", "Listet einige Informationen über den Server", Category.GENERAL);
    }

    @Override
    public void execute(String[] args, Message msg) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(msg.getGuild().getName(), "https://c0debase.de");
        embedBuilder.setThumbnail(msg.getGuild().getIconUrl());
        embedBuilder.setFooter(StringUtils.replaceCharacter(msg.getAuthor().getName()), msg.getAuthor().getEffectiveAvatarUrl());
        embedBuilder.addField("Created", msg.getGuild().getCreationTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), true);
        embedBuilder.addField("Region", msg.getGuild().getRegionRaw(), true);
        embedBuilder.addField("Users", String.valueOf(msg.getGuild().getMembers().size()), true);
        embedBuilder.addField("Text Channels", String.valueOf(msg.getGuild().getTextChannels().size()), true);
        embedBuilder.addField("Voice Channels", String.valueOf(msg.getGuild().getVoiceChannels().size()), true);
        embedBuilder.addField("Roles", String.valueOf(msg.getGuild().getRoles().size()), true);
        embedBuilder.addField("Owner", StringUtils.replaceCharacter(msg.getGuild().getOwner().getUser().getName()) + "#" + msg.getGuild().getOwner().getUser().getDiscriminator(), true);
        embedBuilder.addField("Uptime", String.valueOf(ChronoUnit.DAYS.between(msg.getGuild().getCreationTime(), LocalDateTime.now().atOffset(ZoneOffset.UTC))), true);

        msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }
}
