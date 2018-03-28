package de.c0debase.bot.tempchannel;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;

/**
 * @author Biosphere
 * @date 27.03.18
 */
public class Tempchannel implements TempchannelEvents {

    private TextChannel textChannel;

    public Tempchannel() {
    }

    public Tempchannel(TextChannel textChannel) {
        this.textChannel = textChannel;
    }

    @Override
    public void onTempchannelJoin(VoiceChannel voiceChannel, Member member) {
        if (textChannel == null) {
            textChannel = (TextChannel) voiceChannel.getParent().createTextChannel("temp-" + voiceChannel.getName().toLowerCase()).complete();
            textChannel.createPermissionOverride(member.getGuild().getSelfMember()).setAllow(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE).complete();
            textChannel.createPermissionOverride(member).setAllow(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE).complete();
            textChannel.createPermissionOverride(textChannel.getGuild().getPublicRole()).setDeny(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE).complete();
        } else {
            if (textChannel.getPermissionOverride(member) != null) {
                textChannel.getPermissionOverride(member).getManager().grant(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE).queue();
            } else {
                textChannel.createPermissionOverride(member).setAllow(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE).queue();
            }
        }
    }

    @Override
    public void onTempchannelLeave(VoiceChannel voiceChannel, Member member) {
        if (voiceChannel.getMembers().isEmpty()) {
            textChannel.delete().queue();
            textChannel = null;
        } else if (textChannel.getPermissionOverride(member) != null) {
            textChannel.getPermissionOverride(member).delete().queue();
        }
    }

    @Override
    public void onLoad(TextChannel textChannel, VoiceChannel voiceChannel) {
        if (voiceChannel.getMembers().isEmpty()) {
            textChannel.delete().queue();
            textChannel = null;
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
}
