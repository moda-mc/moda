package cx.moda.moda.util;

import java.util.concurrent.Callable;

import cx.moda.moda.Moda;

@Deprecated
public class BukkitFuture<T> extends xyz.derkades.derkutils.bukkit.BukkitFuture<T> {

	public BukkitFuture(final Callable<T> action) {
		super(Moda.instance, action);
	}

}
