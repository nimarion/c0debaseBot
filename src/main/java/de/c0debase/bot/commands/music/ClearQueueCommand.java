package de.c0debase.bot.commands.music;

import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class ClearQueueCommand extends Command {

    public ClearQueueCommand() {
        super("clearqueue", "Leert die Warteschlange", Categorie.MUSIC, "cc");
    }

    @Override
    public void execute(String[] args, Message msg) {
        EmbedBuilder embedBuilder = getEmbed(msg.getGuild(), msg.getAuthor());
        if (msg.getMember().getVoiceState().inVoiceChannel() && msg.getMember().getVoiceState().getChannel().getMembers().contains(msg.getGuild().getSelfMember())) {
            CodebaseBot.getInstance().getMusicManager().clearQueue(msg.getGuild());
            CodebaseBot.getInstance().getMusicManager().stop(msg.getGuild());
            embedBuilder.setDescription("Warteschlange geleert");
        } else {
            embedBuilder.setDescription("Du bist in keinem Voicechannel mit dem Bot");
        }
        msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }
}
