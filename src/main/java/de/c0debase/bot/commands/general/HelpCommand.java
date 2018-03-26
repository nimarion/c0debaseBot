package de.c0debase.bot.commands.general;

import com.vdurmont.emoji.EmojiManager;
import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.util.Arrays;

/**
 * @author Biosphere
 * @date 23.01.18
 */

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "Zeige diese Hilfe", Categorie.GENERAL, "hilfe");
    }

    @Override
    public void execute(String[] args, Message msg) {
        EmbedBuilder infoEmbed = new EmbedBuilder();
        infoEmbed.setColor(msg.getGuild().getSelfMember().getColor());
        infoEmbed.appendDescription(msg.getAuthor().getAsMention() + " schau mal in deine DMs");
        msg.getTextChannel().sendMessage(infoEmbed.build()).queue();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Hilfe");
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.appendDescription(":question: Help\nKlicke den entsprechenden Emote an für mehr Infos.\n");
        Arrays.stream(Categorie.values()).map(categorie -> "**" + (categorie.ordinal() + 1) + " - " + categorie.getName() + " Commands**\n" + categorie.getDescription() + "\n").forEach(embedBuilder::appendDescription);

        msg.getMember().getUser().openPrivateChannel().queue(success -> success.sendMessage(embedBuilder.build()).queue(message -> {
            Arrays.stream(Categorie.values()).forEach(categorie -> message.addReaction(EmojiManager.getForAlias(categorie.getEmote()).getUnicode()).queue());
            message.addReaction(EmojiManager.getForAlias("wastebasket").getUnicode()).queue();
        }));

    }
}
