package de.c0debase.bot.commands.music;

import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.commands.Command;
import de.c0debase.bot.utils.StringUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class PlayCommand extends Command {

    public PlayCommand() {
        super("play", "Spiele etwas Musik", Categorie.MUSIC);
    }

    @Override
    public void execute(String[] args, Message msg) {

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setFooter(msg.getAuthor().getName(), msg.getAuthor().getEffectiveAvatarUrl());
        embedBuilder.setAuthor("Command: " + getCommand(), null, msg.getGuild().getIconUrl());
        embedBuilder.setColor(msg.getGuild().getSelfMember().getColor());

        if (msg.getMember().getVoiceState() != null && msg.getMember().getVoiceState().inVoiceChannel()) {
            if (msg.getGuild().getAudioManager().getConnectedChannel() == null) {
                msg.getGuild().getAudioManager().openAudioConnection(msg.getMember().getVoiceState().getChannel());
            }
            if (args.length < 1) {
                embedBuilder.setDescription("!play <URL|Titel");
                msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
            } else {
                CodebaseBot.getInstance().getMusicManager().setPaused(msg.getGuild(), false);
                if (StringUtils.extractUrls(args[0]).isEmpty()) {
                    CodebaseBot.getInstance().getMusicManager().loadTrack(msg.getTextChannel(), msg.getMember().getUser(), "ytsearch:" + String.join(" ", args));
                } else {
                    CodebaseBot.getInstance().getMusicManager().loadTrack(msg.getTextChannel(), msg.getMember().getUser(), args[0]);
                }
            }
        } else {
            embedBuilder.setDescription("Du bist in keinem Voicechannel ^^");
            msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
        }
    }
}
