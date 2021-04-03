package cx.moda.moda.util;

import org.bukkit.entity.Player;

import cx.moda.moda.module.Module;

public abstract class IconMenu extends xyz.derkades.derkutils.bukkit.menu.IconMenu {

	public IconMenu(final Module<?> module, final String name, final int size, final Player player) {
		super(name, size, player, t -> module.getScheduler().interval(1, 1, t),  l -> module.registerListener(l));
	}

}
