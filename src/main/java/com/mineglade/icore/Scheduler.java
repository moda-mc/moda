package com.mineglade.icore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class Scheduler {

	public static final Map<String, List<BukkitTask>> RUNNING_TASKS = new HashMap<>();

	private final Module module;

	public Scheduler(final Module module) {
		this.module = module;
	}

	public BukkitTask async(final Runnable runnable) {
		final BukkitTask task = Bukkit.getScheduler().runTaskAsynchronously(ICore.instance, runnable);
		this.registerTask(task);
		return task;
	}

	public BukkitTask run(final Runnable runnable) {
		final BukkitTask task = Bukkit.getScheduler().runTask(ICore.instance, runnable);
		this.registerTask(task);
		return task;
	}

	public BukkitTask delay(final long delay, final Runnable runnable) {
		final BukkitTask task = Bukkit.getScheduler().runTaskLater(ICore.instance, runnable, delay);
		this.registerTask(task);
		return task;
	}

	public BukkitTask interval(final long delay, final long interval, final Runnable runnable) {
		final BukkitTask task = Bukkit.getScheduler().runTaskTimer(ICore.instance, runnable, delay, interval);
		this.registerTask(task);
		return task;
	}

	private void registerTask(final BukkitTask task) {
		final List<BukkitTask> tasks = RUNNING_TASKS.containsKey(this.module.getName()) ?
				RUNNING_TASKS.get(this.module.getName()) : new ArrayList<>();
		tasks.add(task);
		RUNNING_TASKS.put(this.module.getName(), tasks);
	}

	public static void cancelAllTasks(final Module module) {
		if (!RUNNING_TASKS.containsKey(module.getName())) {
			return;
		}

		for (final BukkitTask task : RUNNING_TASKS.get(module.getName())) {
			if (!task.isCancelled()) {
				task.cancel();
			}
		}
	}

}
