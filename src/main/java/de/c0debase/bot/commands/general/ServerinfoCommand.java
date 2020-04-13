package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.utils.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ServerinfoCommand extends Command {

    public ServerinfoCommand() {
        super("serverinfo", "Listet einige Informationen über den Server", Category.GENERAL);
    }

    @Override
    public void execute(final String[] args, final Message message) {
        final EmbedBuilder embedBuilder = getEmbed(message.getGuild(), message.getAuthor());
        embedBuilder.setTitle(message.getGuild().getName(), "https://c0debase.de");
        embedBuilder.setThumbnail(message.getGuild().getIconUrl());
        embedBuilder.addField("Erstellt am", message.getGuild().getTimeCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), true);
        embedBuilder.addField("Region", message.getGuild().getRegionRaw(), true);
        embedBuilder.addField("Mitglieder", String.valueOf(message.getGuild().getMembers().size()), true);
        embedBuilder.addField("Boosts", String.valueOf(message.getGuild().getBoostCount()), true);
        embedBuilder.addField("Text Channels", String.valueOf(message.getGuild().getTextChannels().size()), true);
        embedBuilder.addField("Voice Channels", String.valueOf(message.getGuild().getVoiceChannels().size()), true);
        embedBuilder.addField("Rollen", String.valueOf(message.getGuild().getRoles().size()), true);
        embedBuilder.addField("Owner", StringUtils.replaceCharacter(message.getGuild().getOwner().getUser().getName()) + "#" + message.getGuild().getOwner().getUser().getDiscriminator(), true);
        embedBuilder.addField("Erstellt vor", ChronoUnit.DAYS.between(message.getGuild().getTimeCreated(), LocalDateTime.now().atOffset(ZoneOffset.UTC)) + " Tagen", true);

        message.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }
}
