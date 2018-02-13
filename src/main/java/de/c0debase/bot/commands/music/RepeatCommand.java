package de.c0debase.bot.commands.music;

import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class RepeatCommand extends Command {

    public RepeatCommand() {
        super("repeat", "Wiederhole alle Tracks", Categorie.MUSIC);
    }

    @Override
    public void execute(String[] args, Message msg) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setFooter(msg.getAuthor().getName(), msg.getAuthor().getEffectiveAvatarUrl());
        embedBuilder.setAuthor("Command: " + getCommand(), null, msg.getGuild().getIconUrl());
        embedBuilder.setColor(msg.getGuild().getSelfMember().getColor());
        if (msg.getMember().getVoiceState() != null && msg.getMember().getVoiceState().inVoiceChannel()) {
            boolean repeat = CodebaseBot.getInstance().getMusicManager().isRepeat(msg.getGuild());
            CodebaseBot.getInstance().getMusicManager().setRepeat(msg.getGuild(), !repeat);
            String pausedString = !repeat ? "an" : "aus";
            embedBuilder.setDescription("Wiederholung " + pausedString);
        } else {
            embedBuilder.setDescription("Du bist in keinem Voicechannel");
        }
        msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }
}
