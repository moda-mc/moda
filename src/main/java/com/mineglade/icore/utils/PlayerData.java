package com.mineglade.icore.utils;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.mineglade.icore.ICore;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.Colors;

/**
 * handles all player data in either mysql or a yaml file. 
 * @author MineGlade
 */
public class PlayerData {

	private Player player;
	
	private boolean mysql = ICore.instance.getConfig().getBoolean("mysql.enabled");
	private FileConfiguration dataFile;
	private File dataFileFile;
	
	/**
	 * establishes player, writes and establishes datafilefile.
	 * @param player
	 */
	public PlayerData(Player player) {
		this.player = player;
		if (!mysql) {
			File dataFileFileFolder = new File(ICore.instance.getDataFolder(), "playerdata");
			dataFileFileFolder.mkdirs();
			dataFileFile = new File(dataFileFileFolder, player.getUniqueId() + ".yaml");
			this.dataFile = YamlConfiguration.loadConfiguration(dataFileFile);
		}
	}
	
	public void save() {
		try {
			dataFile.save(dataFileFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * gets chat-color code from mysql or playerdata file.
	 * @return Bukkit color code character <br>&nbsp;&nbsp;example: <code>c</code>
	 */
	public String getChatColor() {

		if (mysql) {
			try {
				PreparedStatement statement = ICore.db
						.prepareStatement("SELECT `color` FROM `playerChatColor` WHERE uuid=?", player.getUniqueId());
				ResultSet result = statement.executeQuery();

				if (result.next()) {
					return result.getString("color");
				} else {
					return "r";
				}

			} catch (SQLException e) {
				e.printStackTrace();
				return "r";
			}
		} else {
			return dataFile.getString("color.chat-color", "r");
		}
	}

	/**
	 * sets a chat-color code in the mysql or playerdata file.
	 * @param color <br>&nbsp;&nbsp;example: <code>c</code>
	 */
	public void setChatColor(String color) {
		if (mysql) {
			Bukkit.getScheduler().runTaskAsynchronously(ICore.instance, () -> {
				try {
					PreparedStatement statement = ICore.db.prepareStatement(
							"INSERT INTO playerChatColor (uuid, color) VALUES (?, ?) ON DUPLICATE KEY UPDATE color=?",
							player.getUniqueId(), color, color);
					statement.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
		} else {
			dataFile.set("color.chat-color", color);
		}
	}
	
	public void setChatFormat(String color) {
		if (mysql) {
			Bukkit.getScheduler().runTaskAsynchronously(ICore.instance, () -> {
				try {
					PreparedStatement statement = ICore.db.prepareStatement(
							"INSERT INTO playerChatFormat (uuid, color) VALUES (?, ?) ON DUPLICATE KEY UPDATE color=?",
							player.getUniqueId(), color, color);
					statement.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
		} else {
			dataFile.set("color.chat-format", color);
		}
	}
	
	public String getChatFormat() {

		if (mysql) {
			try {
				PreparedStatement statement = ICore.db
						.prepareStatement("SELECT `color` FROM `playerChatFormat` WHERE uuid=?", player.getUniqueId());
				ResultSet result = statement.executeQuery();

				if (result.next()) {
					return result.getString("color");
				} else {
					return "r";
				}

			} catch (SQLException e) {
				e.printStackTrace();
				return "r";
			}
		} else {
			return dataFile.getString("color.chat-format", "r");
		}
	}

	/**
	 * gets name-color code from mysql or playerdata file.
	 * @return Bukkit color code character <br>&nbsp;&nbsp;example: <code>c</code>
	 */
	public String getNameColor() {
		if (ICore.instance.getConfig().getBoolean("mysql.enabled")) {
			try {
				PreparedStatement statement = ICore.db
						.prepareStatement("SELECT `color` FROM `playerNameColor` WHERE uuid=?", player.getUniqueId());
				ResultSet result = statement.executeQuery();

				if (result.next()) {
					return result.getString("color");
				} else {
					return "r";
				}

			} catch (SQLException e) {
				e.printStackTrace();
				return "r";
			}
		} else {
			return dataFile.getString("color.name", "r");
		}

	}
	
	/**
	 * sets a name-color code in the mysql or playerdata file.
	 * @param color <br>&nbsp;&nbsp;example: <code>c</code>
	 */
	public void setNameColor(String color) {
		if (mysql) {
			Bukkit.getScheduler().runTaskAsynchronously(ICore.instance, () -> {
				try {
					PreparedStatement statement = ICore.db.prepareStatement(
							"INSERT INTO playerNameColor (uuid, color) VALUES (?, ?) ON DUPLICATE KEY UPDATE color=?",
							player.getUniqueId(), color, color);
					statement.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
		} else {
			dataFile.set("color.name", color);
		}
	}

	/**
	 * sets a nickname in the mysql or playerdata file. (maximum of 16 characters, ignoring color codes)
	 * @param nickname <br>&nbsp;&nbsp;example: <code>&aThis&cIs&bMy&eNickname</code>
	 */
	public void setNickName(String nickname) {

		if (mysql) {
			Bukkit.getScheduler().runTaskAsynchronously(ICore.instance, () -> {
				try {
					PreparedStatement statement = ICore.db.prepareStatement(
							"INSERT INTO playerNickName (uuid, nickname) VALUES (?, ?) ON DUPLICATE KEY UPDATE nickname=?",
							player.getUniqueId(), nickname, nickname);
					statement.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
		} else {
			dataFile.set("nickname", nickname);
		}
		player.setDisplayName(ChatColor.stripColor(nickname));

	}

	/**
	 * gets a nickname from the mysql or playerdata file. (maximum of 16 characters, ignoring color codes)
	 * @return iCore nickname <br>&nbsp;&nbsp;example: <code>&aThis&cIs&bMy&eNickname</code>
	 */
	public String getNickName() {

		if (mysql) {
			try {
				PreparedStatement statement = ICore.db
						.prepareStatement("SELECT `nickname` FROM `playerNickName` WHERE uuid=?", player.getUniqueId());
				ResultSet result = statement.executeQuery();

				if (result.next()) {
					return Colors.parseColors(result.getString("nickname"));
				} else {
					return player.getDisplayName();
				}

			} catch (SQLException e) {
				e.printStackTrace();
				return player.getDisplayName();
			}
		} else {
			return dataFile.getString("nickname", player.getDisplayName());
		}
	}

	/**
	 * resets a nickname to a player's username in the mysql or playerdata file. (maximum of 16 characters, ignoring color codes)
	 */
	public void resetNickName() {

		if (mysql) {
			Bukkit.getScheduler().runTaskAsynchronously(ICore.instance, () -> {
				try {
					PreparedStatement statement = ICore.db.prepareStatement("DELETE FROM playerNickName WHERE uuid=?",
							player.getUniqueId());
					statement.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
		} else {
			dataFile.set("nickname", null);
		}
		player.setDisplayName(player.getName());
		;
	}
}
