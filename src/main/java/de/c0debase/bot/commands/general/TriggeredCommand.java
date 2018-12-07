package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.core.Codebase;
import de.c0debase.bot.utils.ImageEditor;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Biosphere
 * @date 29.06.18
 */
public class TriggeredCommand extends Command {

    public TriggeredCommand() {
        super("triggered", "Erstellt ein Bild ^^", Category.GENERAL);
    }

    @Override
    public void execute(final Codebase bot, final String[] args, final Message message) {
        final Member member = message.getMentionedMembers().size() == 0 ? message.getMember() : message.getMentionedMembers().get(0);
        if (member.getUser().getAvatarUrl() == null) {
            message.getTextChannel().sendMessage("Dieser Command geht nicht mit dem default Avatar").queue();
            return;
        }
        try {
            final URLConnection urlConnection = new URL(member.getUser().getAvatarUrl()).openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            final BufferedImage image = ImageIO.read(urlConnection.getInputStream());
            final ImageEditor imageEditor = new ImageEditor(image);
            final BufferedImage avatarImage = ImageIO.read(new URL("https://i.imgur.com/i1aA59R.png").openConnection().getInputStream());
            imageEditor.drawImage(0, 102, 128, 30, avatarImage);
            message.getTextChannel().sendFile(imageEditor.getInputStream(), "triggered.png").queue();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
