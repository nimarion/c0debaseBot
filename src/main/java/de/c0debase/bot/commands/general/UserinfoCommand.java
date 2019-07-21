package de.c0debase.bot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.c0debase.bot.utils.EmbedUtils;
import de.c0debase.bot.utils.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.time.format.DateTimeFormatter;

public class UserinfoCommand extends Command {

    public UserinfoCommand() {
        this.name = "userinfo";
        this.help = "Zeigt ein paar Infos über einen Nutzer";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        final Message message = commandEvent.getMessage();
        final Member member = message.getMentionedMembers().size() == 0 ? message.getMember() : ((message.getMentionedMembers().get(0).getUser().isBot()) ? message.getMember() : message.getMentionedMembers().get(0));

        final EmbedBuilder embedBuilder = EmbedUtils.getEmbed(commandEvent.getAuthor(), true);
        embedBuilder.setThumbnail(member.getUser().getAvatarUrl());
        embedBuilder.addField("Name", StringUtils.replaceCharacter(member.getUser().getName()), true);
        embedBuilder.addField("Nickname", member.getNickname() == null ? StringUtils.replaceCharacter(member.getUser().getName()) : StringUtils.replaceCharacter(member.getNickname()), true);
        embedBuilder.addField("Status", member.getOnlineStatus().getKey(), true);
        embedBuilder.addField("Spiel", member.getActivities().isEmpty() ? "---" : member.getActivities().get(0).getName(), true);
        embedBuilder.addField("Rollen", String.valueOf(member.getRoles().size()), true);
        embedBuilder.addField("Beitritt", member.getTimeJoined().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), true);
        embedBuilder.addField("Erstelldatum: ", member.getUser().getTimeCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), true);
        embedBuilder.addField("Standart Avatar: ", String.valueOf(member.getUser().getAvatarUrl() == null), true);

        commandEvent.reply(embedBuilder.build());
    }

}
