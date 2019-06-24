package com.mineglade.icore.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.mineglade.icore.ICore;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.Colors;

public class NickNameUtil {

	public static void setNickName(Player player, String nickname) {

		if (ICore.instance.getConfig().getBoolean("mysql.enabled")) {
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
		}
		player.setDisplayName(ChatColor.stripColor(nickname));

	}

	public static String getNickName(Player player) {

		if (ICore.instance.getConfig().getBoolean("mysql.enabled")) {
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
			return player.getDisplayName();
		}
	}

	public static void resetNickName(Player player) {

		if (ICore.instance.getConfig().getBoolean("mysql.enabled")) {
			Bukkit.getScheduler().runTaskAsynchronously(ICore.instance, () -> {
				try {
					PreparedStatement statement = ICore.db.prepareStatement("DELETE FROM playerNickName WHERE uuid=?",
							player.getUniqueId());
					statement.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
		}
		player.setDisplayName(player.getName());
		;
	}
}
