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
        EmbedBuilder embedBuilder = getEmbed(msg.getGuild(), msg.getAuthor());
        if (msg.getMember().getVoiceState().inVoiceChannel() && msg.getMember().getVoiceState().getChannel().getMembers().contains(msg.getGuild().getSelfMember())) {
            if (msg.getGuild().getAudioManager().getConnectedChannel() != null) {
                msg.getGuild().getAudioManager().closeAudioConnection();
                embedBuilder.addField("Verbindung getrennt", "`Channel " + msg.getGuild().getAudioManager().getConnectedChannel().getName() + " verlassen`", false);
            } else {
                embedBuilder.setDescription("Der Bot ist in keinem Voicechannel");
            }
        } else {
            embedBuilder.setDescription("Du bist in keinem Voicechannel mit dem Bot");
        }
        msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }
}
