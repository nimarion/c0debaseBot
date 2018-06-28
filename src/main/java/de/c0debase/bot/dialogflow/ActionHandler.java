package de.c0debase.bot.dialogflow;

import ai.api.model.AIResponse;
import lombok.Getter;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author Biosphere
 * @date 28.06.18
 */
@Getter
public abstract class ActionHandler {

    private final String name;

    public ActionHandler(String name){
        this.name = name;
    }

    public abstract void handle(AIResponse aiResponse, Message message);
}
