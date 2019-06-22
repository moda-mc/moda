package com.mineglade.icore.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.mineglade.icore.ICore;

public class NameColorUtil {

	public static String getChatColor(Player player) {

		if (ICore.instance.getConfig().getBoolean("mysql.enabled")) {
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
			return "r";
		}

	}

	public static void setChatColor(Player player, String color) {
		if (ICore.instance.getConfig().getBoolean("mysql.enabled")) {
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
		}
	}

	public static String getNameColor(Player player) {
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
			return "r";
		}

	}

	public static void setNameColor(Player player, String color) {
		if (ICore.instance.getConfig().getBoolean("mysql.enabled")) {
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
		}
	}
}
