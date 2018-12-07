package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.core.Codebase;
import net.dv8tion.jda.core.entities.Message;

import java.net.URL;
import java.net.URLConnection;

/**
 * @author Biosphere
 * @date 01.07.18
 */
public class AvatarGeneratorCommand extends Command {

    public AvatarGeneratorCommand() {
        super("avatargenerator", "Erstellt ein Avatar", Category.GENERAL, "ag");
    }

    @Override
    public void execute(final Codebase bot, final String[] args, final Message message) {
        final String name = args.length == 0 ? message.getMember().getEffectiveName().replaceAll("\\s+", "") : args[0];
        final String sex = args.length == 2 ? ((!args[1].equalsIgnoreCase("male") && !args[1].equalsIgnoreCase("female")) ? "male" : args[1]) : "male";
        try {
            final URLConnection urlConnection = new URL("http://eightbitavatar.herokuapp.com/?id=" + name + "&s=" + sex.toLowerCase() + "&size=400").openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            message.getTextChannel().sendFile(urlConnection.getInputStream(), "avatar.png").queue();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
