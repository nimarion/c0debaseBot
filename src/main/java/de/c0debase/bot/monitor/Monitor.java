package de.c0debase.bot.monitor;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Biosphere
 * @date 14.02.18
 */
@Getter
@AllArgsConstructor
public abstract class Monitor {

    private String name;
    private String description;
    private Class event;

    public abstract void trigger();
}
