package moda.plugin.moda.utils;

import java.util.concurrent.Callable;

import moda.plugin.moda.Moda;

public class BukkitFuture<T> extends xyz.derkades.derkutils.bukkit.BukkitFuture<T> {

	public BukkitFuture(final Callable<T> action) {
		super(Moda.instance, action);
	}

}
