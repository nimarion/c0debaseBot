package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.utils.Constants;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author SpigotException
 * created at the 29.03.2018 at 13:15
 */
public class YesNoCommand extends Command {

    private StringJoiner stringJoiner;

    private final List<String> yesGifs = Arrays.asList(
            "https://media.giphy.com/media/nFjDu1LjEADh6/giphy.gif",
            "https://media.giphy.com/media/GCvktC0KFy9l6/giphy.gif",
            "https://media.giphy.com/media/1tHzw9PZCB3gY/giphy.gif",
            "https://media.giphy.com/media/1zSiX3p2XEZpe/giphy.gif",
            "https://media.giphy.com/media/3o7abKhOpu0NwenH3O/giphy.gif",
            "https://media.giphy.com/media/pNpONEEg3pLIQ/giphy.gif",
            "https://media.giphy.com/media/jL6OeIhk3zPi/giphy.gif",
            "https://media.giphy.com/media/l4q83E0RjRSGLXBLO/giphy.gif",
            "https://media.giphy.com/media/oGO1MPNUVbbk4/giphy.gif",
            "https://media.giphy.com/media/100YmlniUkSv84/giphy.gif",
            "https://media.giphy.com/media/r1fDuPIcs18d2/giphy.gif",
            "https://media.giphy.com/media/CmQQsUxwjBEgU/giphy.gif",
            "https://media.giphy.com/media/JE6xHkcUPtYs0/giphy.gif",
            "https://media.giphy.com/media/Y01jP8QeLOox2/giphy.gif"
    );

    private final List<String> noGifs = Arrays.asList(
            "https://media.giphy.com/media/g69ZPJfLy7hD2/giphy.gif",
            "https://media.giphy.com/media/12XMGIWtrHBl5e/giphy.gif",
            "https://media.giphy.com/media/nR4L10XlJcSeQ/giphy.gif",
            "https://media.giphy.com/media/6Q2KA5ly49368/giphy.gif",
            "https://media.giphy.com/media/TfS8MAR9ucLHW/giphy.gif",
            "https://media.giphy.com/media/oxFDq4E9CHb7W/giphy.gif",
            "https://media.giphy.com/media/YWFmlljmSpo6k/giphy.gif",
            "https://media.giphy.com/media/FEikw3bXVHdMk/giphy.gif",
            "https://media.giphy.com/media/wofftnAdDtx4s/giphy.gif",
            "https://media.giphy.com/media/vPN3zK9dNL236/giphy.gif",
            "https://media.giphy.com/media/EriPNV1whwKac/giphy.gif"
    );

    public YesNoCommand() {
        super("yn", "Answers to a question with \"yes\" or \"no\"", Categorie.GENERAL, "yesno", "jn", "janein");
    }

    @Override
    public void execute(String[] args, Message msg) {
        if (args.length > 0) {
            stringJoiner = new StringJoiner(" ");
            for (int i = 0; i < args.length; i++) {
                stringJoiner.add(args[i]);
            }
            boolean yn = Constants.RANDOM.nextBoolean();
            msg.getTextChannel().sendMessage(
                    new EmbedBuilder()
                            .setTitle("Ja oder Nein?")
                            .setDescription(
                                    "Deine Frage: " + stringJoiner.toString() + "\n\n" +
                                            "Meine Antwort: " + (yn ? "Ja" : "Nein") + "\n")
                            .setColor(yn ? Color.GREEN : Color.RED)
                            .setImage(yn ? yesGifs.get(Constants.RANDOM.nextInt(yesGifs.size())) : noGifs.get(Constants.RANDOM.nextInt(noGifs.size()))).build()
            ).queue();
        } else {
            msg.getTextChannel().sendMessage(getEmbed(msg.getGuild(), msg.getAuthor()).setDescription("!yn [Deine Frage]").build()).queue();
        }
    }
}
