package de.c0debase.bot.commands.general;

import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.commands.Command;
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
    public void execute(String[] args, Message msg) {
        JDA jda = msg.getJDA();

        EmbedBuilder eb = getEmbed(msg.getGuild(), msg.getAuthor());
        eb.addField("JDA Version", JDAInfo.VERSION, true);
        eb.addField("Ping", jda.getPing() + "ms", true);
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        eb.addField("Uptime", String.valueOf(TimeUnit.MILLISECONDS.toDays(uptime) + "d " + TimeUnit.MILLISECONDS.toHours(uptime) % 24 + "h " + TimeUnit.MILLISECONDS.toMinutes(uptime) % 60 + "m " + TimeUnit.MILLISECONDS.toSeconds(uptime) % 60 + "s"), true);
        eb.addField("Commands", String.valueOf(CodebaseBot.getInstance().getCommandManager().getAvailableCommands().size()), true);
        eb.addField("Mitglieder", String.valueOf(jda.getUserCache().size()), true);
        eb.addField("Java Version", System.getProperty("java.runtime.version").replace("+", "_"), true);
        eb.addField("Betriebssystem", ManagementFactory.getOperatingSystemMXBean().getName(), true);
        eb.addField("CPU Threads", String.valueOf(ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors()), true);
        eb.addField("RAM Usage", (ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() + ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed()) / 1000000 + " / " + (ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax() + ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getMax()) / 1000000 + " MB", true);
        eb.addField("Threads", String.valueOf(Thread.activeCount()), true);
        msg.getChannel().sendMessage(eb.build()).queue();
    }
}


