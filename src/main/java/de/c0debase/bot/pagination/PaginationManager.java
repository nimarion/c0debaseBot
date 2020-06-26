package de.c0debase.bot.pagination;

import com.vdurmont.emoji.EmojiManager;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.c0debase.bot.Codebase;

import java.util.HashSet;
import java.util.Set;

public class PaginationManager extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(PaginationManager.class);
    private Set<Pagination> paginations;


    public PaginationManager(final Codebase bot) {
        this.paginations = new HashSet<>();
        final Set<Class<? extends Pagination>> classes = new Reflections("de.c0debase.bot.pagination.paginations")
                .getSubTypesOf(Pagination.class);
        for (Class<? extends Pagination> pgClass : classes) {
            try {
                final Pagination pagination = pgClass.getDeclaredConstructor().newInstance();
                if (paginations.add(pagination)) {
                    logger.info("Registered " + pagination.getTitle() + " Pagination");
                }
            } catch (Exception exception) {
                logger.error("Error while registering Pagination!", exception);
            }
        }
        bot.getJDA().addEventListener(this);
    }

    @Override
    public void onGenericGuildMessageReaction(final GenericGuildMessageReactionEvent event) {
        if (event.getUser().isBot()) {
            return;
        }
        event.getChannel().retrieveMessageById(event.getMessageId()).queue(success -> {
            final String emote = getReaction(event.getReactionEmote());
            if (emote == null) {
                return;
            }
            if (!success.getEmbeds().isEmpty() && success.getAuthor().isBot()) {
                final MessageEmbed messageEmbed = success.getEmbeds().get(0);
                if (messageEmbed.getFooter() != null) {
                    paginations.forEach(pagination -> {
                        if (pagination.getTitle().equalsIgnoreCase(messageEmbed.getTitle()))
                            pagination.update(success, messageEmbed, emote);
                    });
                }
            }
        });
    }

    private String getReaction(final MessageReaction.ReactionEmote emote) {
        try {
            return EmojiManager.getByUnicode(emote.getName()).getAliases().get(0);
        } catch (Exception e) {
            return emote.getName();
        }
    }

    public Pagination getPaginationByClass(Class<? extends Pagination> paginationClass) {
        for (Pagination pagination : paginations) {
            if (pagination.getClass().equals(paginationClass)) return pagination;
        }
        return null;
    }

}
