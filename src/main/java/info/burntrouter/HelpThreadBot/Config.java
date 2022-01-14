package info.burntrouter.HelpThreadBot;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class Config {
    private static JSONObject configJSON;

    private static final String configPath = "./config.json";

    public Config() {
        try {
            configJSON = new JSONObject(FileUtils.readFileToString(new File(configPath), Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
            createConfig();
            System.exit(1);
        }

    }

    public static void testConfig() {
        try {
            configJSON = new JSONObject(FileUtils.readFileToString(new File(configPath), Charset.defaultCharset()));
        } catch (Exception ignored){
            createConfig();
        }
    }

    public static String getToken() {
        try {
            return configJSON.getString("token");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public static String getGuildId() {
        try {
            return configJSON.getString("guildid");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public static String getChannelId() {
        try {
            return configJSON.getString("channelid");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public static String getDbHost() {
        try {
            return configJSON.getString("db_hostname");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public static String getDbPort() {
        try {
            return configJSON.getString("db_port");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public static String getDbName() {
        try {
            return configJSON.getString("db_name");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public static String getDbUser() {
        try {
            return configJSON.getString("db_user");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public static String getDbPass() {
        try {
            return configJSON.getString("db_pass");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    private static void createConfig() {
        try {
            JSONObject config = new JSONObject();
            config.put("token", "DISCORD TOKEN HERE");
            config.put("guildid", "GUILDID HERE");
            config.put("channelid", "CHANNEL ID HERE");
            config.put("db_hostname", "127.0.0.1");
            config.put("db_port", "3306");
            config.put("db_name", "HelpThreadBot");
            config.put("db_user", "root");
            config.put("db_pass", "password");

            FileUtils.writeStringToFile(new File(configPath), config.toString(1), Charset.defaultCharset());
            System.out.println("CREATED CONFIG.JSON FILE");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: COULD NOT CREATE CONFIG FILE");
            System.exit(1);
        }
    }
}
