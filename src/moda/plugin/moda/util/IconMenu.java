package moda.plugin.moda.util;

import org.bukkit.entity.Player;

import moda.plugin.moda.Moda;
import moda.plugin.moda.module.Module;

public abstract class IconMenu extends xyz.derkades.derkutils.bukkit.menu.IconMenu {

	public IconMenu(final Module<?> module, final String name, final int size, final Player player) {
		super(Moda.instance, name, size, player, l -> module.registerListener(l));
	}

}
