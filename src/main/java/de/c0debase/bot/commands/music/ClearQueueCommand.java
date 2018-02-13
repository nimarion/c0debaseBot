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
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setFooter(msg.getAuthor().getName(), msg.getAuthor().getEffectiveAvatarUrl());
        embedBuilder.setAuthor("Command: " + getCommand(), null, msg.getGuild().getIconUrl());
        embedBuilder.setColor(msg.getGuild().getSelfMember().getColor());
        if (msg.getMember().getVoiceState() != null && msg.getMember().getVoiceState().inVoiceChannel()) {
            CodebaseBot.getInstance().getMusicManager().clearQueue(msg.getGuild());
            CodebaseBot.getInstance().getMusicManager().stop(msg.getGuild());
            embedBuilder.setDescription("Warteschlange geleert");
        } else {
            embedBuilder.setDescription("Du bist in keinem Voicechannel ^^");
        }
        msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }
}
