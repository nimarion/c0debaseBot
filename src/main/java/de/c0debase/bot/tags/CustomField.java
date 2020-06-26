package de.c0debase.bot.tags;

import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public class CustomField extends Field {

    private boolean escape;

    public CustomField(String name, String value, boolean inline, boolean checked) {
        super(name, value, inline, checked);
    }

    public boolean isEscape() {
        return escape;
    }

    public void setEscape(boolean escape) {
        this.escape = escape;
    }

    
}