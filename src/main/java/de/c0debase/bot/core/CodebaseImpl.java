package de.c0debase.bot.core;

import de.c0debase.bot.commands.CommandManager;
import de.c0debase.bot.database.DataManager;
import de.c0debase.bot.database.MongoDataManager;
import de.c0debase.bot.listener.guild.GuildMemberJoinListener;
import de.c0debase.bot.listener.guild.GuildMemberLeaveListener;
import de.c0debase.bot.listener.guild.GuildMemberNickChangeListener;
import de.c0debase.bot.listener.guild.GuildMemberRoleListener;
import de.c0debase.bot.listener.message.MessageReactionListener;
import de.c0debase.bot.listener.message.MessageReceiveListener;
import de.c0debase.bot.listener.message.TableFlipListener;
import de.c0debase.bot.listener.other.GuildReadyListener;
import de.c0debase.bot.listener.voice.GuildVoiceListener;
import de.c0debase.bot.tempchannel.Tempchannel;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CodebaseImpl implements Codebase {

    private static final Logger logger = LoggerFactory.getLogger(Codebase.class);

    private final JDA jda;
    private final DataManager dataManager;
    private final CommandManager commandManager;
    private final Map<String, Tempchannel> tempchannels;

    public CodebaseImpl() throws Exception {
        final long startTime = System.currentTimeMillis();
        logger.info("Starting c0debase");

        tempchannels = new HashMap<>();

        dataManager = initializeDataManager();
        logger.info("Database-Connection set up!");

        jda = initializeJDA();
        logger.info("JDA set up!");

        commandManager = initializeCommandManager();
        logger.info("Command-Manager set up!");

        new GuildVoiceListener(this);

        new MessageReactionListener(this);
        new MessageReceiveListener(this);
        new TableFlipListener(this);

        new GuildMemberJoinListener(this);
        new GuildMemberLeaveListener(this);
        new GuildMemberNickChangeListener(this);
        new GuildMemberRoleListener(this);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                dataManager.close();
            } catch (final Exception exception) {
                exception.printStackTrace();
            }
            jda.shutdown();
        }));
        logger.info(String.format("Startup finished in %dms!", System.currentTimeMillis() - startTime));
    }

    protected DataManager initializeDataManager() throws Exception {
        try {
            return new MongoDataManager(System.getenv("MONGO_HOST") == null ? "localhost" : System.getenv("MONGO_HOST"), System.getenv("MONGO_PORT") == null ? 27017 : Integer.valueOf(System.getenv("MONGO_PORT")), this);
        } catch (final Exception exception) {
            logger.error("Encountered exception while initializing Database-Connection!");
            throw exception;
        }
    }

    protected CommandManager initializeCommandManager() throws Exception {
        try {
            return new CommandManager(this);
        } catch (final Exception exception) {
            logger.error("Encountered exception while initializing Command-Manager!");
            throw exception;
        }
    }

    protected JDA initializeJDA() throws Exception {
        try {
            final JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);
            jdaBuilder.setToken(System.getenv("DISCORD_TOKEN"));
            jdaBuilder.setGame(Game.playing("auf c0debase"));
            jdaBuilder.addEventListener(new GuildReadyListener(this));
            return jdaBuilder.build().awaitReady();
        } catch (Exception exception) {
            logger.error("Encountered exception while initializing ShardManager!");
            throw exception;
        }
    }

    @Override
    public DataManager getDataManager() {
        return dataManager;
    }

    @Override
    public JDA getJDA() {
        return jda;
    }

    @Override
    public CommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public Map<String, Tempchannel> getTempchannels() {
        return tempchannels;
    }
}
