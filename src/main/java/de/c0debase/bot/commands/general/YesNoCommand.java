package de.c0debase.bot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.c0debase.bot.utils.Constants;
import de.c0debase.bot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class YesNoCommand extends Command {

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
        this.name = "yn";
        this.help = "Answers to a question with \"yes\" or \"no\"";
        this.aliases = new String[]{"yesno", "jn", "janein"};
        this.arguments = "[question]";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (!commandEvent.getArgs().isEmpty()) {
            final boolean yn = Constants.RANDOM.nextBoolean();
            commandEvent.reply(
                    new EmbedBuilder()
                            .setTitle("Ja oder Nein?")
                            .setDescription(
                                    "Deine Frage: " + commandEvent.getArgs() + "\n\n" +
                                            "Meine Antwort: " + (yn ? "Ja" : "Nein") + "\n")
                            .setColor(yn ? Color.GREEN : Color.RED)
                            .setImage(yn ? yesGifs.get(Constants.RANDOM.nextInt(yesGifs.size())) : noGifs.get(Constants.RANDOM.nextInt(noGifs.size()))).build()
            );
        } else {
            commandEvent.reply(EmbedUtils.getEmbed(commandEvent.getAuthor(), false).setDescription("!yn [Deine Frage]").build());
        }
    }
}
