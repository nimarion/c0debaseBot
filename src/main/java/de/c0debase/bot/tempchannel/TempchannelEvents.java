package de.c0debase.bot.tempchannel;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;

/**
 * @author Biosphere
 * @date 27.03.18
 */
public interface TempchannelEvents {

    void onTempchannelJoin(final VoiceChannel voiceChannel, final Member member);

    void onTempchannelLeave(final VoiceChannel voiceChannel, final Member member);

    void onLoad(final TextChannel textChannel, final VoiceChannel voiceChannel);

}
