package de.c0debase.bot.mysql;

import java.sql.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class MySQL {

    private Connection connection;
    private String host, user, password, database;
    private int port;
    private ExecutorService executor;

    public MySQL(String host, String user, String password, String database, int port) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.database = database;
        this.port = port;

        this.executor = Executors.newCachedThreadPool();
    }

    public void connect() {
        if (!isConnected()) {
            try {
                this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true", user, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void update(final String query) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAsync(final String query) {
        executor.execute(() -> update(query));
    }

    public ResultSet query(final String query) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
