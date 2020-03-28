package moda.plugin.moda.utils;

import moda.plugin.moda.Moda;
import xyz.derkades.derkutils.ThrowingSupplier;

public class BukkitFuture<T> extends xyz.derkades.derkutils.bukkit.BukkitFuture<T> {

	public BukkitFuture(final ThrowingSupplier<T, Exception> action) {
		super(Moda.instance, action);
	}

}
