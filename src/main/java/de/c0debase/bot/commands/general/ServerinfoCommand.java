package de.c0debase.bot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.c0debase.bot.utils.EmbedUtils;
import de.c0debase.bot.utils.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ServerinfoCommand extends Command {

    public ServerinfoCommand() {
        this.name = "serverinfo";
        this.help = "Listet einige Informationen über den Server";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        final EmbedBuilder embedBuilder = EmbedUtils.getEmbed(commandEvent.getAuthor(), true);
        final Message message = commandEvent.getMessage();
        embedBuilder.setTitle(message.getGuild().getName(), "https://c0debase.de");
        embedBuilder.setThumbnail(message.getGuild().getIconUrl());
        embedBuilder.addField("Erstellt am", message.getGuild().getTimeCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), true);
        embedBuilder.addField("Region", message.getGuild().getRegionRaw(), true);
        embedBuilder.addField("Mitglieder", String.valueOf(message.getGuild().getMembers().size()), true);
        embedBuilder.addField("Text Channels", String.valueOf(message.getGuild().getTextChannels().size()), true);
        embedBuilder.addField("Voice Channels", String.valueOf(message.getGuild().getVoiceChannels().size()), true);
        embedBuilder.addField("Rollen", String.valueOf(message.getGuild().getRoles().size()), true);
        embedBuilder.addField("Owner", StringUtils.replaceCharacter(message.getGuild().getOwner().getUser().getName()) + "#" + message.getGuild().getOwner().getUser().getDiscriminator(), true);
        embedBuilder.addField("Erstellt vor", ChronoUnit.DAYS.between(message.getGuild().getTimeCreated(), LocalDateTime.now().atOffset(ZoneOffset.UTC)) + " Tagen", true);

        commandEvent.reply(embedBuilder.build());
    }
}
