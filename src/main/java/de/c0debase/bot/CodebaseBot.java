package de.c0debase.bot;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import de.c0debase.bot.commands.CommandManager;
import de.c0debase.bot.database.MongoDataManager;
import de.c0debase.bot.listener.other.ReadyListener;
import de.c0debase.bot.listener.voice.DynamicVoiceChannelManager;
import de.c0debase.bot.music.MusicManager;
import de.c0debase.bot.tempchannel.Tempchannel;
import io.sentry.Sentry;
import lombok.Getter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Biosphere
 * @date 23.01.18
 */

@Getter
public class CodebaseBot {

    @Getter
    private static CodebaseBot instance;
    private final CommandManager commandManager;
    private final MongoDataManager mongoDataManager;
    private final Logger logger = LoggerFactory.getLogger("de.c0debase.bot");
    private final HashMap<String, Tempchannel> tempchannels;
    private final ScheduledExecutorService executorService;
    private JDA jda;
    private AIDataService aiDataService;
    private MusicManager musicManager;


    private CodebaseBot() {
        instance = this;
        logger.info("Starting c0debaseBot");
        mongoDataManager = new MongoDataManager();

        executorService = Executors.newScheduledThreadPool(1);
        commandManager = new CommandManager();
        tempchannels = new HashMap<>();
        if (System.getenv("APIAI-TOKEN") != null) {
            aiDataService = new AIDataService(new AIConfiguration(System.getenv("APIAI-TOKEN")));
        }


        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(System.getenv("DISCORD-TOKEN"))
                    .setEnableShutdownHook(true)
                    .setGame(Game.of(Game.GameType.LISTENING, "!help"))
                    .setStatus(OnlineStatus.ONLINE)
                    .setAutoReconnect(true)
                    .setAudioEnabled(true)
                    .addEventListener(new ReadyListener())
                    .addEventListener(new DynamicVoiceChannelManager("Talk "))
                    .addEventListener(new DynamicVoiceChannelManager("Ingame "))
                    .buildBlocking();
            musicManager = new MusicManager(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(executorService::shutdown));
    }


    public static void main(String... args) {
        if (System.getenv("SENTRY_DSN") != null || System.getProperty("sentry.properties") != null) {
            Sentry.init();
        }
        new CodebaseBot();
    }

}
