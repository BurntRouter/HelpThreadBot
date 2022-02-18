package info.burntrouter.HelpThreadBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Bot extends Thread {
    public static JDA api;
    public static Message helpMessage;
    public static Guild guild;
    public static TextChannel textChannel;
    public static Role managerRole;

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
            Objects.requireNonNull(api.getGuildById(Config.getGuildId())).upsertCommand("close", "Closes the thread. Can be used by the thread owner, a moderator, or a somebody with a manager role.").queue();
            System.out.println("Commands Registered!");

            System.out.println(api.getSelfUser().getName() + " online. Ping: " + api.getRestPing().complete());

            guild = api.getGuildById(Config.getChannelId());
            textChannel = api.getTextChannelById(Config.getChannelId());
            if(Config.getManagerRoleID().matches("\\d+")) {
                managerRole = guild.getRoleById(Config.getManagerRoleID());
            }
            sendHelpMessage();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CRASHED!");
            System.exit(2);
        }
    }

    public static void sendHelpMessage() {
        List<Button> buttons = new ArrayList<>();
        buttons.add(Button.primary("help", "Get Help"));
        UrlValidator urlValidator = new UrlValidator();
        if(urlValidator.isValid(Config.getFAQLink())) {
            buttons.add(Button.link("faq", Config.getFAQLink()));
        } else {
            System.out.println("Invalid FAQ link! Skipping...");
        }

        helpMessage = textChannel.sendMessage("Welcome to " + textChannel.getAsMention() + "!\n" +
                        "Before creating a help thread please click the FAQ button.\n" +
                        "If you need help with anything tech related please click `Get Help` button below.\n")
                .setActionRow(buttons).complete();

    }
}
