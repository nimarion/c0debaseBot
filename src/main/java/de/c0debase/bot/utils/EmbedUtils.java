package de.c0debase.bot.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class EmbedUtils {

    public static EmbedBuilder getEmbed(final User author, final boolean success){
        return new EmbedBuilder().setFooter("@" + author.getName() + "#" + author.getDiscriminator(),
                author.getEffectiveAvatarUrl()).setColor(success ? Color.GREEN : Color.RED);
    }

}
