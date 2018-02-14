package de.c0debase.bot.commands.general;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.io.IOUtils;

import java.io.StringWriter;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class UrbandirectoryCommand extends Command {

    public UrbandirectoryCommand() {
        super("urbandirectory", "Sucht im Urban Dictionary etwas für dich", Categorie.GENERAL, "ud");
    }

    @Override
    public void execute(String[] args, Message msg) {
        if (args.length < 1) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setFooter("@" + msg.getAuthor().getName() + "#" + msg.getAuthor().getDiscriminator(), msg.getAuthor().getEffectiveAvatarUrl());
            embedBuilder.setTitle("Command: " + getCommand());
            embedBuilder.setColor(msg.getGuild().getSelfMember().getColor());
            embedBuilder.appendDescription("**Beschreibung: ** Suche etwas im Urban Dictionary!\n");
            embedBuilder.appendDescription("**Benutzung: ** " + "!ud [term]");
            msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
        } else {
            msg.getTextChannel().sendTyping().queue();
            final String search = String.join(" ", args);

            StringWriter writer = new StringWriter();
            try {
                URL url = new URL("http://api.urbandictionary.com/v0/define?term=" + URLEncoder.encode(search, "UTF-8"));
                IOUtils.copy(url.openConnection().getInputStream(), writer, "UTF-8");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            EmbedBuilder embedBuilder = getEmbed(msg.getGuild(), msg.getAuthor());
            embedBuilder.setTitle("Definition: " + search);
            embedBuilder.setFooter("@" + msg.getAuthor().getName() + "#" + msg.getAuthor().getDiscriminator(), msg.getAuthor().getEffectiveAvatarUrl());

            JsonArray jsonResult;
            try {
                JsonElement jsonElement = new JsonParser().parse(writer.toString());
                jsonResult = jsonElement.getAsJsonObject().getAsJsonArray("list");
                embedBuilder.appendDescription(jsonResult.size() != 0 ? jsonResult.get(0).getAsJsonObject().get("definition").getAsString() : "Search term not found.");
            } catch (JsonParseException ex) {
                embedBuilder.appendDescription("An error occurred.");
            } finally {
                msg.getTextChannel().sendMessage(embedBuilder.build()).queue();
            }
        }
    }
}
