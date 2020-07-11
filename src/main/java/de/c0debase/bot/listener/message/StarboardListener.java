package de.c0debase.bot.listener.message;

import java.util.Date;

import de.c0debase.bot.Codebase;
import de.c0debase.bot.database.model.StarboardPost;
import de.c0debase.bot.utils.DiscordUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class StarboardListener extends ListenerAdapter{

    private final Codebase bot;

    public StarboardListener(final Codebase bot) {
        this.bot = bot;
        bot.getJDA().addEventListener(this);
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getUser().isBot()) {
            return;
        }
        event.getChannel().retrieveMessageById(event.getMessageId()).queue(message -> {
            final String emote = DiscordUtils.getReaction(event.getReactionEmote());
            if (emote.equalsIgnoreCase("star")) {
                if (getStarboardChannel(event.getGuild()) != null) {
                    handleStarReaction(message);
                }
            }
        });
    }

    @Override
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
        if (event.getUser().isBot()) {
            return;
        }
        event.getChannel().retrieveMessageById(event.getMessageId()).queue(message -> {
            final String emote = DiscordUtils.getReaction(event.getReactionEmote());
            if (emote.equalsIgnoreCase("star")) {
                if (getStarboardChannel(event.getGuild()) != null) {
                    handleStarReaction(message);
                }
            }
        });
    }

    private void handleStarReaction(final Message message) {
        final StarboardPost starboardPost = bot.getDatabase().getStarboardDao().getStarboardPost(message.getId());
        final boolean dontDelete = message.getReactions().stream()
                .filter(r -> r.getReactionEmote().getName().equalsIgnoreCase("⭐")).findFirst().isPresent();
        if (!dontDelete && starboardPost != null) {
            deleteStarMessage(message, starboardPost);
            return;
        }
        if (starboardPost == null) {
            postStarMessage(message);
        } else {
            editStarMessage(message, starboardPost);
        }
    }

    public void editStarMessage(final Message message, final StarboardPost starboardPost) {
        final TextChannel textChannel = getStarboardChannel(message.getGuild());
        textChannel.retrieveMessageById(starboardPost.getStarboardMessageId()).queue(success -> {
            final int starCount = (int) message.getReactions().stream()
                    .filter(r -> r.getReactionEmote().getName().equalsIgnoreCase("⭐")).findFirst().get().getCount();
            starboardPost.setStarCount(starCount);
            success.editMessage(getStarMessageEmbed(message, starboardPost)).queue();
            bot.getDatabase().getStarboardDao().updateStarboardPost(message.getId(), starCount);
        }, error -> {
            error.printStackTrace();
        });
    }

    private void postStarMessage(final Message message) {
        final int starCount = (int) message.getReactions().stream()
                .filter(r -> r.getReactionEmote().getName().equalsIgnoreCase("⭐")).findFirst().get().getCount();
        final StarboardPost starboardPost = new StarboardPost(message.getGuild().getId(), message.getId(),
                message.getChannel().getId(), message.getAuthor().getId(), starCount);
        final TextChannel textChannel = getStarboardChannel(message.getGuild());

        textChannel.sendMessage(getStarMessageEmbed(message, starboardPost)).queue(success -> {
            starboardPost.setStarboardMessageId(success.getId());
            bot.getDatabase().getStarboardDao().createStarboardPost(starboardPost);
        });
    }

    private void deleteStarMessage(final Message message, final StarboardPost starboardPost) {
        final TextChannel textChannel = getStarboardChannel(message.getGuild());
        textChannel.retrieveMessageById(starboardPost.getStarboardMessageId()).queue(success -> {
            success.delete().queue();
            bot.getDatabase().getStarboardDao().deleteStarboardPost(message.getId());
        });
    }

    private TextChannel getStarboardChannel(final Guild guild) {
        return guild.getTextChannelsByName("starboard", true).isEmpty() ? null
                : guild.getTextChannelsByName("starboard", true).get(0);
    }

    private Message getStarMessageEmbed(final Message message, final StarboardPost starboardPost) {
        MessageBuilder messageBuilder = new MessageBuilder();
        MessageEmbed messageEmbed = new EmbedBuilder().setTimestamp(new Date().toInstant())
                .setAuthor(message.getAuthor().getName(), null, message.getAuthor().getAvatarUrl())
                .setDescription(String.format("%s \n\n**Original**\n[Jump!](%s)",
                 message.getContentStripped(), message.getJumpUrl()))
                 .setImage(!message.getAttachments().isEmpty() && message.getAttachments().get(0).isImage() ? message.getAttachments().get(0).getUrl() : null)
                .build();
        messageBuilder.append(String.format(":star2: %s %s ID: %s", starboardPost.getStarCount(), message.getTextChannel().getAsMention(), message.getId()));
        messageBuilder.setEmbed(messageEmbed);
        return messageBuilder.build();
    }
    
}