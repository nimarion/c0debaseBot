package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class AvatarGeneratorCommand extends Command {

    public AvatarGeneratorCommand() {
        super("avatargenerator", "Erstellt ein Avatar", Category.GENERAL, "ag");
    }

    @Override
    public void execute(final String[] args, final Message message) {
        final String name = args.length == 0 ? message.getMember().getEffectiveName().replaceAll("\\s+", "") : args[0];
        final String rawSex = args[1];
        final String sex = args.length == 2 ?
                ((!rawSex.equalsIgnoreCase("male") && !rawSex.equalsIgnoreCase("female")) ? "male" : rawSex) : "male";
        final TextChannel channel = message.getTextChannel();
        try {
            final URLConnection urlConnection = new URL("http://eightbitavatar.herokuapp.com/?id=" + name + "&s=" + sex.toLowerCase() + "&size=400").openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            try (final InputStream inputStream = urlConnection.getInputStream()) {
                channel.sendFile(inputStream, "avatar.png").queue();
            }
        } catch (final Exception exception) {
            channel.sendMessage("Ein Fehler ist aufgetreten!").queue();
        }
    }
}
