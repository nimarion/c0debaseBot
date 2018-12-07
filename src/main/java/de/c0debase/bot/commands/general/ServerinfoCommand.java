package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.core.Codebase;
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
    public void execute(final Codebase bot, final String[] args, final Message message) {
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(message.getGuild().getName(), "https://c0debase.de");
        embedBuilder.setThumbnail(message.getGuild().getIconUrl());
        embedBuilder.setFooter(StringUtils.replaceCharacter(message.getAuthor().getName()), message.getAuthor().getEffectiveAvatarUrl());
        embedBuilder.addField("Created", message.getGuild().getCreationTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), true);
        embedBuilder.addField("Region", message.getGuild().getRegionRaw(), true);
        embedBuilder.addField("Users", String.valueOf(message.getGuild().getMembers().size()), true);
        embedBuilder.addField("Text Channels", String.valueOf(message.getGuild().getTextChannels().size()), true);
        embedBuilder.addField("Voice Channels", String.valueOf(message.getGuild().getVoiceChannels().size()), true);
        embedBuilder.addField("Roles", String.valueOf(message.getGuild().getRoles().size()), true);
        embedBuilder.addField("Owner", StringUtils.replaceCharacter(message.getGuild().getOwner().getUser().getName()) + "#" + message.getGuild().getOwner().getUser().getDiscriminator(), true);
        embedBuilder.addField("Uptime", String.valueOf(ChronoUnit.DAYS.between(message.getGuild().getCreationTime(), LocalDateTime.now().atOffset(ZoneOffset.UTC))), true);

        message.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }
}
