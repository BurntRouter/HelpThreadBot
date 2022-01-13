package info.burntrouter.HelpThreadBot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLite {
    private final Connection connection;

    public SQLite(String connection) throws SQLException {
        this.connection = DriverManager.getConnection(connection);
        this.connection.createStatement();
    }

    public PreparedStatement getStatement(String sql) throws SQLException{
        return this.connection.prepareStatement(sql);
    }
}
