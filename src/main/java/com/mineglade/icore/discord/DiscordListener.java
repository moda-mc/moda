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

	private JDA jda;

	public DiscordListener() {
		this.init();
	}

	private void init() {
		try {
			this.jda = new JDABuilder(ICore.instance.getConfig().getString("discord-bot.token"))
					.addEventListener(this)
					.build();

			this.jda.awaitReady();
//			ICore.instance.getLogger().info("Finished Building JDA");
		} catch (final LoginException | InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onMessageReceived(final MessageReceivedEvent event) {
		final JDA jda = event.getJDA();

		final User author = event.getAuthor();
		final Message message = event.getMessage();
		final MessageChannel channel = event.getChannel();

		final String msg = message.getContentDisplay();

		final boolean bot = author.isBot();

		if (event.isFromType(ChannelType.TEXT)) {
			final Guild guild = event.getGuild();
			final TextChannel textChannel = event.getTextChannel();
			final Member member = event.getMember();

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
            final PrivateChannel privateChannel = event.getPrivateChannel();

            ICore.instance.getLogger().info(String.format("[PRIV] %s: %s", author.getName(), msg));
        }

		if (msg.equals("!ip"))
        {
            channel.sendMessage("You can join the server on `play.mineglade.com`").queue();
        }
	}

	public JDA getInstance() {
		return this.jda;
	}

	public void shutdown() {
		if (this.jda != null)
			this.jda.shutdownNow();
	}

	public void restart() {
		this.shutdown();
		this.init();
	}

}