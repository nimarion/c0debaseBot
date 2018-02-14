package de.c0debase.bot.monitor;

import de.c0debase.bot.CodebaseBot;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author Biosphere
 * @date 14.02.18
 */
public class MonitorManager {

    private ArrayList<Monitor> availableMonitors = new ArrayList<>();

    public MonitorManager() {
        Set<Class<? extends Monitor>> classes = new Reflections("de.c0debase.bot.monitor").getSubTypesOf(Monitor.class);
        classes.forEach(monitorClass -> {
            try {
                registerMonitor(monitorClass.newInstance());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public void trigger(Class event) {
        availableMonitors.forEach(monitor -> {
            if (monitor.getEvent().equals(event)) {
                monitor.trigger();
            }
        });
    }

    private void registerMonitor(Monitor monitor) {
        if (!availableMonitors.contains(monitor)) {
            availableMonitors.add(monitor);
            CodebaseBot.getInstance().getLogger().info("Registered Monitor " + monitor.getName());
        }
    }

}
