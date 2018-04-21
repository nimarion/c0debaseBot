package de.c0debase.bot.commands.staff;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author Biosphere
 * @date 21.04.18
 */
public class EvalCommand extends Command {

    private static final ScriptEngine SCRIPT_ENGINE = new ScriptEngineManager().getEngineByName("Nashorn");

    public EvalCommand() {
        super("eval", "", Categorie.STAFF);
    }

    @Override
    public void execute(String[] args, Message msg) {
        try {
            SCRIPT_ENGINE.eval("var imports = new JavaImporter(" +
                    "Packages.net.dv8tion.jda.core," +
                    "java.lang," +
                    "java.lang.management," +
                    "java.text," +
                    "java.util," +
                    "java.time" +
                    ");");
        } catch (ScriptException er) {
            er.printStackTrace();
        }

        SCRIPT_ENGINE.put("channel", msg.getTextChannel());
        SCRIPT_ENGINE.put("author", msg.getAuthor());
        SCRIPT_ENGINE.put("jda", msg.getJDA());
        SCRIPT_ENGINE.put("guild", msg.getGuild());
        SCRIPT_ENGINE.put("message", msg);

        try {
            Object out = SCRIPT_ENGINE.eval("{with (imports) {" + String.join(" ", args) + "}};");
            if (out != null) {
                new MessageBuilder().appendCodeBlock(out.toString(), "Java").buildAll(MessageBuilder.SplitPolicy.NEWLINE, MessageBuilder.SplitPolicy.SPACE, MessageBuilder.SplitPolicy.ANYWHERE).forEach(message -> msg.getTextChannel().sendMessage(message).queue());
            }
        } catch (Exception exception) {
            msg.getTextChannel().sendMessage(getEmbed(msg.getGuild(), msg.getAuthor()).setTitle("An exception was thrown").setDescription(exception.toString()).build()).queue();
        }
    }
}
