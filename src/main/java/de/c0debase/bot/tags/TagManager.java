package de.c0debase.bot.tags;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import de.c0debase.bot.utils.Constants;

public class TagManager {

    private final List<Tag> tags;

    // TODO: Replace with database or download from server
    public TagManager(){
        final InputStream inputStream = TagManager.class.getResourceAsStream("/tags.json");
        final Reader reader = new InputStreamReader(inputStream);
        final Type listType = new TypeToken<List<Tag>>() {
        }.getType();

        final JsonArray jsonArray = Constants.GSON.fromJson(reader, JsonObject.class).get("tags").getAsJsonArray();

        tags =  Constants.GSON.fromJson(jsonArray.toString(), listType);
    }

    public Tag getTag(final String name){
        for (Tag tag : tags){
            if(tag.getName().equalsIgnoreCase(name)){
                return tag;
            }
        }
        return null;
    }

    public List<Tag> getTags(){
        return tags;
    }

}