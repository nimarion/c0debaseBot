package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.utils.DiscordUtils;
import de.c0debase.bot.utils.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.time.format.DateTimeFormatter;

public class UserinfoCommand extends Command {

        public UserinfoCommand() {
                super("userinfo", "Zeigt ein paar Infos über einen Nutzer", Category.GENERAL);
        }

        @Override
        public void execute(final String[] args, final Message message) {
                final Member member = DiscordUtils.getAddressedMember(message);

                final EmbedBuilder embedBuilder = getEmbed(message.getMember());
                embedBuilder.setThumbnail(member.getUser().getAvatarUrl());
                embedBuilder.addField("Name", StringUtils.replaceCharacter(member.getUser().getName()), true);
                embedBuilder.addField("Nickname",
                                member.getNickname() == null ? StringUtils.replaceCharacter(member.getUser().getName())
                                                : StringUtils.replaceCharacter(member.getNickname()),
                                true);
                embedBuilder.addField("Status", member.getOnlineStatus().getKey(), true);
                embedBuilder.addField("Spiel",
                                member.getActivities().isEmpty() ? "---" : member.getActivities().get(0).getName(),
                                true);
                embedBuilder.addField("Rollen", String.valueOf(member.getRoles().size()), true);
                embedBuilder.addField("Beitritt",
                                member.getTimeJoined().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), true);
                embedBuilder.addField("Erstelldatum: ", member.getUser().getTimeCreated()
                                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), true);
                embedBuilder.addField("Standard Avatar: ", String.valueOf(member.getUser().getAvatarUrl() == null),
                                true);

                message.getTextChannel().sendMessage(embedBuilder.build()).queue();
        }

}
