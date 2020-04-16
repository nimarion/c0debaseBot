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

    public Pagination(String title, int pageSize) {
        this.title = title;
        this.pageSize = pageSize;
    }

    public Pagination(String title) {
        this(title, 10);
    }

    public abstract void update(Message success, MessageEmbed messageEmbed, String emote);

    public abstract void createFirst(boolean descending, TextChannel textChannel);

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

    public <T> Map<Integer, T> getPage(int page, List<T> list, boolean descending) {
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
            int number = fromIndex + count.get();
            if (!descending) {
                number = list.size() - (fromIndex + count.get());
            }
            count.getAndIncrement();
            map.put(number, entry);
        });
        return map;
    }

    public <T> Map<Integer, T> getPage(int page, List<T> list) {
        return getPage(page, list, true);
    }
}