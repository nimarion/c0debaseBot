package de.c0debase.bot.pagination.paginations;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.c0debase.bot.pagination.Pagination;
import de.c0debase.bot.utils.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class NitroLeaderboard extends Pagination {

    public NitroLeaderboard() {
        super("Nitro Leaderboard:");
    }

    @Override
    public void buildList(EmbedBuilder embedBuilder, int page, boolean descending, Guild guild) {
        final List<Member> users = getSortedNitroBoosters(guild);
        if (!descending)
            Collections.reverse(users);
        for (Map.Entry<Integer, Member> entry : getPage(page, users, descending).entrySet()) {
            Member member = entry.getValue();
            int count = entry.getKey();
            if (member != null) {
                long days = ChronoUnit.DAYS.between(member.getTimeBoosted(),
                        LocalDateTime.now().atOffset(ZoneOffset.UTC));
                embedBuilder
                        .appendDescription("`" + count + ")` " + StringUtils.replaceCharacter(member.getEffectiveName())
                                + "#" + member.getUser().getDiscriminator() + " (Beitritt am "
                                + member.getTimeBoosted().toInstant().atOffset(ZoneOffset.UTC)
                                        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                                + " / Seit " + days + " Tag" + (days == 1 ? "" : "en") + ")\n");
            } else {
                embedBuilder.appendDescription("`" + count + ")` undefined#0000\n");
            }
            count++;
        }
    }

    @Override
    public EmbedBuilder getEmbed(final Guild guild, final boolean descending) {
        final EmbedBuilder embedBuilder = getEmbed(guild);
        embedBuilder.setFooter("Seite: (1/" + ((getSortedNitroBoosters(guild).size() / getPageSize()) + 1)
                + ") Sortierung: " + (descending ? "absteigend" : "aufsteigend"), guild.getIconUrl());
        return embedBuilder;
    }

    public List<Member> getSortedNitroBoosters(final Guild guild) {
        final List<Member> members = new LinkedList<>();
        members.addAll(guild.getMembers().stream().filter(member -> member.getTimeBoosted() != null)
                .collect(Collectors.toList()));
        members.sort((m1, m2) -> Long.compare(m2.getTimeBoosted().toInstant().toEpochMilli(),
                m1.getTimeBoosted().toInstant().toEpochMilli()));
        return members;
    }

}