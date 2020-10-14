package cx.moda.moda.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cx.moda.moda.module.storage.ModuleStorageHandler;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import cx.moda.moda.Moda;

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

	public BukkitTask async(final BukkitRunnable runnable) {
		final BukkitTask task = runnable.runTaskAsynchronously(Moda.instance);
		this.registerTask(task);
		return task;
	}

	public BukkitTask run(final BukkitRunnable runnable) {
		final BukkitTask task = runnable.runTask(Moda.instance);
		this.registerTask(task);
		return task;
	}

	public BukkitTask delay(final long delay, final BukkitRunnable runnable) {
		final BukkitTask task = runnable.runTaskLater(Moda.instance, delay);
		this.registerTask(task);
		return task;
	}

	public BukkitTask delayAsync(final long delay, final BukkitRunnable runnable) {
		final BukkitTask task = runnable.runTaskLaterAsynchronously(Moda.instance, delay);
		this.registerTask(task);
		return task;
	}

	public BukkitTask interval(final long delay, final long interval, final BukkitRunnable runnable) {
		final BukkitTask task = runnable.runTaskTimer(Moda.instance, delay, interval);
		this.registerTask(task);
		return task;
	}

	public BukkitTask intervalAsync(final long delay, final long interval, final BukkitRunnable runnable) {
		final BukkitTask task = runnable.runTaskTimerAsynchronously(Moda.instance, delay, interval);
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
			task.cancel();
		}
	}

}
