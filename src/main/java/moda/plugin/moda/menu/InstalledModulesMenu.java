package moda.plugin.moda.menu;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import moda.plugin.moda.Moda;
import moda.plugin.moda.modules.Module;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;

public class InstalledModulesMenu extends IconMenu {

	public InstalledModulesMenu(final Player player) {
		super(Moda.instance, "Moda", 5*9, player);
		this.addItems();
	}

	private void addItems() {
		int i = 0;

		final List<Module<?>> disabledModules = Module.LOADED;

		for (final Module<?> module : Module.ENABLED) {
			this.items.put(i, new ItemBuilder(Material.GRASS).name(module.getName()).create());
			i++;
		}

		for (final Module<?> module : disabledModules) {
			this.items.put(i, new ItemBuilder(Material.STONE).name(module.getName()).create());
			i++;
		}
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		final ItemStack clicked = event.getItemStack();

		if (clicked.getType().equals(Material.GRASS)) {
		 	try {
				final Module<?> module = Module.getLoadedModuleByName(clicked.getItemMeta().getDisplayName());
				module.disable();
				this.player.sendMessage("Disabled module " + module.getName());
			} catch (final Exception e) {
				this.player.sendMessage("Error occured while disabling module");
				e.printStackTrace();
			}
		} else if (clicked.getType().equals(Material.STONE)) {
			try {
				final Module<?> module = Module.getLoadedModuleByName(clicked.getItemMeta().getDisplayName());
				module.enable();
				this.player.sendMessage("Enabled module " + module.getName());
			} catch (final Exception e) {
				this.player.sendMessage("Error occured while enabling module");
				e.printStackTrace();
			}
		}
		this.addItems();
		this.refreshItems();
		return false;
	}

}
