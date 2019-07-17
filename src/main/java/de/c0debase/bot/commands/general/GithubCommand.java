package de.c0debase.bot.commands.general;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

public class GithubCommand extends Command {

    public GithubCommand() {
        super("github", "Zeigt einige Informationen über das Repo des Bots", Category.GENERAL, "code", "source");
    }

    @Override
    public void execute(final String[] args, final Message message) {
        final StringWriter writer = new StringWriter();
        try {
            try (final InputStream inputStream = new URL("https://api.github.com/repos/Biospheere/c0debaseBot").openConnection().getInputStream()) {
                IOUtils.copy(inputStream, writer, "UTF-8");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        final JsonObject jsonObject = new JsonParser().parse(writer.toString()).getAsJsonObject();

        final EmbedBuilder embedBuilder = getEmbed(message.getGuild(), message.getAuthor());
        embedBuilder.setTitle("c0debaseBot", "https://github.com/Biospheere/c0debaseBot");
        embedBuilder.addField("Sprache", jsonObject.get("language").getAsString(), false);
        embedBuilder.addField("Stars", String.valueOf(jsonObject.get("stargazers_count").getAsInt()), false);
        embedBuilder.addField("Forks", String.valueOf(jsonObject.get("forks_count").getAsInt()), false);
        embedBuilder.addField("Issues", String.valueOf(jsonObject.get("open_issues_count").getAsInt()), false);
        embedBuilder.addField("Clonen", "`git clone " + jsonObject.get("ssh_url").getAsString() + "`", false);
        message.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }
}
