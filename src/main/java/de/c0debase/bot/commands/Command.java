package de.c0debase.bot.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

/**
 * @author Biosphere
 * @date 23.01.18
 */

@Getter
public abstract class Command {

    private String command;
    private String[] aliases;
    private String description;
    private Category category;

    public Command(String command, String description, Category category, String... alias) {
        this.command = command;
        this.description = description;
        this.category = category;
        this.aliases = alias;
    }

    public abstract void execute(String[] args, Message msg);

    protected EmbedBuilder getEmbed(Guild guild, User requester) {
        return new EmbedBuilder().setFooter("@" + requester.getName() + "#" + requester.getDiscriminator(), requester.getEffectiveAvatarUrl()).setColor(guild.getSelfMember().getColor());
    }

    @Getter
    @AllArgsConstructor
    public enum Category {
        GENERAL("General", "one", "Öffentliche Commands"),
        STAFF("Team", "two", "Commands für das Team"),
        MUSIC("Musik", "musical_score", "Alle Commands für die Musik");

        private String name;
        private String emote;
        private String description;
    }
}
