package info.burntrouter.HelpThreadBot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQL {
    private final Connection connection;

    public MySQL(String driver, String connection)
            throws SQLException, ClassNotFoundException
    {
        Class.forName(driver);

        this.connection = DriverManager.getConnection(connection);

        this.connection.createStatement();
    }

    public PreparedStatement getStatement(String sql)
            throws SQLException
    {
        return this.connection.prepareStatement(sql);
    }
}
