package de.c0debase.bot.commands.music;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
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
        Guild guild = msg.getGuild();
        AudioManager audioManager = guild.getAudioManager();
        Member member = msg.getMember();
        TextChannel textChannel = msg.getTextChannel();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setFooter(msg.getAuthor().getName(), msg.getAuthor().getEffectiveAvatarUrl());
        embedBuilder.setAuthor("Command: " + getCommand(), null, msg.getGuild().getIconUrl());
        embedBuilder.setColor(msg.getGuild().getSelfMember().getColor());

        if (audioManager.isConnected() || audioManager.isAttemptingToConnect()) {
            embedBuilder.setDescription("Der Bot ist bereits verbunden");
        } else if (!member.getVoiceState().inVoiceChannel()) {
            embedBuilder.setDescription("Dafür musst du in einem Voicechannel sein.");
        } else {
            VoiceChannel channel = member.getVoiceState().getChannel();
            guild.getAudioManager().openAudioConnection(channel);
            embedBuilder.setDescription("Channel" + channel.getName() + " betreten");
        }
        textChannel.sendMessage(embedBuilder.build()).queue();

    }
}
