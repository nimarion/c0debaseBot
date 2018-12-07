package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.core.Codebase;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDAInfo;
import net.dv8tion.jda.core.entities.Message;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class StatsCommand extends Command {

    public StatsCommand() {
        super("stats", "Zeigt dir einige Informationen über den Bot", Category.GENERAL, "info");
    }

    @Override
    public void execute(final Codebase bot, final String[] args, final Message message) {
        final JDA jda = message.getJDA();
        final long uptime = ManagementFactory.getRuntimeMXBean().getUptime();

        final EmbedBuilder eb = getEmbed(message.getGuild(), message.getAuthor());
        eb.addField("JDA Version", JDAInfo.VERSION, true);
        eb.addField("Ping", jda.getPing() + "ms", true);
        eb.addField("Uptime", String.valueOf(TimeUnit.MILLISECONDS.toDays(uptime) + "d " + TimeUnit.MILLISECONDS.toHours(uptime) % 24 + "h " + TimeUnit.MILLISECONDS.toMinutes(uptime) % 60 + "m " + TimeUnit.MILLISECONDS.toSeconds(uptime) % 60 + "s"), true);
        eb.addField("Commands", String.valueOf(bot.getCommandManager().getAvailableCommands().size()), true);
        eb.addField("Mitglieder", String.valueOf(jda.getUserCache().size()), true);
        eb.addField("Java Version", System.getProperty("java.runtime.version").replace("+", "_"), true);
        eb.addField("Betriebssystem", ManagementFactory.getOperatingSystemMXBean().getName(), true);
        eb.addField("CPU Threads", String.valueOf(ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors()), true);
        eb.addField("RAM Usage", (ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() + ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed()) / 1000000 + " / " + (ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax() + ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getMax()) / 1000000 + " MB", true);
        eb.addField("Threads", String.valueOf(Thread.activeCount()), true);
        message.getChannel().sendMessage(eb.build()).queue();
    }
}


