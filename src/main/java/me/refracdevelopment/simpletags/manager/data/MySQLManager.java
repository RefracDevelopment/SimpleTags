package me.refracdevelopment.simpletags.manager.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.refracdevelopment.simpletags.SimpleTags;
import me.refracdevelopment.simpletags.utilities.Tasks;
import me.refracdevelopment.simpletags.utilities.chat.RyMessageUtils;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLManager {

    private final String host = SimpleTags.getInstance().getConfigFile().getString("mysql.host");
    private final String username = SimpleTags.getInstance().getConfigFile().getString("mysql.username");
    private final String password = SimpleTags.getInstance().getConfigFile().getString("mysql.password");
    private final String database = SimpleTags.getInstance().getConfigFile().getString("mysql.database");
    private final String port = SimpleTags.getInstance().getConfigFile().getString("mysql.port");
    private HikariDataSource hikariDataSource;

    public MySQLManager() {
        RyMessageUtils.sendConsole(true, "&aEnabling MySQL support.");
        Exception ex = connect();

        if (ex != null) {
            RyMessageUtils.sendConsole(true, "&cThere was an error connecting to your database. Here's the suspect: &e" + ex.getLocalizedMessage());
            ex.printStackTrace();
            Bukkit.shutdown();
        } else
            RyMessageUtils.sendConsole(true, "&aManaged to successfully connect to: &e" + database + "&a!");

        createT();
    }

    public void createT() {
        Tasks.runAsync(this::createTables);
    }

    public Exception connect() {
        try {
            HikariConfig config = new HikariConfig();

            Class.forName("org.mariadb.jdbc.Driver");
            config.setDriverClassName("org.mariadb.jdbc.Driver");
            config.setJdbcUrl("jdbc:mariadb://" + host + ':' + port + '/' + database);
            config.setUsername(username);
            config.setPassword(password);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            hikariDataSource = new HikariDataSource(config);
        } catch (Exception exception) {
            hikariDataSource = null;
            exception.printStackTrace();
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
        return hikariDataSource != null;
    }

    public void close() {
        this.hikariDataSource.close();
    }


    /**
     * @return A new database connecting, provided by the Hikari pool.
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
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
                RyMessageUtils.sendConsole(true, "MySQL#execute : " + query);
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
                RyMessageUtils.sendConsole(true, "MySQL#select : " + query);
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