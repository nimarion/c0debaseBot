package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.tags.Tag;
import de.c0debase.bot.tags.TagManager;
import de.c0debase.bot.utils.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class TagCommand extends Command {

    public TagCommand() {
        super("tag", "description", Category.GENERAL);
    }

    @Override
    public void execute(String[] args, Message message) {
        final EmbedBuilder embedBuilder = getEmbed(message.getGuild(), message.getAuthor());
        if(args.length == 0){
            embedBuilder.setDescription("!tag <name>");
        } else {
            final String targetTag = args[0];
            final TagManager  tagManager = getBot().getTagManager();
            final Tag tag = tagManager.getTag(targetTag);
            if(tag != null){
                embedBuilder.setTitle(tag.getTitle(), tag.getUrl());
                embedBuilder.appendDescription(tag.getDescription());
                if(tag.getFields() != null){
                    tag.getFields().forEach(field -> {
                        embedBuilder.addField(field.getName(), field.isEscape() ? StringUtils.replaceCharacter(field.getValue()) : field.getValue(), field.isInline());
                    });
                }
            } else {
                embedBuilder.setDescription("Tag existiert nicht!");
            }
        }
        message.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }

    
}