package moda.plugin.moda.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import moda.plugin.moda.Moda;
import moda.plugin.moda.module.Module;
import moda.plugin.moda.module.ModuleManager;
import moda.plugin.moda.repo.ModuleMetaLocal;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;

public class InstalledModulesMenu extends IconMenu {
	
	private static final Material TYPE_DISABLED = Material.RED_CONCRETE;
	private static final Material TYPE_ENABLED = Material.LIME_CONCRETE;

	public InstalledModulesMenu(final Player player) {
		super(Moda.instance, "Moda - Installed Modules", 5, player);
		this.addItems();
	}

	private void addItems() {
		int i = 0;

		final ModuleManager manager = ModuleManager.getInstance();

		final List<String> loaded = manager.getLoadedModules().stream().map(Module::getName).collect(Collectors.toList());

		for (final String name : manager.getInstalledModulesNames()) {
			final ItemBuilder builder;

			final List<String> lore = new ArrayList<>();

			if (loaded.contains(name)) {
				builder = new ItemBuilder(TYPE_ENABLED);
				lore.add(ChatColor.GREEN + "This module is enabled.");
			} else {
				builder = new ItemBuilder(TYPE_DISABLED);
				lore.add(ChatColor.RED + "This module is installed, but not enabled.");
			}

			builder.name(ChatColor.RESET + "" + ChatColor.BOLD + name);

			lore.add("");

			final Optional<ModuleMetaLocal> metaOpt = manager.getLocalMetadata(name);

			if (!metaOpt.isPresent()) {
				lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "No metadata");
			} else {
				final ModuleMetaLocal meta = metaOpt.get();
				lore.add(ChatColor.GRAY + meta.getDescription());
				lore.add("");
				lore.add(this.formatKV("Downloaded version", meta.getDownloadedVersion().getVersion()));
				lore.add(this.formatKV("Author", meta.getAuthor()));
			}

			builder.lore(lore);

			addItem(i, builder.create());
			i++;
		}
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		final ItemStack clicked = event.getItemStack();
		final String name = ChatColor.stripColor(event.getItemStack().getItemMeta().getDisplayName());

		if (clicked.getType() == TYPE_DISABLED) {
			// Unloaded module
			try {
				this.player.sendMessage("Loading module " + name);
				ModuleManager.getInstance().load(name);
				this.player.sendMessage("Loaded module " + name);
			} catch (final Exception | IllegalAccessError e) {
				this.player.sendMessage("Error occured while loading module");
				e.printStackTrace();
			}
		} else if (clicked.getType() == TYPE_ENABLED) {
			// Loaded module
			try {
				ModuleManager.getInstance().unload(name);
				this.player.sendMessage("Disabled module " + name);
			} catch (final Exception e) {
				this.player.sendMessage("Error occured while disabling module");
				e.printStackTrace();
			}
		} else {
			this.player.sendMessage("error");
		}
		
		this.addItems();
		return false;
	}

	private String formatKV(final String k, final String v) {
		return ChatColor.GRAY + k + ": " + ChatColor.WHITE + v;
	}

}
