package de.c0debase.bot.pagination;

import de.c0debase.bot.core.Codebase;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Pagination {

    private String title;
    private int pageSize;
    private TextChannel textChannel;

    public Pagination(String title, TextChannel textChannel, int pageSize) {
        this.title = title;
        this.textChannel = textChannel;
        this.pageSize = pageSize;
    }

    public Pagination(String title) {
        this(title, Codebase.getBot().getGuild().getTextChannelById(System.getenv("BOTCHANNEL")), 10);
    }

    public abstract void update(Message success, MessageEmbed messageEmbed, String emote);

    public abstract void createFirst(boolean descending, TextChannel textChannel);

    public void createFirst(boolean descending) {
        createFirst(descending, getTextChannel());
    }

    public void createFirst() {
        createFirst(true, getTextChannel());
    }

    public abstract void buildList(EmbedBuilder embedBuilder, int page, boolean descending);

    public Codebase getBot() {
        return Codebase.getBot();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }

    public void setTextChannel(TextChannel textChannel) {
        this.textChannel = textChannel;
    }

    public <T> Map<Integer, T> getPage(int page, List<T> list) {
        if (pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException("Invalid page size: " + pageSize);
        }

        final int fromIndex = (page - 1) * pageSize;
        if (list == null || list.size() < fromIndex) {
            return Collections.emptyMap();
        }

        HashMap<Integer, T> map = new HashMap<>();
        AtomicInteger count = new AtomicInteger(1);
        list.subList(fromIndex, Math.min(fromIndex + pageSize, list.size())).forEach(entry -> {
            map.put(fromIndex + count.get(), entry);
            count.getAndIncrement();
        });
        return map;
    }
}