package de.c0debase.bot.commands.music;

import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class VolumeCommand extends Command {

    public VolumeCommand() {
        super("volume", "Ändert die Lautstärke", Categorie.MUSIC, "v");
    }

    @Override
    public void execute(String[] args, Message msg) {
        Guild guild = msg.getGuild();
        EmbedBuilder embedBuilder = getEmbed(msg.getGuild(), msg.getAuthor());
        if (msg.getMember().getVoiceState().inVoiceChannel() && msg.getMember().getVoiceState().getChannel().getMembers().contains(msg.getGuild().getSelfMember())) {
            if (args.length == 1) {
                int volume;
                try {
                    volume = Integer.parseInt(args[0]);
                    if (volume < 0 || volume > 100) {
                        embedBuilder.addField("Ungültige Lautstärke", "Wert kann nur zwischen 0 und 100 gesetzt werden.", false);
                    } else {
                        CodebaseBot.getInstance().getMusicManager().setVolume(guild, volume);
                        embedBuilder.addField("Neue Lautstärke: " + CodebaseBot.getInstance().getMusicManager().getVolume(guild), getVolume(CodebaseBot.getInstance().getMusicManager().getVolume(guild)), false);
                    }
                } catch (NumberFormatException e) {
                    embedBuilder.addField("Ungültiges Angabe", "*" + args[0] + "* ist keine Zahl.", false);
                }
            } else {
                embedBuilder.addField("Lautstärke: " + CodebaseBot.getInstance().getMusicManager().getVolume(guild), getVolume(CodebaseBot.getInstance().getMusicManager().getVolume(guild)), false);
            }
        } else {
            embedBuilder.setDescription("Du bist in keinem Voicechannel ^^");
        }
        msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }

    private String getVolume(int volume) {
        String s = "";
        for (int i = 10; i > 0; i--) {
            if (i > (volume / 10)) {
                s += ":black_large_square:";
            } else {
                s += ":white_large_square:";
            }
        }
        return s;
    }

}
