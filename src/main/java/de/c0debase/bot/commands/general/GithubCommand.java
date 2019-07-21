package de.c0debase.bot.commands.general;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.c0debase.bot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

public class GithubCommand extends Command {

    public GithubCommand() {
        this.name = "github";
        this.help = "Zeigt einige Informationen über das Repo des Bots";
        this.aliases = new String[]{"code", "source"};
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        final StringWriter writer = new StringWriter();
        try {
            try (final InputStream inputStream = new URL("https://api.github.com/repos/Biospheere/c0debaseBot").openConnection().getInputStream()) {
                IOUtils.copy(inputStream, writer, "UTF-8");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final JsonObject jsonObject = new JsonParser().parse(writer.toString()).getAsJsonObject();
        final EmbedBuilder embedBuilder = EmbedUtils.getEmbed(commandEvent.getAuthor(), true);
        embedBuilder.setTitle("c0debaseBot", "https://github.com/Biospheere/c0debaseBot");
        embedBuilder.addField("Sprache", jsonObject.get("language").getAsString(), false);
        embedBuilder.addField("Stars", String.valueOf(jsonObject.get("stargazers_count").getAsInt()), false);
        embedBuilder.addField("Forks", String.valueOf(jsonObject.get("forks_count").getAsInt()), false);
        embedBuilder.addField("Issues", String.valueOf(jsonObject.get("open_issues_count").getAsInt()), false);
        embedBuilder.addField("Clonen", "`git clone " + jsonObject.get("ssh_url").getAsString() + "`", false);
        commandEvent.reply(embedBuilder.build());
    }
}
