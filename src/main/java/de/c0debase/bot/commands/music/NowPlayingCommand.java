package de.c0debase.bot.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author Biosphere
 * @date 20.02.18
 */
public class NowPlayingCommand extends Command {

    public NowPlayingCommand() {
        super("nowplaying", "Zeigt den Namen des aktuellen Tracks", Categorie.MUSIC, "np");
    }

    @Override
    public void execute(String[] args, Message msg) {
        EmbedBuilder embedBuilder = getEmbed(msg.getGuild(), msg.getAuthor());
        if (CodebaseBot.getInstance().getMusicManager().getPlayingTrack(msg.getGuild()) != null) {
            AudioTrack audioTrack = CodebaseBot.getInstance().getMusicManager().getPlayingTrack(msg.getGuild());
            embedBuilder.setDescription("**" + audioTrack.getInfo().title + "**");
        } else {
            embedBuilder.setDescription("Es wird gerade nichts gespielt");
        }
        msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }
}
