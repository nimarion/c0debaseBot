package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.utils.ImageEditor;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Biosphere
 * @date 29.06.18
 */
public class TriggeredCommand extends Command {

    public TriggeredCommand() {
        super("triggered", "Erstellt ein Bild ^^", Categorie.GENERAL);
    }

    @Override
    public void execute(String[] args, Message msg) {
        Member member = msg.getMentionedMembers().isEmpty() ? msg.getMember() : msg.getMentionedMembers().get(0);
        if(member.getUser().getAvatarUrl() == null){
            msg.getTextChannel().sendMessage("Dieser Command geht nicht mit default Avatar").queue();
            return;
        }
        try {
            URLConnection urlConnection = new URL(member.getUser().getAvatarUrl()).openConnection();
            urlConnection.setRequestProperty("User-Agent",  "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            BufferedImage image = ImageIO.read(urlConnection.getInputStream());
            ImageEditor imageEditor = new ImageEditor(image);
            BufferedImage avatarImage = ImageIO.read(new URL("https://i.imgur.com/i1aA59R.png").openConnection().getInputStream());
            System.out.println(avatarImage.getWidth() + "-" + avatarImage.getHeight());
            System.out.println(image.getWidth() + "#" + image.getHeight());
            imageEditor.drawImage(0, 102, 128, 30, avatarImage);
            msg.getTextChannel().sendFile(imageEditor.getInputStream(), "triggered.png").queue();
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
