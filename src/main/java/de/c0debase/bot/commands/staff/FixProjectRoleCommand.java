package de.c0debase.bot.commands.staff;

import de.c0debase.bot.commands.Command;
import de.c0debase.bot.database.data.CodebaseUser;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.managers.GuildController;

//TODO: remove command! temporary command for one-time-use only
public class FixProjectRoleCommand extends Command {

    private static final long PROJECT_ROLE_ID = 408957966998568960L;

    public FixProjectRoleCommand() {
        super("fixprojectrole", "Temporary Command - fix project roles", Category.STAFF);
    }

    @Override
    public void execute(final String[] args, final Message message) {
        final Guild guild = message.getGuild();
        final GuildController guildController = guild.getController();
        final String guildID = guild.getId();
        final Role projectRole = message.getJDA().getRoleById(PROJECT_ROLE_ID);
        int counter = 0;
        for (Member member : guild.getMembers()) {
            final CodebaseUser codebaseUser = bot.getDataManager().getUserData(guildID, member.getUser().getId());
            if (codebaseUser.getLevel() > 2 && !member.getRoles().contains(projectRole)) {
                guildController.addSingleRoleToMember(member, projectRole).queue();
                counter++;
            }
        }
        message.getChannel().sendMessage(String.format("Added Project-Role to %s User", counter)).queue();
    }
}
