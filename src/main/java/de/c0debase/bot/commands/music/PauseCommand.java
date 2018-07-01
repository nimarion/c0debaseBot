package de.c0debase.bot.commands.music;

import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class PauseCommand extends Command {

    public PauseCommand() {
        super("pause", "Pausiert einen Track", Category.MUSIC);
    }

    @Override
    public void execute(String[] args, Message msg) {
        EmbedBuilder embedBuilder = getEmbed(msg.getGuild(), msg.getAuthor());
        if (msg.getMember().getVoiceState().inVoiceChannel() && msg.getMember().getVoiceState().getChannel().getMembers().contains(msg.getGuild().getSelfMember())) {
            boolean paused = CodebaseBot.getInstance().getMusicManager().isPaused(msg.getGuild());
            CodebaseBot.getInstance().getMusicManager().setPaused(msg.getGuild(), !paused);
            String pausedString = !paused ? "pausiert" : "fortgesetzt";
            embedBuilder.addField("Track " + pausedString, "`Der aktuelle Track wurde " + pausedString + "`", false);
        } else {
            embedBuilder.setDescription("Du bist in keinem Voicechannel");
        }
        msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }
}
