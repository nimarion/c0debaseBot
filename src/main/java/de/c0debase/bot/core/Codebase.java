package de.c0debase.bot.core;

import de.c0debase.bot.commands.CommandManager;
import de.c0debase.bot.database.DataManager;
import de.c0debase.bot.tempchannel.Tempchannel;
import net.dv8tion.jda.core.JDA;

import java.util.Map;

public interface Codebase extends AutoCloseable {

    void init() throws Exception;

    DataManager getDataManager();

    JDA getJDA();

    CommandManager getCommandManager();

    Map<String, Tempchannel> getTempchannels();

}
