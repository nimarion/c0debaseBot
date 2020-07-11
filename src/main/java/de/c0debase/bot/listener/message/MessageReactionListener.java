package de.c0debase.bot.listener.message;

import de.c0debase.bot.Codebase;
import de.c0debase.bot.utils.DiscordUtils;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReactionListener extends ListenerAdapter {

    private final Codebase bot;

    public MessageReactionListener(final Codebase bot) {
        this.bot = bot;
        bot.getJDA().addEventListener(this);
    }

    @Override
    public void onGenericGuildMessageReaction(final GenericGuildMessageReactionEvent event) {
        if (event.getUser().isBot()) {
            return;
        }
        event.getChannel().retrieveMessageById(event.getMessageId()).queue(success -> {
            final String emote = DiscordUtils.getReaction(event.getReactionEmote());
            if (emote == null) {
                return;
            }
            if (emote.equalsIgnoreCase("wastebasket") && success.getAuthor().isBot()) {
                success.delete().queue();
                return;
            }
        });
    }
    
}
