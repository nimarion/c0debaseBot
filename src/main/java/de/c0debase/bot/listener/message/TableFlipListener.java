package de.c0debase.bot.listener.message;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class TableFlipListener extends ListenerAdapter {
    private static final String TABLE_FLIP = "(╯°□°）╯︵ ┻━┻";
    private static final String UNFLIP = "┬─┬ ノ( ゜-゜ノ)";

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().equals(TABLE_FLIP)) {
            event.getChannel().sendMessage(UNFLIP).queue(null, ignored -> {});
        }
    }
}
