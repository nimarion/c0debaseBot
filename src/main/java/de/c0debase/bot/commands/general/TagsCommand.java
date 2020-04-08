package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.tags.TagManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class TagsCommand extends Command {

    public TagsCommand() {
        super("tags", "Zeigt alle vorhandenen Tags an", Category.GENERAL);
    }

    @Override
    public void execute(String[] args, Message message) {
        final TagManager tagManager = getBot().getTagManager();
        final EmbedBuilder embedBuilder = getEmbed(message.getGuild(), message.getAuthor());
        embedBuilder.setTitle("Verfügbare Tags");
        
        tagManager.getTags().forEach(tag -> {
            embedBuilder.addField(tag.getName(), tag.getDescription(), false);
        });

        message.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }



}