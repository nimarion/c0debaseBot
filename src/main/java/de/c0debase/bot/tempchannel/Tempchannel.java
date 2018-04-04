package de.c0debase.bot.tempchannel;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.Arrays;
import java.util.List;

/**
 * @author Biosphere
 * @date 27.03.18
 */
public class Tempchannel implements TempchannelEvents {
    private static final List<Permission> MEMBER_PERMISSIONS;

    static {
        MEMBER_PERMISSIONS = Arrays.asList(Permission.MESSAGE_WRITE, Permission.MESSAGE_READ);
    }

    private TextChannel textChannel;

    public Tempchannel() {
    }

    public Tempchannel(TextChannel textChannel) {
        this.textChannel = textChannel;
    }

    @Override
    public void onTempchannelJoin(VoiceChannel voiceChannel, Member member) {
        if (voiceChannel.equals(voiceChannel.getGuild().getAfkChannel())) {
            return;
        }
        if (textChannel == null) {
            final Guild guild = voiceChannel.getGuild();
            voiceChannel.getParent().createTextChannel("temp-" + voiceChannel.getName().toLowerCase())
                    .addPermissionOverride(guild.getSelfMember(), MEMBER_PERMISSIONS, null)
                    .addPermissionOverride(member, MEMBER_PERMISSIONS, null)
                    .addPermissionOverride(guild.getPublicRole(), null, MEMBER_PERMISSIONS)
                    .queue(channel -> setTextChannel((TextChannel) channel));
        } else {
            if (textChannel.getPermissionOverride(member) != null) {
                textChannel.getPermissionOverride(member).getManager().grant(MEMBER_PERMISSIONS).queue();
            } else {
                textChannel.createPermissionOverride(member).setAllow(MEMBER_PERMISSIONS).queue();
            }
            if (member.getUser().isBot()) {
                return;
            }
            final EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(member.getColor());
            embedBuilder.setDescription(":arrow_right: " + member.getAsMention() + " ist beigetreten");
            textChannel.sendMessage(embedBuilder.build()).queue();
        }
    }

    @Override
    public void onTempchannelLeave(VoiceChannel voiceChannel, Member member) {
        if (voiceChannel.equals(voiceChannel.getGuild().getAfkChannel())) {
            return;
        }
        if (voiceChannel.getMembers().isEmpty()) {
            textChannel.delete().queue();
            textChannel = null;
        } else if (textChannel.getPermissionOverride(member) != null) {
            textChannel.getPermissionOverride(member).delete().queue();
            if (member.getUser().isBot()) {
                return;
            }
            final EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(member.getColor());
            embedBuilder.setDescription(":arrow_left: " + member.getAsMention() + " hat uns verlassen");
            textChannel.sendMessage(embedBuilder.build()).queue();
        }
    }

    @Override
    public void onLoad(TextChannel textChannel, VoiceChannel voiceChannel) {
        if (voiceChannel.getMembers().isEmpty()) {
            textChannel.delete().queue();
            setTextChannel(null);
            return;
        }

        for (Member member : textChannel.getMembers()) {
            if (member.hasPermission(Permission.MESSAGE_MANAGE)) {
                continue;
            }
            if (voiceChannel.getMembers().contains(member)) {
                if (textChannel.getPermissionOverride(member) != null) {
                    textChannel.getPermissionOverride(member).getManager().grant(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE).queue();
                } else {
                    textChannel.createPermissionOverride(member).setAllow(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE).queue();
                }
            } else {
                textChannel.getPermissionOverride(member).delete().queue();
            }
        }
    }

    private void setTextChannel(final TextChannel textChannel) {
        this.textChannel = textChannel;
    }
}
