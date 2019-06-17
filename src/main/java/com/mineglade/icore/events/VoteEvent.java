package com.mineglade.icore.events;

import com.mineglade.icore.ICore;
import com.mineglade.icore.PrefixType;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import xyz.derkades.derkutils.bukkit.UUIDFetcher;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class VoteEvent implements Listener {

    @EventHandler
    public void onPlayerVote(VotifierEvent event) {

        Vote vote = event.getVote();

        ICore.economy.bankDeposit(vote.getUsername(), 25);
        Bukkit.broadcastMessage(
        		ICore.getPrefix(PrefixType.COMMAND) 
        		+ ChatColor.translateAlternateColorCodes('&', vote.getUsername() 
        				+ " has voted on " + vote.getServiceName() + "!")
        		);
        
        Bukkit.getScheduler().runTaskAsynchronously(ICore.instance, () -> {
        	try {
    			PreparedStatement statement = ICore.db.prepareStatement(
    					"INSERT INTO votes (uuid, votes) VALUES (?, 1) ON DUPLICATE KEY UPDATE votes=votes+1"
    					, UUIDFetcher.getUUID(vote.getUsername()));
    			statement.execute();
        	} catch (SQLException e) {
    			e.printStackTrace();
    		}
        });
        
        
    }

}
