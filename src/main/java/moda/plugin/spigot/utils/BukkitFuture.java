package moda.plugin.spigot.utils;

import moda.plugin.spigot.Moda;
import xyz.derkades.derkutils.ThrowingSupplier;

public class BukkitFuture<T> extends xyz.derkades.derkutils.bukkit.BukkitFuture<T> {

	public BukkitFuture(final ThrowingSupplier<T> action) {
		super(Moda.instance, action);
	}

}
