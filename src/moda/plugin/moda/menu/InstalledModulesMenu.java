package moda.plugin.moda.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import moda.plugin.moda.Moda;
import moda.plugin.moda.modules.Module;
import moda.plugin.moda.modules.ModuleManager;
import moda.plugin.moda.repo.ModuleMeta;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;

public class InstalledModulesMenu extends IconMenu {

	public InstalledModulesMenu(final Player player) {
		super(Moda.instance, "Moda - Installed Modules", 5*9, player);
		this.addItems();
	}

	private void addItems() {
		int i = 0;

		final ModuleManager manager = ModuleManager.getInstance();

		final List<String> loaded = manager.getLoadedModules().stream().map(Module::getName).collect(Collectors.toList());

		for (final String name : manager.getInstalledModulesNames()) {
			final ItemBuilder builder = new ItemBuilder(Material.WOOL);

			builder.name(ChatColor.RESET + "" + ChatColor.BOLD + name);

			final List<String> lore = new ArrayList<>();

			if (loaded.contains(name)) {
				builder.damage(5);
				lore.add(ChatColor.GREEN + "This module is enabled.");
			} else {
				builder.damage(14);
				lore.add(ChatColor.RED + "This module is installed, but not enabled.");
			}

			lore.add("");

			final ModuleMeta meta = manager.getMetadata(name);

			if (meta == null) {
				lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "No metadata");
			} else {
				lore.add(ChatColor.GRAY + meta.getDescription());
				lore.add("");
				lore.add(this.formatKV("Downloaded version", meta.getDownloadedVersionString()));
				lore.add(this.formatKV("Latest version", meta.getLatestVersionThatSupports(Moda.minecraftVersion).getVersion()));
				lore.add(this.formatKV("Author", meta.getAuthor()));
			}

			builder.lore(lore);

			this.items.put(i, builder.create());
			i++;
		}
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		final ItemStack clicked = event.getItemStack();
		final String name = ChatColor.stripColor(event.getItemStack().getItemMeta().getDisplayName());

		if (clicked.getDurability() == 14) {
			// Unloaded module
			try {
				this.player.sendMessage("Loading module " + name);
				ModuleManager.getInstance().load(name);
				this.player.sendMessage("Loaded module " + name);
			} catch (final Exception | IllegalAccessError e) {
				this.player.sendMessage("Error occured while loading module");
				e.printStackTrace();
			}
		} else if (clicked.getDurability() == 5) {
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
		this.refreshItems();
		return false;
	}

	private String formatKV(final String k, final String v) {
		return ChatColor.GRAY + k + ": " + ChatColor.WHITE + v;
	}

}
