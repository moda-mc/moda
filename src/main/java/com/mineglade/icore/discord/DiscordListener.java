package com.mineglade.icore.discord;

import javax.security.auth.login.LoginException;

import com.mineglade.icore.ICore;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordListener extends ListenerAdapter {
	
	public static void bot() {
		try {
			JDA jda = new JDABuilder(ICore.instance.getConfig().getString("discord-bot.token"))
					.addEventListener(new DiscordListener())
					.build();
			
			jda.awaitReady();
			ICore.instance.getLogger().info("Finished Building JDA");
		}
		catch (LoginException e) {
			e.printStackTrace();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void onMessageReceived(MessageReceivedEvent event) {
		JDA jda = event.getJDA();
		long responseNumber = event.getResponseNumber();
		
		User author = event.getAuthor();
		Message message = event.getMessage();
		MessageChannel channel = event.getChannel();
		
		String msg = message.getContentDisplay();
		
		boolean bot = author.isBot();
		
		if (event.isFromType(ChannelType.TEXT)) {
			Guild guild = event.getGuild();
			TextChannel textChannel = event.getTextChannel();
			Member member = event.getMember();
			
			String name;
			if (message.isWebhookMessage()) {
				name = author.getName();
			}
			else {
				name = member.getEffectiveName();
			}
			ICore.instance.getLogger().info(String.format("%s: %s", author.getName(), msg));
			
		}
		else if (event.isFromType(ChannelType.PRIVATE))
		{
            PrivateChannel privateChannel = event.getPrivateChannel();

            ICore.instance.getLogger().info(String.format("[PRIV] %s: %s", author.getName(), msg));
        }
		
		if (msg.equals("!ip"))
        {
            channel.sendMessage("You can join the server on `play.mineglade.com`").queue();
        }
	}
}