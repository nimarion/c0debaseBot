package de.c0debase.bot.commands.music;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class JoinCommand extends Command {

    public JoinCommand() {
        super("join", "Betritt einen Voicechannel", Categorie.MUSIC, "summon");
    }

    @Override
    public void execute(String[] args, Message msg) {
        EmbedBuilder embedBuilder = getEmbed(msg.getGuild(), msg.getAuthor());

        AudioManager audioManager = msg.getGuild().getAudioManager();
        if (audioManager.isConnected() || audioManager.isAttemptingToConnect()) {
            embedBuilder.setDescription("Der Bot ist bereits verbunden");
        } else if (!msg.getMember().getVoiceState().inVoiceChannel()) {
            embedBuilder.setDescription("Dafür musst du in einem Voicechannel sein.");
        } else {
            VoiceChannel channel = msg.getMember().getVoiceState().getChannel();
            msg.getGuild().getAudioManager().openAudioConnection(channel);
            embedBuilder.setDescription("Channel" + channel.getName() + " betreten");
        }
        msg.getTextChannel().sendMessage(embedBuilder.build()).queue();

    }
}
