package info.burntrouter.HelpThreadBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;

public class Bot extends Thread {
    public static JDA api;
    public static Message helpMessage;
    public static Guild guild;
    public static TextChannel textChannel;

    public Bot() {
        setup();
        start();
    }

    private void setup() {
        try {
            System.out.println("Help Threads Bot is warming up!");
            api = JDABuilder.createDefault(Config.getToken()).setChunkingFilter(ChunkingFilter.NONE).setActivity(Activity.watching("The help threads"))
                    .setEnabledIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_EMOJIS).build();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CRASHED!");
            System.exit(2);
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Registering Listener");
            api.addEventListener(new Listener());
            System.out.println("Listener Registered!");

            System.out.println("Connecting to Discord...");
            api.awaitReady();
            System.out.println("Connected!");

            System.out.println("Registering Commands");
            api.getGuildById(Config.getGuildId()).upsertCommand("close", "Closes the thread. Can be used by the thread owner, a moderator, or a somebody with a manager role.").queue();
            System.out.println("Commands Registered!");

            System.out.println(api.getSelfUser().getName() + " online. Ping: " + api.getRestPing().complete());

            guild = api.getGuildById(Config.getChannelId());
            textChannel = api.getTextChannelById(Config.getChannelId());
            sendHelpMessage();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CRASHED!");
            System.exit(2);
        }
    }

    public static void sendHelpMessage() {
        helpMessage = textChannel.sendMessage("Welcome to " + textChannel.getAsMention() + "!\n" +
                        "Before creating a help thread please click the FAQ button.\n" +
                        "If you need help with anything tech related please click `Get Help` button below.\n")
                .setActionRow(Button.link("https://discord.com/channels/204621105720328193/915461470257565737", "FAQ"),
                        Button.primary("help", "Get Help")).complete();
    }
}
