package moda.plugin.spigot.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import moda.plugin.spigot.Moda;
import moda.plugin.spigot.utils.storage.ModuleStorageHandler;

public class Scheduler {

	public static final Map<String, List<BukkitTask>> RUNNING_TASKS = new HashMap<>();

	private final Module<? extends ModuleStorageHandler> module;

	public Scheduler(final Module<? extends ModuleStorageHandler> module) {
		this.module = module;
	}

	public BukkitTask async(final Runnable runnable) {
		final BukkitTask task = Bukkit.getScheduler().runTaskAsynchronously(Moda.instance, runnable);
		this.registerTask(task);
		return task;
	}

	public BukkitTask run(final Runnable runnable) {
		final BukkitTask task = Bukkit.getScheduler().runTask(Moda.instance, runnable);
		this.registerTask(task);
		return task;
	}

	public BukkitTask delay(final long delay, final Runnable runnable) {
		final BukkitTask task = Bukkit.getScheduler().runTaskLater(Moda.instance, runnable, delay);
		this.registerTask(task);
		return task;
	}

	public BukkitTask delayAsync(final long delay, final Runnable runnable) {
		final BukkitTask task = Bukkit.getScheduler().runTaskLaterAsynchronously(Moda.instance, runnable, delay);
		this.registerTask(task);
		return task;
	}

	public BukkitTask interval(final long delay, final long interval, final Runnable runnable) {
		final BukkitTask task = Bukkit.getScheduler().runTaskTimer(Moda.instance, runnable, delay, interval);
		this.registerTask(task);
		return task;
	}

	public BukkitTask intervalAsync(final long delay, final long interval, final Runnable runnable) {
		final BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(Moda.instance, runnable, delay, interval);
		this.registerTask(task);
		return task;
	}


	private void registerTask(final BukkitTask task) {
		final List<BukkitTask> tasks = RUNNING_TASKS.containsKey(this.module.getName()) ?
				RUNNING_TASKS.get(this.module.getName()) : new ArrayList<>();
		tasks.add(task);
		RUNNING_TASKS.put(this.module.getName(), tasks);
	}

	public static void cancelAllTasks(final Module<? extends ModuleStorageHandler> module) {
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
