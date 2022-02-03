package info.burntrouter.HelpThreadBot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ThreadChannel;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateArchivedEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class Listener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.isFromThread()) {
            ThreadChannel threadChannel = event.getThreadChannel();
            String threadId = threadChannel.getId();
            String userId = event.getMember().getId();
            if(threadChannel.getHistoryFromBeginning(100).complete().size() == 3 && !event.getMember().getUser().isBot()) {
                if(Database.isThreadOwner(userId, threadId)) {
                    String title = StringUtils.truncate(event.getMessage().getContentStripped().trim(), 100);

                    Database.setThreadTitle(threadId, title);
                    threadChannel.sendMessage("Thank you! Now please provide as much detail as possible so you can be best helped!").queue();
                    event.getThreadChannel().getManager().setName(title).queue();
                } else {
                    event.getMessage().delete().queue();
                }
            }
        }
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        String userid = event.getUser().getId();
        if(event.getButton().getId().equals("help") && !Database.hasOpenThread(event.getUser().getId())) {
            ThreadChannel threadChannel = Bot.textChannel.createThreadChannel(event.getMember().getEffectiveName() + "\'s Thread").complete();
            threadChannel.addThreadMemberById(event.getMember().getId()).queue();
            threadChannel.sendMessage(event.getMember().getAsMention() + " please respond with a **4-5 word summary** to set the thread's title as.\n" +
                    "Ex: `My Pi won't power on.`").queue();
            Database.createThread(userid, threadChannel.getId());
        } else {
            event.getHook().getInteraction().reply("You can't open another thread while you have one open!\n" +
                    "Use `/close` to close your active thread.").setEphemeral(true).queue();
        }
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if(event.getCommandString().replace("/", "").contains("close")) {
            if(event.getChannelType().isThread()) {
                if(event.getMember().hasPermission(Permission.MANAGE_THREADS) ||
                        Database.isThreadOwner(event.getMember().getId(), event.getChannel().getId())) {
                    event.getHook().getInteraction().reply("Thread has been closed!").setEphemeral(true).complete();
                    event.getGuild().getThreadChannelById(event.getChannel().getId()).getManager().setArchived(true).queue();
                }
            } else {
                event.getHook().getInteraction().reply("This must be used in a thread!").setEphemeral(true).queue();
            }
        }
    }

    @Override
    public void onChannelUpdateArchived(ChannelUpdateArchivedEvent event) {
        Database.closeThread(event.getChannel().getId());
    }

    @Override
    public void onChannelCreate(ChannelCreateEvent event) {
        if(event.getChannel().getType().isThread()) {
            Bot.helpMessage.delete().queue();
            Bot.sendHelpMessage();

            String threadId = event.getChannel().getId();
            new ThreadWatch(Bot.api.getThreadChannelById(threadId));
        }
    }

    @Override
    public void onChannelDelete(ChannelDeleteEvent event)
    {
        if(event.getChannel().getType().isThread()) {
            Database.closeThread(event.getChannel().getId());
        }
    }
    public static class ThreadWatch extends Thread {
        private ThreadChannel threadChannel;

        public ThreadWatch(ThreadChannel threadChannel) {
            try {
                this.threadChannel = threadChannel;
                this.start();
                this.setName("Threadwatch");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        @Override
        public void run() {
            try {
                sleep(300000);
                    if(threadChannel.getHistoryFromBeginning(100).complete().size() == 2) {
                        Database.closeThread(threadChannel.getId());
                        threadChannel.delete().queue();
                    }
                } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
