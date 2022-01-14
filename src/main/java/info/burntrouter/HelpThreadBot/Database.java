package info.burntrouter.HelpThreadBot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    public static MySQL mySQL;

    public Database() {
        connect();
    }

    public static void connect() {
        try {
            mySQL = new MySQL("com.mysql.jdbc.Driver", "jdbc:mysql://" + Config.getDbHost() + ":" + Config.getDbPort() + "/" + Config.getDbName() + "?autoReconnect=true&user=" + Config.getDbUser() + "&password=" + Config.getDbPass());
        } catch (SQLException | ClassNotFoundException e) {
            mySQL = null;
            e.printStackTrace();
            System.exit(2);
        }
    }

    public static void createThread(String userid, String threadid) {
    try {
        PreparedStatement preparedStatement = mySQL.getStatement("INSERT INTO threads (userid, threadid) VALUES (?, ?)");
        preparedStatement.setString(1, userid);
        preparedStatement.setString(2, threadid);
        preparedStatement.execute();
        preparedStatement.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    public static boolean isThreadOwner(String userid, String threadid) {
        try {
            PreparedStatement preparedStatement = mySQL.getStatement("SELECT userid FROM threads WHERE threadid = ?");
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
        List<Boolean> intList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = mySQL.getStatement("SELECT threadClosed FROM threads WHERE userid = ?");
            preparedStatement.setString(1, userid);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                intList.add(resultSet.getBoolean("threadClosed"));
            }
            resultSet.close();
            preparedStatement.close();
            for (boolean bool :
                    intList) {
                if (!bool) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static void closeThread(String threadid) {
        try {
            PreparedStatement preparedStatement = mySQL.getStatement("UPDATE threads SET threadClosed = 1 WHERE threadid = ?");
            preparedStatement.setString(1, threadid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void setThreadTitle(String threadId, String threadTitle) {
        try {
            PreparedStatement preparedStatement = mySQL.getStatement("UPDATE threads SET threadTitle = ? WHERE threadid = ?");
            preparedStatement.setString(1, threadTitle);
            preparedStatement.setString(2, threadId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
