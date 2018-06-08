package de.c0debase.bot.commands.general;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import de.c0debase.bot.commands.Command;
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
        super("lenny", "Get a random Lenny", Categorie.GENERAL);
    }

    @Override
    public void execute(String[] args, Message message) {
        message.getTextChannel().sendTyping().queue();
        final StringWriter writer = new StringWriter();
        try {
            URL url = new URL("https://api.lenny.today/v1/random");
            try (final InputStream inputStream = url.openConnection().getInputStream()) {
                IOUtils.copy(inputStream, writer, "UTF-8");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JsonArray jsonResult = new JsonParser().parse(writer.toString()).getAsJsonArray();
        message.getTextChannel().sendMessage(jsonResult.get(0).getAsJsonObject().get("face").getAsString()).queue();
    }
}
