package info.burntrouter.HelpThreadBot;

import java.sql.*;
import java.util.List;

public class Database {
    public static final String dbPath = "database.db";

    public Database() {
        try {
            connect();
        } catch (Exception e) {
            createNewDatabase();
        }

    }

    public static Connection connect() {
        String url = "jdbc:sqlite:"+dbPath;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void createNewDatabase() {
        try (Connection connection = DriverManager.getConnection(dbPath)) {
            DatabaseMetaData metaData = connection.getMetaData();
            createTables();
            System.out.println("New Database Created");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTables() throws SQLException {
        connect().prepareStatement("CREATE TABLE threads" +
                " (userid TEXT, " +
                "threadid TEXT PRIMARY KEY, " +
                "threadClosed INTEGER NOT NULL DEFAULT '0')").execute();
    }

    public static void createThread(String userid, String threadid) {
    try {
        PreparedStatement preparedStatement = connect().prepareStatement("INSERT INTO threads (userid, threadid) VALUES (?, ?)");
        preparedStatement.setString(1, userid);
        preparedStatement.setString(2, threadid);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    public static boolean isThreadOwner(String userid, String threadid) {
        try {
            PreparedStatement preparedStatement = connect().prepareStatement("SELECT userid FROM threads WHERE threadid = ?");
            preparedStatement.setString(1, threadid);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String string = resultSet.getString("userid");
            resultSet.close();
            preparedStatement.close();
            return string.equals(userid);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean hasOpenThread(String userid) {
        List<Integer> intList = null;
        try {
            PreparedStatement preparedStatement = connect().prepareStatement("SELECT threadClosed FROM threads WHERE userid = ?");
            preparedStatement.setString(1, userid);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                intList.add(resultSet.getInt("threadClosed"));
            }
            resultSet.close();
            preparedStatement.close();
            return intList.contains(1);
        } catch (Exception e) {
            return false;
        }
    }

    public static void closeThread(String threadid) {
        try {
            PreparedStatement preparedStatement = connect().prepareStatement("UPDATE threads SET threadClosed = 1 WHERE threadid = ?");
            preparedStatement.setString(1, threadid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
