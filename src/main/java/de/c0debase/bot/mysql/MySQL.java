package de.c0debase.bot.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class MySQL {

    private HikariDataSource dataSource;
    private ExecutorService executor;

    public MySQL(String host, String user, String password, String database, int port) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(user);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
        this.executor = Executors.newCachedThreadPool();
    }

    public void update(final String query) {
        try (final Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void updateAsync(final String query) {
        executor.execute(() -> update(query));
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
