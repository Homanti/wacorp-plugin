package com.homanti.wacorp;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DiscordBot {
    private static JDA jda;

    public DiscordBot() {
    }

    public static void initialize(String token) throws LoginException, InterruptedException {
        jda = JDABuilder.createDefault(token).enableIntents(GatewayIntent.GUILD_MESSAGES, new GatewayIntent[]{GatewayIntent.DIRECT_MESSAGES}).build();
        jda.awaitReady();
    }

    public static void sendMessage(String channelId, String message) {
        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel != null) {
            channel.sendMessage(message).queue();
        } else {
            System.err.println("Канал не найден!");
        }

    }
}
