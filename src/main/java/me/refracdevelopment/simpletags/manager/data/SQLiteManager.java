package me.refracdevelopment.simpletags.manager.data;

import me.refracdevelopment.simpletags.utilities.Tasks;
import me.refracdevelopment.simpletags.utilities.chat.RyMessageUtils;
import org.bukkit.Bukkit;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLiteManager {

    private SQLiteDataSource dataSource;

    public SQLiteManager(String path) {
        RyMessageUtils.sendConsole(true, "&aEnabling SQLite support.");
        Exception ex = connect(path);

        if (ex != null) {
            RyMessageUtils.sendConsole(true, "&cThere was an error connecting to your database. Here's the suspect: &e" + ex.getLocalizedMessage());
            ex.printStackTrace();
            Bukkit.shutdown();
        } else
            RyMessageUtils.sendConsole(true, "&aManaged to successfully connect to: &e" + path + "&a!");

        createT();
    }

    public void createT() {
        Tasks.runAsync(this::createTables);
    }

    public Exception connect(String path) {
        try {
            Class.forName("org.sqlite.JDBC");
            dataSource = new SQLiteDataSource();
            dataSource.setUrl("jdbc:sqlite:" + path);
        } catch (Exception exception) {
            dataSource = null;
            return exception;
        }
        return null;
    }

    public void shutdown() {
        close();
    }

    public void createTables() {
        createTable("SimpleTags", "uuid VARCHAR(36) NOT NULL PRIMARY KEY, name VARCHAR(16), tag VARCHAR(50), tagPrefix VARCHAR(50)");
    }

    public boolean isInitiated() {
        return dataSource != null;
    }

    public void close() {
        try {
            getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * @return A new database connecting, provided by the Hikari pool.
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Create a new table in the database.
     *
     * @param name The name of the table.
     * @param info The table info between the round VALUES() brackets.
     */
    public void createTable(String name, String info) {
        new Thread(() -> {
            try (Connection resource = getConnection(); PreparedStatement statement = resource.prepareStatement("CREATE TABLE IF NOT EXISTS " + name + "(" + info + ");")) {
                statement.execute();
                statement.closeOnCompletion();
            } catch (SQLException exception) {
                RyMessageUtils.sendConsole(true, "An error occurred while creating database table " + name + ".");
                exception.printStackTrace();
            } finally {
                Thread.currentThread().interrupt();
            }

            Thread.currentThread().interrupt();
        }).start();
    }

    /**
     * Execute an update to the database.
     *
     * @param query  The statement to the database.
     * @param values The values to be inserted into the statement.
     */
    public void execute(String query, Object... values) {
        new Thread(() -> {
            try (Connection resource = getConnection(); PreparedStatement statement = resource.prepareStatement(query)) {
                for (int i = 0; i < values.length; i++)
                    statement.setObject((i + 1), values[i]);

                statement.execute();
                statement.closeOnCompletion();
            } catch (SQLException exception) {
                RyMessageUtils.sendConsole(true, "An error occurred while executing an update on the database.");
                RyMessageUtils.sendConsole(true, "SQLite#execute : " + query);
                exception.printStackTrace();
            } finally {
                Thread.currentThread().interrupt();
            }

            Thread.currentThread().interrupt();
        }).start();
    }

    /**
     * Execute a query to the database.
     *
     * @param query    The statement to the database.
     * @param callback The data callback (Async).
     * @param values   The values to be inserted into the statement.
     */
    public void select(String query, SelectCall callback, Object... values) {
        new Thread(() -> {
            try (Connection resource = getConnection(); PreparedStatement statement = resource.prepareStatement(query)) {
                for (int i = 0; i < values.length; i++)
                    statement.setObject((i + 1), values[i]);

                callback.call(statement.executeQuery());
                statement.closeOnCompletion();
            } catch (SQLException exception) {
                RyMessageUtils.sendConsole(true, "An error occurred while executing a query on the database.");
                RyMessageUtils.sendConsole(true, "SQLite#select : " + query);
                exception.printStackTrace();
            } finally {
                Thread.currentThread().interrupt();
            }

            Thread.currentThread().interrupt();
        }).start();
    }

    public void updatePlayerTag(String uuid, String tag, String tagPrefix) {
        execute("UPDATE SimpleTags SET tag=?, tagPrefix=? WHERE uuid=?", tag, tagPrefix, uuid);
    }

    public void updatePlayerName(String uuid, String name) {
        execute("UPDATE SimpleTags SET name=? WHERE uuid=?", name, uuid);
    }

    public void delete() {
        execute("TRUNCATE TABLE SimpleTags");
    }

    public void deletePlayer(String uuid) {
        execute("DELETE FROM SimpleTags WHERE uuid=?", uuid);
    }
}