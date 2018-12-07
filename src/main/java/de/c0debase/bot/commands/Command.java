package de.c0debase.bot.commands;

import de.c0debase.bot.core.Codebase;
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

    private final String command;
    private final String[] aliases;
    private final String description;
    private final Category category;

    public Command(final String command, final String description, final Category category, final String... alias) {
        this.command = command;
        this.description = description;
        this.category = category;
        this.aliases = alias;
    }

    public abstract void execute(final Codebase bot, final String[] args, final Message message);

    protected EmbedBuilder getEmbed(final Guild guild, final User requester) {
        return new EmbedBuilder().setFooter("@" + requester.getName() + "#" + requester.getDiscriminator(), requester.getEffectiveAvatarUrl()).setColor(guild.getSelfMember().getColor());
    }

    @Getter
    @AllArgsConstructor
    public enum Category {
        GENERAL("General", "one", "Öffentliche Commands"),
        STAFF("Team", "two", "Commands für das Team");

        private String name;
        private String emote;
        private String description;
    }
}
