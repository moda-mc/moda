package com.mineglade.moda;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import com.mineglade.moda.votes.Votes;

import xyz.derkades.derkutils.FileUtils;

public abstract class Module implements Listener {

	static final Module[] MODULES = {
			new Votes(),
	};

	protected Moda plugin;
	protected ModuleLogger logger;
	protected FileConfiguration config;
	protected LangFile lang;
	protected Scheduler scheduler;

	public Module() {
		this.plugin = Moda.instance;
	}

	public abstract String getName();

	public void onEnable() {}

	public void onDisable() {}

	public abstract IMessage[] getMessages();

	public Command[] getCommands() { return null; }

	public final File getDataFolder() {
		return new File(this.plugin.getDataFolder(), this.getName());
	}

	public final void enable() throws IOException {
		Bukkit.getPluginManager().registerEvents(this, this.plugin);

		this.getDataFolder().mkdirs();

		final File configOutputFile = new File(this.getDataFolder(), "config.yaml");
		FileUtils.copyOutOfJar(this.getClass(), "/modules/" + this.getName() + "config.yaml", configOutputFile);
		this.config = YamlConfiguration.loadConfiguration(configOutputFile);

		final File langFileFile = new File(this.getDataFolder(), "lang.yaml");
		final FileConfiguration langFileFileConfiguration = YamlConfiguration.loadConfiguration(langFileFile);
		this.lang = new LangFile(langFileFileConfiguration, this.getMessages());

		if (this.getCommands() != null && this.getCommands().length > 0) {
			try {
				final Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
				field.setAccessible(true);
				final CommandMap map = (CommandMap) field.get(Bukkit.getServer());

				for (final Command command : this.getCommands()) {
					map.register(Moda.instance.getName(), command);
				}
			} catch (final IllegalAccessException | NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
		}

		this.scheduler = new Scheduler(this);

		this.logger.debug("Enabled");
	}

	public final void disable() {
		HandlerList.unregisterAll(this);
		Scheduler.cancelAllTasks(this);

		this.logger.debug("Disabled");
	}

}
