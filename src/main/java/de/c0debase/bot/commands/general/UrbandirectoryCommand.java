package de.c0debase.bot.commands.general;

import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class UrbandirectoryCommand extends Command {

    public UrbandirectoryCommand() {
        super("urbandirectory", "Sucht im Urban Dictionary etwas für dich", Category.GENERAL, "ud");
    }

    @Override
    public void execute(String[] args, Message message) {
        if (args.length < 1) {
            message.getTextChannel().sendMessage(getEmbed(message.getGuild(), message.getAuthor()).setDescription("!ud [term]").build()).queue();
        } else {
            message.getTextChannel().sendTyping().queue();
            final String search = String.join(" ", args);

            final StringWriter writer = new StringWriter();
            try {
                URL url = new URL("http://api.urbandictionary.com/v0/define?term=" + URLEncoder.encode(search, "UTF-8"));
                try (final InputStream inputStream = url.openConnection().getInputStream()) {
                    IOUtils.copy(inputStream, writer, "UTF-8");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            EmbedBuilder embedBuilder = getEmbed(message.getGuild(), message.getAuthor());
            embedBuilder.setTitle("Definition: " + search);

            try {
                JsonArray jsonResult = new JsonParser().parse(writer.toString()).getAsJsonObject().getAsJsonArray("list");
                embedBuilder.appendDescription(jsonResult.size() != 0 ? jsonResult.get(0).getAsJsonObject().get("definition").getAsString() : "Search term not found.");
            } catch (JsonParseException ex) {
                embedBuilder.appendDescription("An error occurred.");
            } finally {
                message.getTextChannel().sendMessage(embedBuilder.build()).queue();
            }
        }
    }
}
