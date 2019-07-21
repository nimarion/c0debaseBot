package de.c0debase.bot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.c0debase.bot.utils.EmbedUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.regex.Pattern;

public class ColorCommand extends Command {

    private static final Pattern VALID_HEX_CODE = Pattern.compile("#?([0-9a-f]{3}|[0-9a-f]{6}|[0-9a-f]{8})", Pattern.CASE_INSENSITIVE);

    public ColorCommand() {
        this.name = "color";
        this.help = "Zeigt ein Bild der angegebenen Farbe";
        this.arguments = "[color]";
    }


    @Override
    protected void execute(CommandEvent commandEvent) {
        final Message message = commandEvent.getMessage();
        final TextChannel channel = message.getTextChannel();
        if (!commandEvent.getArgs().isEmpty()) {
            final String color = commandEvent.getArgs().replace("#", "");
            if (VALID_HEX_CODE.matcher(color).matches()) {
                commandEvent.reply(EmbedUtils.getEmbed(commandEvent.getAuthor(), true)
                        .setTitle("Color-Code-Preview").setDescription("Farbcode Vorschau für: " + color)
                        .setImage("https://dummyimage.com/250x250/" + color + "/" + color + ".png").build());
            } else {
                commandEvent.reply(EmbedUtils.getEmbed(commandEvent.getAuthor(), false).setDescription("Kein Hex Code gefunden :hushed:").build());
            }
        } else {
            commandEvent.reply("ajaj");
        }
    }

}
