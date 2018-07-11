package de.c0debase.bot.database.data;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Biosphere
 * @date 08.07.18
 */
@Data
public class Activity {

    private int year, day, messages;
    private String guildID;
    private List<String> users;
    private Map<String, Integer> channel;

}
