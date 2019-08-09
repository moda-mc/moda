package com.mineglade.moda.utils;

import com.mineglade.moda.Moda;

import xyz.derkades.derkutils.ThrowingSupplier;

public class BukkitFuture<T> extends xyz.derkades.derkutils.bukkit.BukkitFuture<T> {

	public BukkitFuture(final ThrowingSupplier<T, Exception> action) {
		super(Moda.instance, action);
	}

}
