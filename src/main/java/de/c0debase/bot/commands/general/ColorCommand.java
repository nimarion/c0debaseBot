package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.util.regex.Pattern;

/**
 * @author Biosphere
 * @date 29.03.18
 */
public class ColorCommand extends Command {

    private static final Pattern VALID_HEX_CODE = Pattern.compile("#?([0-9a-f]{3}|[0-9a-f]{6}|[0-9a-f]{8})", Pattern.CASE_INSENSITIVE);


    public ColorCommand() {
        super("color", "Show a image of a color", Category.GENERAL);
    }

    @Override
    public void execute(String[] args, Message msg) {
        if (args.length > 0) {
            if (VALID_HEX_CODE.matcher(args[0].replace("#", "")).matches()) {
                msg.getTextChannel().sendMessage(
                        new EmbedBuilder()
                                .setTitle("Color-Code-Preview").setDescription("Farbcode Vorschau für: " + args[0])
                                .setImage("https://dummyimage.com/250x250/" + args[0].replace("#", "") + "/" + args[0].replace("#", "") + ".png")
                                .setColor(msg.getGuild().getSelfMember().getColor()).build()
                ).queue();
            } else {
                msg.getTextChannel().sendMessage(getEmbed(msg.getGuild(), msg.getAuthor()).setDescription("Kein Hex Code gefunden :hushed:").build()).queue();
            }
        } else {
            msg.getTextChannel().sendMessage(getEmbed(msg.getGuild(), msg.getAuthor()).setDescription("!color [code]").build()).queue();
        }
    }

}
