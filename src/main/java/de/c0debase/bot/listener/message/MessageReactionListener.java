package de.c0debase.bot.listener.message;

import com.vdurmont.emoji.EmojiManager;
import de.c0debase.bot.core.Codebase;
import de.c0debase.bot.database.data.CodebaseUser;
import de.c0debase.bot.utils.Pagination;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageReactionListener extends ListenerAdapter {

    private final Codebase bot;

    public MessageReactionListener(final Codebase bot) {
        this.bot = bot;
        bot.getJDA().addEventListener(this);
    }

    @Override
    public void onGenericGuildMessageReaction(final GenericGuildMessageReactionEvent event) {
        if (event.getUser().isBot()) {
            return;
        }
        event.getChannel().getMessageById(event.getMessageId()).queue(success -> {
            final String emote = getReaction(event.getReactionEmote());
            if (emote == null) {
                return;
            }
            if (emote.equalsIgnoreCase("wastebasket") && success.getAuthor().isBot()) {
                success.delete().queue();
                return;
            }
            if (!success.getEmbeds().isEmpty() && success.getAuthor().isBot()) {
                final MessageEmbed messageEmbed = success.getEmbeds().get(0);
                if (messageEmbed.getFooter() != null && messageEmbed.getFooter().getText().contains("Seite")) {
                    final Pagination leaderboard = bot.getDataManager().getLeaderboard(success.getGuild().getId());
                    final String[] strings = messageEmbed.getFooter().getText().replace("Seite: (", "").replace(")", "").split("/");

                    final int max = Integer.valueOf(strings[1]);
                    int current = Integer.valueOf(strings[0]);

                    final EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(success.getGuild().getSelfMember().getColor());
                    embedBuilder.setTitle("Leaderboard: " + event.getGuild().getName());

                    if (max != current) {
                        if (emote.equalsIgnoreCase("arrow_right")) {
                            current++;
                        } else if (emote.equalsIgnoreCase("arrow_left") && current > 1) {
                            current--;
                        }
                    } else if (emote.equalsIgnoreCase("arrow_left") && current >= 1) {
                        current--;
                    }

                    embedBuilder.setFooter("Seite: (" + current + "/" + max + ")", success.getGuild().getIconUrl());

                    int count = 1;
                    if (current > 0) {
                        for (CodebaseUser codebaseUser : leaderboard.getPage(current)) {
                            final Member member = success.getGuild().getMemberById(Long.valueOf(codebaseUser.getUserID()));
                            if (member != null) {
                                embedBuilder.appendDescription("`" + (current == 1 ? count : +((current - 1) * 10 + count)) + ")` " + member.getEffectiveName() + "#" + member.getUser().getDiscriminator() + " (Lvl." + codebaseUser.getLevel() + ")\n");
                                count++;
                            }
                        }
                        success.editMessage(embedBuilder.build()).queue();
                    }
                }
            }
        });
    }

    private String getReaction(final MessageReaction.ReactionEmote emote) {
        try {
            return EmojiManager.getByUnicode(emote.getName()).getAliases().get(0);
        } catch (Exception e) {
            return emote.getName();
        }
    }
}

