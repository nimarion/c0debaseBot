package de.c0debase.bot.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.c0debase.bot.utils.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class StatsCommand extends Command {

    public StatsCommand() {
        this.name = "stats";
        this.help = "Zeigt dir einige Informationen über den Bot";
        this.aliases = new String[]{"info"};
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        final JDA jda = commandEvent.getJDA();
        final long uptime = ManagementFactory.getRuntimeMXBean().getUptime();

        final EmbedBuilder embedBuilder = EmbedUtils.getEmbed(commandEvent.getAuthor(), true);
        embedBuilder.addField("JDA Version", JDAInfo.VERSION, true);
        embedBuilder.addField("Ping", jda.getGatewayPing() + "ms", true);
        embedBuilder.addField("Uptime", TimeUnit.MILLISECONDS.toDays(uptime) + "d " + TimeUnit.MILLISECONDS.toHours(uptime) % 24 + "h " +
                TimeUnit.MILLISECONDS.toMinutes(uptime) % 60 + "m " +
                TimeUnit.MILLISECONDS.toSeconds(uptime) % 60 + "s", true);
        //embedBuilder.addField("Commands", String.valueOf(bot.getCommandManager().getAvailableCommands().size()), true);
        embedBuilder.addField("Mitglieder", String.valueOf(jda.getUserCache().size()), true);
        embedBuilder.addField("Java Version", System.getProperty("java.runtime.version").replace("+", "_"), true);
        embedBuilder.addField("Betriebssystem", ManagementFactory.getOperatingSystemMXBean().getName(), true);
        embedBuilder.addField("CPU Threads",
                String.valueOf(ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors()), true);
        embedBuilder.addField("RAM Usage", (ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() +
                ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed()) / 1000000 + " / " +
                (ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax() +
                        ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getMax()) / 1000000 + " MB", true);
        embedBuilder.addField("Threads", String.valueOf(Thread.activeCount()), true);

        commandEvent.reply(embedBuilder.build());
    }
}


