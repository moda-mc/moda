package moda.plugin.moda.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import moda.plugin.moda.Moda;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;

public class ModaMenu extends IconMenu {

	public ModaMenu(final Player player) {
		super(Moda.instance, "Moda", 5*9, player);

		addItem(20, new ItemBuilder(Material.STONE).name("Browse modules").create());
		addItem(24, new ItemBuilder(Material.STONE).name("Installed modules").create());
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		final int slot = event.getPosition();
		if (slot == 20) {
			new BrowseModulesMenu(this.player);
		} else if (slot == 24) {
			new InstalledModulesMenu(this.player);
		}
		return false;
	}



}