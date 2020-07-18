package de.c0debase.bot.tempchannel;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public interface TempchannelEvents {

    void onTempchannelJoin(final VoiceChannel voiceChannel, final Member member);

    void onTempchannelLeave(final VoiceChannel voiceChannel, final Member member);

    void onLoad(final TextChannel textChannel, final VoiceChannel voiceChannel);

}
