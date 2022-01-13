package info.burntrouter.HelpThreadBot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ThreadChannel;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateArchivedEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Listener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.isFromThread()) {
            ThreadChannel threadChannel = event.getThreadChannel();
            if(threadChannel.getHistoryFromBeginning(100).complete().size() == 3 && !event.getMember().getUser().isBot()) {
                System.out.println(event.getMessage().getContentStripped());
                threadChannel.sendMessage("Thank you! Now please provide as much detail as possible so you can be best helped!").queue();
                event.getThreadChannel().getManager().setName(event.getMessage().getContentStripped()).queue();
            }
        }
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        String userid = event.getUser().getId();
        if(event.getButton().getId().equals("help")) {
            ThreadChannel threadChannel = Bot.textChannel.createThreadChannel(event.getMember().getEffectiveName() + "s Thread").complete();
            threadChannel.addThreadMemberById(event.getMember().getId()).queue();
            threadChannel.sendMessage(event.getMember().getAsMention() + " please respond with a brief question to set the thread's title as.\n" +
                    "Ex: `My Pi won't power on.`").queue();
        } else {
            event.getHook().setEphemeral(true).sendMessage("You can't open another thread while you have one open!\n" +
                    "Use `/close` to close your active thread.").queue();
        }
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        System.out.println("GOT COMMAND");
        if(event.getCommandString().replace("/", "").contains("close")) {
            System.out.println("COMMAND CLOSE");
            if(event.getChannelType().isThread()) {
                if(event.getMember().hasPermission(Permission.MANAGE_THREADS)) {
                    event.getHook().setEphemeral(true).getInteraction().reply("Thread has been closed!").complete();
                    event.getGuild().getThreadChannelById(event.getChannel().getId()).getManager().setArchived(true).queue();
                }
            } else {
                System.out.println("RESPONDING");
                event.getHook().getInteraction().reply("This must be used in a thread!").queue();
            }
        }
    }

    @Override
    public void onChannelUpdateArchived(ChannelUpdateArchivedEvent event) {
    }

    @Override
    public void onChannelCreate(ChannelCreateEvent event) {
        if(event.getChannel().getType().isThread()) {
            Bot.helpMessage.delete().queue();
            Bot.sendHelpMessage();
        }
    }
}
