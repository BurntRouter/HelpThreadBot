package info.burntrouter.HelpThreadBot;

public class Launcher {
    public static void main(String[] args) {
        System.out.println("Starting HelpThreadBot");
        Config.testConfig();
        Database.connect();
        new Bot();
    }
}
