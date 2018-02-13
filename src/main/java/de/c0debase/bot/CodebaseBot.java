package de.c0debase.bot;

import de.c0debase.bot.commands.CommandManager;
import de.c0debase.bot.level.LevelManager;
import de.c0debase.bot.level.LevelUser;
import de.c0debase.bot.listener.other.ReadyListener;
import de.c0debase.bot.music.MusicManager;
import de.c0debase.bot.mysql.MySQL;
import de.c0debase.bot.utils.Pagination;
import io.sentry.Sentry;
import lombok.Getter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger("de.c0debase.bot");
    private MusicManager musicManager;
    private JDA jda;

    private CodebaseBot() {
        instance = this;

        mySQL = new MySQL(System.getenv("MYSQL-HOSTNAME") == null ? "sqlserver" : System.getenv("MYSQL-HOSTNAME"), System.getenv("MYSQL-USERNAME"), System.getenv("MYSQL-PASSWORT"), System.getenv("MYSQL-DATABASE") == null ? "codebase" : System.getenv("MYSQL-DATABASE"), System.getenv("MYSQL-PORT") == null ? 3306 : Integer.valueOf(System.getenv("MYSQL-PORT")));
        mySQL.connect();
        mySQL.update("CREATE TABLE IF NOT EXISTS Users (ID VARCHAR(50),XP int,LEVEL int);");

        commandManager = new CommandManager();
        levelManager = new LevelManager();
        leaderboardPagination = new Pagination<>(levelManager.getLevelUsersSorted(), 10);

        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(System.getenv("DISCORD-TOKEN"))
                    .setEnableShutdownHook(true)
                    .setGame(Game.of(Game.GameType.DEFAULT, "auf c0debase"))
                    .setStatus(OnlineStatus.ONLINE)
                    .setAutoReconnect(true)
                    .setAudioEnabled(true)
                    .addEventListener(new ReadyListener())
                    .buildBlocking();
            musicManager = new MusicManager(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(mySQL::disconnect));
    }



    public static void main(String... args) {
        if(System.getenv("SENTRY_DSN") == null && System.getProperty("sentry.properties") == null){
            System.setProperty("sentry.properties", "https://3454dcf83f3d47208654a0ac282d4d00@sentry.io/287392");
        }
        Sentry.init();
        new CodebaseBot();
    }

}
