package de.c0debase.bot.tags;

import java.util.List;

public class Tag {

    private String name;
    private String title;
    private String url;
    private String description;
    private List<CustomField> fields;

    public List<CustomField> getFields() {
        return fields;
    }

    public void setFields(List<CustomField> fields) {
        this.fields = fields;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    

}