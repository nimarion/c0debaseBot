package de.c0debase.bot.utils;

import java.util.Collections;
import java.util.List;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class Pagination<T> {

    private List<T> objects;
    private int pageSize;

    public Pagination(List<T> objects, int pageSize) {
        this.objects = objects;
        this.pageSize = pageSize;
    }

    public List<T> getPage(int page) {
        if (pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException("Invalid page size: " + pageSize);
        }

        int fromIndex = (page - 1) * pageSize;
        if (objects == null || objects.size() < fromIndex) {
            return Collections.emptyList();
        }

        return objects.subList(fromIndex, Math.min(fromIndex + pageSize, objects.size()));
    }

    public void updateList(List<T> obj) {
        this.objects = obj;
    }

}
