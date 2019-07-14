package moda.plugin.spigot.modules.votes;

import java.util.Map;

import org.bukkit.OfflinePlayer;

import moda.plugin.spigot.utils.storage.ModuleStorageHandler;

public interface VotesStorageHandler extends ModuleStorageHandler {
	
	public void addVote(OfflinePlayer player);
	
	public int getVotes(OfflinePlayer player);
	
	public void resetVote(OfflinePlayer player);
	
	public void resetAllVotes();
	
	public Map<OfflinePlayer, Integer> getTopVotes(int limit);
	
}
