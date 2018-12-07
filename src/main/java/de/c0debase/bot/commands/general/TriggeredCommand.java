package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.core.Codebase;
import de.c0debase.bot.utils.ImageEditor;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class TriggeredCommand extends Command {

    public TriggeredCommand() {
        super("triggered", "Erstellt ein Bild ^^", Category.GENERAL);
    }

    @Override
    public void execute(final String[] args, final Message message) {
        final Member member = message.getMentionedMembers().size() == 0 ? message.getMember() : message.getMentionedMembers().get(0);
        final TextChannel channel = message.getTextChannel();
        if (member.getUser().getAvatarUrl() == null) {
            channel.sendMessage("Dieser Command geht nicht mit dem default Avatar").queue();
            return;
        }
        try {
            final URLConnection urlConnection = new URL(member.getUser().getAvatarUrl()).openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            try (final InputStream imageInputStream = urlConnection.getInputStream();
                 final InputStream avatarInputStream = new URL("https://i.imgur.com/i1aA59R.png").openConnection()
                         .getInputStream()) {
                final BufferedImage image = ImageIO.read(imageInputStream);
                ImageEditor imageEditor = new ImageEditor(image);
                try {
                    final BufferedImage avatarImage = ImageIO.read(avatarInputStream);
                    imageEditor.drawImage(0, 102, 128, 30, avatarImage);
                    channel.sendFile(imageEditor.getInputStream(), "triggered.png").queue();
                } finally {
                    imageEditor.close();
                }
            }
        } catch (Exception exception) {
            channel.sendMessage("Ein Fehler ist aufgetreten!").queue();
        }
    }
}
