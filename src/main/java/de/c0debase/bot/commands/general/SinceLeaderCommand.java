package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.collections4.SortedBidiMap;


public class SinceLeaderCommand extends Command {

    private SortedBidiMap<Long, Member> sortedUsers;

    public SinceLeaderCommand() {
        super("sinceleader", "Listet die Discorduser anhand ihres Beitrittdatums zu diesem Discord.", Category.GENERAL);

    }

    @Override
    public void execute(String[] args, Message message) {
        /* final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(message.getGuild().getSelfMember().getColor());
        embedBuilder.setTitle("Since Leaderboard: " + message.getGuild().getName());

        final Pagination pagination = bot.getDataManager().getLeaderboard(message.getGuild().getId());
        int count = 1;
        for (CodebaseUser codebaseUser : pagination.getPage(1)) {
            final Member member = message.getGuild().getMemberById(Long.valueOf(codebaseUser.getUserID()));
            if (member != null) {
                embedBuilder.appendDescription("`" + count + ")` " + StringUtils.replaceCharacter(member.getEffectiveName()) + "#" + member.getUser().getDiscriminator() + " (Lvl." + codebaseUser.getLevel() + ")\n");
            } else {
                embedBuilder.appendDescription("`" + count + ")` undefined#0000 (Lvl." + codebaseUser.getLevel() + ")\n");
            }
            count++;
        }

        embedBuilder.setFooter("Seite: (1/" + ((message.getGuild().getMembers().size() / 10) + 1) + ")", message.getGuild().getIconUrl());

        message.getTextChannel().sendMessage(embedBuilder.build()).queue((Message success) -> {
            success.addReaction(EmojiManager.getForAlias("arrow_left").getUnicode()).queue();
            success.addReaction(EmojiManager.getForAlias("arrow_right").getUnicode()).queue();
        });*/
    }

}
