package de.c0debase.bot.utils;

import java.awt.Color;

import com.vdurmont.emoji.EmojiManager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;

public class DiscordUtils {

    public static Member getOldestMember(final Guild guild) {
        Member oldestMember = guild.getMembers().get(0);
        for (Member member : guild.getMembers()) {
            if (member.hasTimeJoined() && member.getTimeJoined().isBefore(oldestMember.getTimeJoined())) {
                oldestMember = member;
            } else {
                final Member requestedMember = guild.retrieveMemberById(member.getId(), true).complete();
                if (requestedMember.getTimeJoined().isBefore(oldestMember.getTimeJoined())) {
                    oldestMember = requestedMember;
                }
            }
        }
        return oldestMember;
    }

    public static String getReaction(final MessageReaction.ReactionEmote emote) {
        try {
            return EmojiManager.getByUnicode(emote.getName()).getAliases().get(0);
        } catch (Exception e) {
            return emote.getName();
        }
    }

    public static Member getAddressedMember(final Message message) {
        return message.getMentionedMembers().isEmpty() ? message.getMember()
                : ((message.getMentionedMembers().get(0).getUser().isBot()) ? message.getMember()
                        : message.getMentionedMembers().get(0));
    }

    public static EmbedBuilder getDefaultEmbed(final User user) {
        return new EmbedBuilder()
                .setFooter("@" + user.getName() + "#" + user.getDiscriminator(), user.getEffectiveAvatarUrl())
                .setColor(Color.GREEN);
    }

    public static EmbedBuilder getDefaultEmbed(final Member member) {
        return getDefaultEmbed(member.getUser()).setColor(member.getGuild().getSelfMember().getColor());
    }

}