package de.c0debase.bot.commands.music;

import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.commands.Command;
import de.c0debase.bot.utils.StringUtils;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class PlayCommand extends Command {

    public PlayCommand() {
        super("play", "Spiele etwas Musik", Category.MUSIC, "add");
    }

    @Override
    public void execute(String[] args, Message msg) {
        if (msg.getMember().getVoiceState().inVoiceChannel()) {
            if (msg.getGuild().getAudioManager().getConnectedChannel() == null) {
                msg.getGuild().getAudioManager().openAudioConnection(msg.getMember().getVoiceState().getChannel());
            }
            if (args.length < 1) {
                msg.getTextChannel().sendMessage(getEmbed(msg.getGuild(), msg.getAuthor()).setDescription("!play <URL|Titel").build()).queue();
            } else {
                CodebaseBot.getInstance().getMusicManager().setPaused(msg.getGuild(), false);
                if (StringUtils.extractUrls(args[0]).isEmpty()) {
                    CodebaseBot.getInstance().getMusicManager().loadTrack(msg.getTextChannel(), msg.getMember().getUser(), "ytsearch:" + String.join(" ", args));
                } else {
                    CodebaseBot.getInstance().getMusicManager().loadTrack(msg.getTextChannel(), msg.getMember().getUser(), args[0]);
                }
            }
        } else {
            msg.getTextChannel().sendMessage(getEmbed(msg.getGuild(), msg.getAuthor()).setDescription("Du bist in keinem Voicechannel").build()).queue();
        }
    }
}
