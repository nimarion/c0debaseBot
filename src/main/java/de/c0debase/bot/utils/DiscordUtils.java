package de.c0debase.bot.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class DiscordUtils {

    public static Member getOldestMember(final Guild guild){
        Member oldestMember = null;
        for(Member member : guild.getMembers()){
            if(oldestMember == null){
                oldestMember = member;
            } else if (member.hasTimeJoined() && member.getTimeJoined().isBefore(oldestMember.getTimeJoined())){
                oldestMember = member;
            } else {
                final Member requestedMember = guild.retrieveMemberById(member.getId(), true).complete();
                if(requestedMember.getTimeJoined().isBefore(oldestMember.getTimeJoined())){
                    oldestMember = requestedMember;
                }
            }
        }
        return oldestMember;
    }
    
}