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
    private static final String BLACK_SQUARE = ":black_large_square:";
    private static final String WHITE_SQUARE = ":white_large_square:";

    public VolumeCommand() {
        super("volume", "Ändert die Lautstärke", Categorie.MUSIC, "v");
    }

    @Override
    public void execute(String[] args, Message msg) {
        final Guild guild = msg.getGuild();
        final EmbedBuilder embedBuilder = getEmbed(msg.getGuild(), msg.getAuthor());
        if (msg.getMember().getVoiceState().inVoiceChannel() && msg.getMember().getVoiceState().getChannel().getMembers().contains(msg.getGuild().getSelfMember())) {
            if (args.length == 1) {
                final int newVolume;
                try {
                    newVolume = Integer.parseInt(args[0]);
                    if (newVolume < 0 || newVolume > 100) {
                        embedBuilder.addField("Ungültige Lautstärke", "Wert kann nur zwischen 0 und 100 gesetzt werden.", false);
                    } else {
                        CodebaseBot.getInstance().getMusicManager().setVolume(guild, newVolume);
                        embedBuilder.addField(String.format("Neue Lautstärke: %d", newVolume), getVolume(newVolume), false);
                    }
                } catch (NumberFormatException e) {
                    embedBuilder.addField("Ungültiges Angabe", String.format("*%s* ist keine Zahl.", args[0]), false);
                }
            } else {
                final int volume = CodebaseBot.getInstance().getMusicManager().getVolume(guild);
                embedBuilder.addField(String.format("Lautstärke: %d", volume), getVolume(volume), false);
            }
        } else {
            embedBuilder.setDescription("Du bist in keinem Voicechannel ^^");
        }
        msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }

    private String getVolume(int volume) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < 11; i++) {
            if (i > (volume / 10)) {
                stringBuilder.append(BLACK_SQUARE);
            } else {
                stringBuilder.append(WHITE_SQUARE);
            }
        }
        return stringBuilder.toString();
    }

}
