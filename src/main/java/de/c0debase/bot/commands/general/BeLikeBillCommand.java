package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author Biosphere
 * @date 22.04.18
 */
public class BeLikeBillCommand extends Command {

    public BeLikeBillCommand() {
        super("belikebill", "Zeigt ein BeLikeBill Bild", Category.GENERAL, "blb");
    }

    @Override
    public void execute(String[] args, Message msg) {
        msg.getTextChannel().sendMessage(getEmbed(msg.getGuild(), msg.getAuthor()).setImage("http://belikebill.azurewebsites.net/billgen-API.php?default=1&name=" + (msg.getMentionedMembers().isEmpty() ? msg.getMember().getEffectiveName().replaceAll("\\s+",""): msg.getMentionedMembers().get(0).getEffectiveName().replaceAll("\\s+",""))).build()).queue();
    }
}
