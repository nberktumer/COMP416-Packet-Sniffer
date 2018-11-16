package server.data;

import java.sql.*;

public class SQLiteStorage implements IKeyStorage {
    private static final String JDBC_CONNECTION_STRING = "jdbc:sqlite:data_sqlite.db";

    private Connection connection;

    public SQLiteStorage() {
        initialize();
    }

    /**
     * Returns the value from the saved map
     *
     * @param key key value to get the value from the map
     * @return the value according to the given key
     */
    @Override
    public String getKey(String key) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM SERVER WHERE id=?")) {
            statement.setString(1, key);
            ResultSet result = statement.executeQuery();
            return result.getString("value");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks whether the key exists
     *
     * @param key key value to be checked
     * @return true if the given key exists
     */
    @Override
    public boolean containsKey(String key) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) > 0 AS hasValue FROM SERVER WHERE id=?")) {
            statement.setString(1, key);
            ResultSet result = statement.executeQuery();
            return result.getBoolean("hasValue");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Saves a new key, value pair to the map. If the key already exists, it overrides the old value.
     *
     * @param key   key to be saved
     * @param value value to be saved
     */
    @Override
    public synchronized void setKey(String key, String value) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO SERVER VALUES (?, ?)")) {
            statement.setString(1, key);
            statement.setString(2, value);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the SQLite database connection
     */
    private synchronized void initialize() {
        try {
            connection = DriverManager.getConnection(JDBC_CONNECTION_STRING);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS SERVER (id TEXT PRIMARY KEY, value TEXT NOT NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
