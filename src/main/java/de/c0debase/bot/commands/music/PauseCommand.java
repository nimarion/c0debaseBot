package de.c0debase.bot.commands.music;

import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class PauseCommand extends Command {

    public PauseCommand() {
        super("pause", "Pausiert einen Track", Categorie.MUSIC);
    }

    @Override
    public void execute(String[] args, Message msg) {
        TextChannel channel = msg.getTextChannel();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setFooter(msg.getAuthor().getName(), msg.getAuthor().getEffectiveAvatarUrl());
        embedBuilder.setAuthor("Command: " + getCommand(), null, msg.getGuild().getIconUrl());
        embedBuilder.setColor(msg.getGuild().getSelfMember().getColor());
        if (msg.getMember().getVoiceState() != null && msg.getMember().getVoiceState().inVoiceChannel()) {
            boolean paused = CodebaseBot.getInstance().getMusicManager().isPaused(msg.getGuild());
            CodebaseBot.getInstance().getMusicManager().setPaused(msg.getGuild(), !paused);
            String pausedString = !paused ? "pausiert" : "fortgesetzt";
            embedBuilder.addField("Track " + pausedString, "`Der aktuelle Track wurde " + pausedString + ".`", false);
        } else {
            embedBuilder.setDescription("Du bist in keinem Voicechannel");
        }
        channel.sendMessage(embedBuilder.build()).queue();
    }
}
