package de.c0debase.bot.commands;

import de.c0debase.bot.core.Codebase;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public abstract class Command {
    protected Codebase bot = null;

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

    public abstract void execute(final String[] args, final Message message);

    protected EmbedBuilder getEmbed(final Guild guild, final User requester) {
        return new EmbedBuilder().setFooter("@" + requester.getName() + "#" + requester.getDiscriminator(),
                requester.getEffectiveAvatarUrl()).setColor(guild.getSelfMember().getColor());
    }

    public void setInstance(final Codebase instance) {
        if (bot != null) {
            throw new IllegalStateException("Can only initialize once!");
        }
        bot = instance;
    }

    public Codebase getBot() {
        return bot;
    }

    public String getCommand() {
        return command;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public enum Category {
        GENERAL("General", "one", "Öffentliche Commands"),
        STAFF("Team", "two", "Commands für das Team");

        private String name;
        private String emote;
        private String description;

        Category(String name, String emote, String description) {
            this.name = name;
            this.emote = emote;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getEmote() {
            return emote;
        }

        public String getDescription() {
            return description;
        }
    }
}
