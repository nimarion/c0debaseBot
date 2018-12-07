package de.c0debase.bot.commands.general;

import com.google.gson.JsonParser;
import de.c0debase.bot.commands.Command;
import de.c0debase.bot.core.Codebase;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

/**
 * @author Biosphere
 * @date 08.06.18
 */
public class LennyCommand extends Command {

    public LennyCommand() {
        super("lenny", "Get a random Lenny", Category.GENERAL);
    }

    @Override
    public void execute(final Codebase bot, final String[] args, final Message message) {
        message.getTextChannel().sendTyping().queue();
        final StringWriter writer = new StringWriter();
        try {
            final URL url = new URL("https://api.lenny.today/v1/random");
            try (final InputStream inputStream = url.openConnection().getInputStream()) {
                IOUtils.copy(inputStream, writer, "UTF-8");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        message.getTextChannel().sendMessage(new JsonParser().parse(writer.toString()).getAsJsonArray().get(0).getAsJsonObject().get("face").getAsString()).queue();
    }
}
