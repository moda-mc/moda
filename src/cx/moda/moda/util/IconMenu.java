package cx.moda.moda.util;

import cx.moda.moda.module.Module;
import org.bukkit.entity.Player;

import cx.moda.moda.Moda;

public abstract class IconMenu extends xyz.derkades.derkutils.bukkit.menu.IconMenu {

	public IconMenu(final Module<?> module, final String name, final int size, final Player player) {
		super(Moda.instance, name, size, player, l -> module.registerListener(l));
	}

}
