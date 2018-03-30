package de.c0debase.bot.listener.other;

import de.c0debase.bot.CodebaseBot;
import de.c0debase.bot.listener.voice.DynamicVoiceChannelManager;
import de.c0debase.bot.tempchannel.Tempchannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.utils.PermissionUtil;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Biosphere
 * @date 23.01.18
 */

public class ReadyListener extends ListenerAdapter {
    private static final List<Class> exceptions;

    static {
        exceptions = Arrays.asList(ReadyListener.class, DynamicVoiceChannelManager.class);
    }

    @Override
    public void onReady(ReadyEvent event) {
        super.onReady(event);

        event.getJDA().getGuilds().get(0).getMembers().forEach(member -> {
            if (!member.getUser().isBot()) {
                CodebaseBot.getInstance().getLevelManager().load(member.getUser().getId());
                if (CodebaseBot.getInstance().getLevelManager().getLevelUser(member.getUser().getId()).getLevel() >= 3 && !member.getGuild().getRolesByName("Projekt", true).isEmpty()) {
                    Role role = member.getGuild().getRolesByName("Projekt", true).get(0);
                    if (PermissionUtil.canInteract(member.getGuild().getSelfMember(), role)) {
                        member.getGuild().getController().addRolesToMember(member, role).queue();
                    }
                }
            }
        });

        Set<Class<? extends ListenerAdapter>> classes = new Reflections("de.c0debase.bot.listener").getSubTypesOf(ListenerAdapter.class);
        classes.forEach(listenerClass -> {
            if(!exceptions.contains(listenerClass)) {
                try {
                    event.getJDA().addEventListener(listenerClass.newInstance());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        for (VoiceChannel voiceChannel : event.getJDA().getGuilds().get(0).getVoiceChannels()) {
            String name = ("temp-" + voiceChannel.getName().toLowerCase()).replaceAll("\\s+", "-");
            final TextChannel textChannel = voiceChannel.getGuild().getTextChannelsByName(name, true).isEmpty() ? null : voiceChannel.getGuild().getTextChannelsByName(name, true).get(0);
            if (textChannel == null) {
                CodebaseBot.getInstance().getTempchannels().put(voiceChannel.getId(), new Tempchannel());
            } else {
                Tempchannel tempchannel = new Tempchannel(textChannel);
                tempchannel.onLoad(textChannel, voiceChannel);
                CodebaseBot.getInstance().getTempchannels().put(voiceChannel.getId(), tempchannel);
            }
        }
        CodebaseBot.getInstance().getLevelManager().startInviteChecker();
    }

}