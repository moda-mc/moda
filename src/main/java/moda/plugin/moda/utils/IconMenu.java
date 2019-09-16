package moda.plugin.moda.utils;

import org.bukkit.entity.Player;

import moda.plugin.moda.Moda;

public abstract class IconMenu extends xyz.derkades.derkutils.bukkit.menu.IconMenu {

	public IconMenu(Module module, String name, int size, Player player) {
		super(Moda.instance, name, size, player); // TODO Somehow modify Derkutils so it is possible to register the event from a module
	}

}
