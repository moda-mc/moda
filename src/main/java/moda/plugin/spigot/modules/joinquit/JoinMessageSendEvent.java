package moda.plugin.spigot.modules.joinquit;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class JoinMessageSendEvent extends PlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancel;

	public JoinMessageSendEvent(final Player player) {
		super(player);
	}

	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}

	@Override
	public boolean isCancelled() {
		return this.cancel;
	}

	@Override
	public void setCancelled(final boolean cancel) {
		this.cancel = cancel;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
}