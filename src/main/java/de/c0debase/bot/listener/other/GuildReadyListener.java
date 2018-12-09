package de.c0debase.bot.listener.other;

import de.c0debase.bot.core.Codebase;
import de.c0debase.bot.tempchannel.Tempchannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.GuildReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildReadyListener extends ListenerAdapter {

    private final Codebase bot;

    public GuildReadyListener(final Codebase bot) {
        this.bot = bot;
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        for (VoiceChannel voiceChannel : event.getGuild().getVoiceChannels()) {
            final String name = ("temp-" + voiceChannel.getName().toLowerCase()).replaceAll("\\s+", "-");
            final TextChannel textChannel = voiceChannel.getGuild().getTextChannelsByName(name, true).isEmpty() ? null : voiceChannel.getGuild().getTextChannelsByName(name, true).get(0);
            if (textChannel == null) {
                bot.getTempchannels().put(voiceChannel.getId(), new Tempchannel());
            } else {
                Tempchannel tempchannel = new Tempchannel(textChannel);
                tempchannel.onLoad(textChannel, voiceChannel);
                bot.getTempchannels().put(voiceChannel.getId(), tempchannel);
            }
        }
    }

}