package de.c0debase.bot.listener.guild;

import java.util.ArrayList;
import java.util.List;

import de.c0debase.bot.core.Codebase;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostCountEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildBoostListener extends ListenerAdapter{

    private final List<Member> booster;

    public GuildBoostListener() {
        this.booster = new ArrayList<>();
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        booster.addAll(event.getGuild().getBoosters());
    }

    @Override
    public void onGuildUpdateBoostCount(GuildUpdateBoostCountEvent event) {
        final List<Member> boosterUpdate = event.getGuild().getBoosters();
        final Member member;

        //User is boosting the Server twice
        if(boosterUpdate.size() == booster.size()){
            event.getGuild().getTextChannelsByName("log", true).forEach(channel -> {
                final EmbedBuilder logBuilder = new EmbedBuilder();
                if(event.getNewBoostCount() < event.getOldBoostCount()){
                    logBuilder.setDescription("Jemand hat seinen zusätzlichen Boost auf dem Server entfernt");
                } else {
                    logBuilder.setDescription("Jemand hat seinen zusätzlichen Boost auf dem Server aktiviert");
                }
                channel.sendMessage(logBuilder.build()).queue();
            });
            return;
        }

        if(event.getNewBoostCount() < event.getOldBoostCount()){
            final List<Member> compareList = new ArrayList<>(booster);
            compareList.removeAll(boosterUpdate);
            //Member removed his boost
            member = compareList.get(0);
        } else {
           //List is sorted by {@link net.dv8tion.jda.api.entities.Member#getTimeBoosted()} ascending
           member = boosterUpdate.get(boosterUpdate.size() - 1);
        }

        event.getGuild().getTextChannelsByName("log", true).forEach(channel -> {
            final EmbedBuilder logBuilder = new EmbedBuilder();
            logBuilder.setFooter("@" + member.getUser().getName() + "#" + member.getUser().getDiscriminator(), member.getUser().getEffectiveAvatarUrl());
            logBuilder.setThumbnail(member.getUser().getEffectiveAvatarUrl());
            if(event.getNewBoostCount() < event.getOldBoostCount()){
                logBuilder.setDescription(member.getAsMention() + " hat seinen Boost entfernt");
            } else {
                logBuilder.setDescription(member.getAsMention() + " boostet den Server jetzt");
            }
            channel.sendMessage(logBuilder.build()).queue();
        });

        booster.clear();
        booster.addAll(boosterUpdate);
    }


}