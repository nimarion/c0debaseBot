package de.c0debase.bot.listener.message;

import com.vdurmont.emoji.EmojiManager;
import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.commands.Command;
import de.c0debase.bot.commands.Command.Categorie;
import de.c0debase.bot.level.LevelUser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.core.events.message.priv.react.GenericPrivateMessageReactionEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.awt.*;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class MessageReactionListener extends ListenerAdapter {

    @Override
    public void onGenericPrivateMessageReaction(GenericPrivateMessageReactionEvent event) {
        if (event.getUser().isBot()) {
            return;
        }
        event.getChannel().getMessageById(event.getMessageId()).queue((Message success) -> {
            final String emote = getReaction(event.getReactionEmote());
            if (emote == null) {
                return;
            }
            if (!success.getEmbeds().isEmpty() && success.getAuthor().isBot()) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Color.GREEN);
                if (emote.equalsIgnoreCase("wastebasket")) {
                    success.delete().queue();
                    return;
                }

                for (Categorie categorie : Categorie.values()) {
                    if (categorie.getEmote().equalsIgnoreCase(emote)) {
                        embedBuilder.setTitle(":question: " + categorie.getName() + " Commands Help");
                        for (Command command : CodebaseBot.getInstance().getCommandManager().getAvailableCommands()) {
                            if (command.getCategorie() == categorie) {
                                embedBuilder.appendDescription("**!" + command.getCommand() + "**\n" + command.getDescription() + "\n\n");
                            }
                        }
                        success.editMessage(embedBuilder.build()).queue();
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void onGenericGuildMessageReaction(GenericGuildMessageReactionEvent event) {
        if (event.getUser().isBot()) {
            return;
        }
        event.getChannel().getMessageById(event.getMessageId()).queue(success -> {
            if (event.getReactionEmote().getName().equalsIgnoreCase("wastebasket") && success.getAuthor().isBot()) {
                success.delete().queue();
                return;
            }
            if (!success.getEmbeds().isEmpty() && success.getAuthor().isBot()) {
                final String emote = getReaction(event.getReactionEmote());
                if (emote == null) {
                    return;
                }
                MessageEmbed messageEmbed = success.getEmbeds().get(0);
                if (messageEmbed.getFooter() != null && messageEmbed.getFooter().getText().contains("Seite")) {
                    CodebaseBot.getInstance().getLeaderboardPagination().updateList(CodebaseBot.getInstance().getLevelManager().getLevelUsersSorted());

                    String[] strings = messageEmbed.getFooter().getText().replace("Seite: (", "").replace(")", "").split("/");

                    int max = Integer.valueOf(strings[1]);
                    int current = Integer.valueOf(strings[0]);


                    EmbedBuilder embedBuilder = new EmbedBuilder();
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

                    for (LevelUser levelUser : CodebaseBot.getInstance().getLeaderboardPagination().getPage(current)) {
                        Member member = success.getGuild().getMemberById(Long.valueOf(levelUser.getId()));
                        if (member != null) {
                            embedBuilder.appendDescription("`" + (current == 1 ? count : +((current - 1) * 10 + count)) + ")` " + member.getEffectiveName() + "#" + member.getUser().getDiscriminator() + " (Lvl." + levelUser.getLevel() + ")\n");
                            count++;
                        }
                    }
                    success.editMessage(embedBuilder.build()).queue();
                }
            }
        });
    }

    @Override
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
        if (event.getMember().getUser().isBot()) {
            return;
        }
        event.getChannel().getMessageById(event.getMessageId()).queue(success -> {
            if (success.getTextChannel().getTopic() != null && success.getTextChannel().getTopic().endsWith("RS")) {
                final String emote = getReaction(event.getReactionEmote());
                if (emote == null) {
                    return;
                }
                Role role = success.getGuild().getRolesByName(emote, true).stream().findFirst().orElseGet(null);
                if (role != null && PermissionUtil.canInteract(event.getGuild().getSelfMember(), role) && success.getGuild().getMembersWithRoles(role).contains(event.getMember())) {
                    success.getGuild().getController().removeRolesFromMember(event.getMember(), role).queue();
                }
            }
        });
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        event.getChannel().getMessageById(event.getMessageId()).queue(success -> {
            if (success.getTextChannel().getTopic() != null && success.getTextChannel().getTopic().endsWith("RS")) {
                String emote = getReaction(event.getReactionEmote());
                Role role = success.getGuild().getRolesByName(emote, true).stream().findFirst().orElseGet(null);
                if (role != null && PermissionUtil.canInteract(event.getGuild().getSelfMember(), role) && !success.getGuild().getMembersWithRoles(role).contains(event.getMember())) {
                        success.getGuild().getController().addRolesToMember(event.getMember(), role).queue();
                }
            }
        });
    }

    private String getReaction(MessageReaction.ReactionEmote emote) {
        try {
            return EmojiManager.getByUnicode(emote.getName()).getAliases().get(0);
        } catch (Exception e) {
            return emote.getName();
        }
    }
}

