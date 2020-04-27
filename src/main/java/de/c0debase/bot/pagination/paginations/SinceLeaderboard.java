package de.c0debase.bot.pagination.paginations;

import com.vdurmont.emoji.EmojiManager;
import de.c0debase.bot.pagination.Pagination;
import de.c0debase.bot.utils.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SinceLeaderboard extends Pagination {

    public SinceLeaderboard() {
        super("Since Leaderboard:");
    }

    @Override
    public void update(Message success, MessageEmbed messageEmbed, String emote) {

        final String[] strings = messageEmbed.getFooter().getText().replace("Seite: (", "").replace(")", "").replace(" Sortierung: ", "/").split("/");

        int current = Integer.parseInt(strings[0]);
        if (emote.equalsIgnoreCase("arrow_left") && current == 1) {
            return;
        }
        final int max = Integer.parseInt(strings[1]);
        final boolean descending = strings[2].equalsIgnoreCase("absteigend");

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(success.getGuild().getSelfMember().getColor());
        embedBuilder.setTitle(getTitle());


        if (max != current) {
            if (emote.equalsIgnoreCase("arrow_right")) {
                current++;
            } else if (emote.equalsIgnoreCase("arrow_left") && current > 1) {
                current--;
            }
        } else if (emote.equalsIgnoreCase("arrow_left") && current > 1) {
            current--;
        }

        if (current > 0) {
            buildList(embedBuilder, current, descending);
            embedBuilder.setFooter("Seite: (" + current + "/" + max + ") Sortierung: " + (descending ? "absteigend" : "aufsteigend"), getBot().getGuild().getIconUrl());
            success.editMessage(embedBuilder.build()).queue();
        }
    }

    @Override
    public void createFirst(boolean descending, TextChannel textChannel) {
        descending = !descending;
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(getBot().getGuild().getSelfMember().getColor());
        embedBuilder.setTitle(getTitle());

        buildList(embedBuilder, 1, descending);

        embedBuilder.setFooter("Seite: (1/" + ((getBot().getGuild().getMembers().size() / getPageSize()) + 1) + ") Sortierung: " + (descending ? "absteigend" : "aufsteigend"), getBot().getGuild().getIconUrl());

        textChannel.sendMessage(embedBuilder.build()).queue((Message success) -> {
            success.addReaction(EmojiManager.getForAlias("arrow_left").getUnicode()).queue();
            success.addReaction(EmojiManager.getForAlias("arrow_right").getUnicode()).queue();
        });
    }

    @Override
    public void buildList(EmbedBuilder embedBuilder, int page, boolean descending) {
        final List<Member> users = getSortedMembers();
        if (!descending) Collections.reverse(users);
        descending = !descending;
        for (Map.Entry<Integer, Member> entry : getPage(page, users, descending).entrySet()) {
            Member member = entry.getValue();
            int count = entry.getKey();
            if (member != null) {
                long days = ChronoUnit.DAYS.between(member.getTimeJoined(), LocalDateTime.now().atOffset(ZoneOffset.UTC));
                embedBuilder.appendDescription("`" + count + ")` " + StringUtils.replaceCharacter(member.getEffectiveName()) + "#" + member.getUser().getDiscriminator() + " (Beitritt am " + member.getTimeJoined().toInstant().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) + " / Seit " + days + " Tag" + (days == 1 ? "" : "en") + ")\n");
            } else {
                embedBuilder.appendDescription("`" + count + ")` undefined#0000\n");
            }
            count++;
        }
    }

    private List<Member> getSortedMembers() {
        final List<Member> members = new LinkedList<>();
        getBot().getGuild().getMembers().forEach(member -> {
            if(member.getTimeJoined().equals(member.getGuild().getTimeCreated()) && !member.isOwner()){
                members.add(member.getGuild().retrieveMemberById(member.getId(), true).complete());
            } else {
                members.add(member);
            }
        });
        members.sort((m1, m2) -> Long.compare(m2.getTimeJoined().toInstant().toEpochMilli(), m1.getTimeJoined().toInstant().toEpochMilli()));
        return members;
    }
}