package de.c0debase.bot.commands.music;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class LeaveCommand extends Command {

    public LeaveCommand() {
        super("leave", "Verlässt einen Voicechannel", Categorie.MUSIC, "disconnect");
    }

    @Override
    public void execute(String[] args, Message msg) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setFooter(msg.getAuthor().getName(), msg.getAuthor().getEffectiveAvatarUrl());
        embedBuilder.setAuthor("Command: " + getCommand(), null, msg.getGuild().getIconUrl());
        embedBuilder.setColor(msg.getGuild().getSelfMember().getColor());

        if (msg.getGuild().getAudioManager().getConnectedChannel() != null) {
            embedBuilder.addField("Verbindung getrennt", "`Channel " + msg.getGuild().getAudioManager().getConnectedChannel().getName() + " verlassen`", false);
        } else {
            embedBuilder.setDescription("Du bist in keinem Voicechannel");
        }

        msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
        msg.getGuild().getAudioManager().closeAudioConnection();
    }
}
