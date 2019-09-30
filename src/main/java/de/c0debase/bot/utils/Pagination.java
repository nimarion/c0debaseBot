package de.c0debase.bot.utils;

import de.c0debase.bot.database.data.CodebaseUser;

import java.util.Collections;
import java.util.List;

public class Pagination {

    private final List<CodebaseUser> objects;
    private final int pageSize;

    /**
     *
     * @param objects The {@link CodebaseUser} to display on a page
     * @param pageSize The size of a page
     */
    public Pagination(final List<CodebaseUser> objects, final int pageSize) {
        this.objects = objects;
        this.pageSize = pageSize;
    }

    /**
     *
     * @param page The page number which should be returned
     * @return A List of  {@link CodebaseUser} of the requested page
     */
    public List<CodebaseUser> getPage(final int page) {
        if (pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException("Invalid page size: " + pageSize);
        }

        final int fromIndex = (page - 1) * pageSize;
        if (objects == null || objects.size() < fromIndex) {
            return Collections.emptyList();
        }

        return objects.subList(fromIndex, Math.min(fromIndex + pageSize, objects.size()));
    }


}
