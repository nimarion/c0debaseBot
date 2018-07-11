package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.net.URL;
import java.net.URLConnection;

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
        Member member = args.length == 0 ? msg.getMember() : searchMember(args[0], msg.getMember());
        try {
            URLConnection urlConnection = new URL("http://belikebill.azurewebsites.net/billgen-API.php?default=1&name=" + member.getEffectiveName().replaceAll("\\s+","")).openConnection();
            urlConnection.setRequestProperty("User-Agent",  "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            msg.getTextChannel().sendFile(urlConnection.getInputStream(), "belikebill.png").queue();
        } catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
