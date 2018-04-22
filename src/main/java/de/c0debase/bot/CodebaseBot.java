package de.c0debase.bot;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import de.c0debase.bot.commands.CommandManager;
import de.c0debase.bot.level.LevelManager;
import de.c0debase.bot.level.LevelUser;
import de.c0debase.bot.listener.other.ReadyListener;
import de.c0debase.bot.listener.voice.DynamicVoiceChannelManager;
import de.c0debase.bot.monitor.MonitorManager;
import de.c0debase.bot.music.MusicManager;
import de.c0debase.bot.mysql.MySQL;
import de.c0debase.bot.tempchannel.Tempchannel;
import de.c0debase.bot.utils.Constants;
import de.c0debase.bot.utils.Pagination;
import io.sentry.Sentry;
import lombok.Getter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Biosphere
 * @date 23.01.18
 */

@Getter
public class CodebaseBot {

    @Getter
    private static CodebaseBot instance;
    private final CommandManager commandManager;
    private final LevelManager levelManager;
    private final Pagination<LevelUser> leaderboardPagination;
    private final MySQL mySQL;
    private final MonitorManager monitorManager;
    private final Logger logger = LoggerFactory.getLogger("de.c0debase.bot");
    private final HashMap<String, Tempchannel> tempchannels;
    private final ScheduledExecutorService executorService;
    private JDA jda;
    private AIDataService aiDataService;
    private MusicManager musicManager;


    private CodebaseBot() {
        instance = this;

        mySQL = new MySQL(System.getenv("MYSQL-HOSTNAME") == null ? "sqlserver" : System.getenv("MYSQL-HOSTNAME"), System.getenv("MYSQL-USERNAME"), System.getenv("MYSQL-PASSWORT"), System.getenv("MYSQL-DATABASE") == null ? "codebase" : System.getenv("MYSQL-DATABASE"), System.getenv("MYSQL-PORT") == null ? 3306 : Integer.valueOf(System.getenv("MYSQL-PORT")));
        mySQL.update("CREATE TABLE IF NOT EXISTS Users (ID VARCHAR(50),XP int,LEVEL int);");

        executorService = Executors.newScheduledThreadPool(1);
        commandManager = new CommandManager();
        monitorManager = new MonitorManager();
        levelManager = new LevelManager();
        tempchannels = new HashMap<>();
        leaderboardPagination = new Pagination<>(levelManager.getLevelUsersSorted(), 10);
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
                    .addEventListener(new ListenerAdapter() {
                        @Override
                        public void onGenericEvent(Event event) {
                            super.onGenericEvent(event);
                            monitorManager.trigger(event.getClass());
                        }
                    })
                    .buildBlocking();
            musicManager = new MusicManager(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        executorService.scheduleAtFixedRate(() -> {
            final String day = Constants.simpleDateFormat.format(new Date());
            try (final Connection connection = CodebaseBot.getInstance().getMySQL().getConnection()) {
                ResultSet resultSet = connection.prepareStatement("SELECT * FROM MemberMonitor WHERE DAY='" + day + "';").executeQuery();
                if (resultSet.next()) {
                    CodebaseBot.getInstance().getMySQL().updateAsync("UPDATE MemberMonitor SET MEMBER='" + jda.getGuildById("361448651748540426").getMembers().size() + "' WHERE DAY='" + day + "';");
                } else {
                    CodebaseBot.getInstance().getMySQL().update("INSERT INTO MemberMonitor (DAY, MEMBER) VALUES ('" + day + "'," + jda.getGuildById("361448651748540426").getMembers().size() + ");");
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }, 0, 5, TimeUnit.MINUTES);
        Runtime.getRuntime().addShutdownHook(new Thread(executorService::shutdown));
    }


    public static void main(String... args) {
        if (System.getenv("SENTRY_DSN") != null || System.getProperty("sentry.properties") != null) {
            Sentry.init();
        }
        new CodebaseBot();
    }

}
