package de.c0debase.bot.core;

import de.c0debase.bot.commands.CommandManager;
import de.c0debase.bot.database.DataManager;
import de.c0debase.bot.tempchannel.Tempchannel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Map;

public interface Codebase {

    DataManager getDataManager();

    JDA getJDA();

    CommandManager getCommandManager();

    Map<String, Tempchannel> getTempchannels();

    Guild getGuild();

}
