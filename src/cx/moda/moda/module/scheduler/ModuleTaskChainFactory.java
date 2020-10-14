package cx.moda.moda.module.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import co.aikar.taskchain.AsyncQueue;
import co.aikar.taskchain.GameInterface;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainAbortAction;
import co.aikar.taskchain.TaskChainAsyncQueue;
import co.aikar.taskchain.TaskChainFactory;
import cx.moda.moda.module.Module;
import cx.moda.moda.module.storage.ModuleStorageHandler;

public class ModuleTaskChainFactory extends TaskChainFactory {
	
	private ModuleTaskChainFactory(final Module<? extends ModuleStorageHandler> module, final AsyncQueue asyncQueue) {
        super(new ModuleGameInterface(module, asyncQueue));
    }

    public static TaskChainFactory create(final Module<? extends ModuleStorageHandler> module) {
        return new ModuleTaskChainFactory(module, new TaskChainAsyncQueue());
    }

    private static class ModuleGameInterface implements GameInterface {
    	
        private final Module<? extends ModuleStorageHandler> module;
        private final AsyncQueue asyncQueue;

        ModuleGameInterface(final Module<? extends ModuleStorageHandler> module, final AsyncQueue asyncQueue) {
            this.module = module;
            this.asyncQueue = asyncQueue;
        }

        @Override
        public AsyncQueue getAsyncQueue() {
            return this.asyncQueue;
        }

        @Override
        public boolean isMainThread() {
            return Bukkit.isPrimaryThread();
        }

        @Override
        public void postToMain(final Runnable runnable) {
            if (this.module.isEnabled()) {
                this.module.getScheduler().run(runnable);
            } else {
                runnable.run();
            }
        }

        @Override
        public void scheduleTask(final int ticks, final Runnable runnable) {
            if (this.module.isEnabled()) {
                this.module.getScheduler().delay(ticks, runnable);
            } else {
                runnable.run();
            }
        }

        @Override
        public void registerShutdownHandler(final TaskChainFactory factory) {}
        
    }

    public static final TaskChainAbortAction<Player, String, ?> MESSAGE = new TaskChainAbortAction<Player, String, Object>() {
    	
        @Override
        public void onAbort(final TaskChain<?> chain, final Player player, final String message) {
            player.sendMessage(message);
        }
        
    };

}
