package de.c0debase.bot.tempchannel;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;

/**
 * @author Biosphere
 * @date 27.03.18
 */
public interface TempchannelEvents {

    void onTempchannelJoin(VoiceChannel voiceChannel, Member member);

    void onTempchannelLeave(VoiceChannel voiceChannel, Member member);

    void onLoad(TextChannel textChannel, VoiceChannel voiceChannel);

}
