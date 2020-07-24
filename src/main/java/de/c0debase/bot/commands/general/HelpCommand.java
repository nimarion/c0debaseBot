package de.c0debase.bot.commands.general;

import de.c0debase.bot.commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "Zeigt diese Hilfe", Category.GENERAL, "hilfe");
    }

    @Override
    public void execute(final String[] args, final Message message) {
        final EmbedBuilder embedBuilder = getEmbed(message.getGuild(), message.getAuthor());
        final Collection<Command> commandCollection = bot.getCommandManager().getAvailableCommands();

        if (args.length == 0) {
            embedBuilder.setTitle("Command Übersicht");
            appendCommandOverview(commandCollection, embedBuilder);
        } else {
            addCommandDescription(args[0], commandCollection, embedBuilder);
        }
        message.getTextChannel().sendMessage(embedBuilder.build()).queue();
    }

    private void appendCommandOverview(final Collection<Command> collection, final EmbedBuilder embedBuilder) {
        collection.stream().collect(Collectors.groupingBy(Command::getCategory)).entrySet().stream()
                .sorted((entry1, entry2) -> {
                    final int sizeComparison = entry2.getValue().size() - entry1.getValue().size();
                    return sizeComparison != 0 ? sizeComparison : entry1.getKey().compareTo(entry2.getKey());
                }).forEach(entry -> {
                    final List<Command> commandList = entry.getValue();
                    final String categoryCommands = commandList.stream().map(Command::getCommand)
                            .sorted(String::compareTo).map(string -> String.format("`%s`", string))
                            .collect(Collectors.joining("  "));
                    embedBuilder.addField(entry.getKey().toString(), categoryCommands, false);
                });
    }

    private void addCommandDescription(final String commandName, final Collection<Command> collection,
            final EmbedBuilder embedBuilder) {
        final Optional<Command> optCommand = collection.stream()
                .filter(command -> command.getCommand().equals(commandName)).findFirst();
        optCommand.ifPresentOrElse(command -> {
            embedBuilder.addField(command.getCommand(), command.getDescription(), false);
        }, () -> embedBuilder.setDescription("**Command wurde nicht gefunden**"));
    }
}
