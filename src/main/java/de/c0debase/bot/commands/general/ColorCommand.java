package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.regex.Pattern;

public class ColorCommand extends Command {

    private static final Pattern VALID_HEX_CODE = Pattern.compile("#?([0-9a-f]{3}|[0-9a-f]{6}|[0-9a-f]{8})",
            Pattern.CASE_INSENSITIVE);

    public ColorCommand() {
        super("color", "Show a image of a color", Category.GENERAL);
    }

    @Override
    public void execute(final String[] args, final Message message) {
        final EmbedBuilder embedBuilder = getEmbed(message.getMember());
        if (args.length > 0) {
            if (VALID_HEX_CODE.matcher(args[0].replace("#", "")).matches()) {
                embedBuilder.setTitle("Color-Code-Preview").setDescription("Farbcode Vorschau für: " + args[0])
                        .setImage("https://dummyimage.com/250x250/" + args[0].replace("#", "") + "/"
                                + args[0].replace("#", "") + ".png");
            } else {
                embedBuilder.setDescription("Kein Hex Code gefunden :hushed:");
            }
        } else {
            embedBuilder.setDescription("!color [code]");
        }
        message.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }

}
