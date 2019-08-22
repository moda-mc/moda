package moda.plugin.moda.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import moda.plugin.moda.Moda;
import moda.plugin.moda.modules.Module;
import moda.plugin.moda.modules.ModuleManager;
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

		final List<String> loaded = new ArrayList<>();

		for (final Module<?> module : manager.getLoadedModules()) {
			loaded.add(module.getName());
			this.items.put(i, new ItemBuilder(Material.WOOL).damage(5).name(module.getName()).create());
			i++;
		}

		for (final String name : manager.getInstalledModulesNames()) {
			if (loaded.contains(name)) {
				continue;
			}

			//final ModuleMeta meta = Modules.getMetadata(name);
			this.items.put(i, new ItemBuilder(Material.WOOL).damage(14).name(name).create());
			i++;
		}
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		final ItemStack clicked = event.getItemStack();
		final String name = event.getItemStack().getItemMeta().getDisplayName();

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
				this.player.sendMessage("Enabled module " + name);
			} catch (final Exception e) {
				this.player.sendMessage("Error occured while enabling module");
				e.printStackTrace();
			}
		} else {
			this.player.sendMessage("error");
		}
		this.addItems();
		this.refreshItems();
		return false;
	}

}
