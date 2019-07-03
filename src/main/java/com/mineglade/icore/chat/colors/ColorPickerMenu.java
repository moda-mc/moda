package com.mineglade.icore.chat.colors;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.mineglade.icore.ICore;
import com.mineglade.icore.utils.IdiotException;
import com.mineglade.icore.utils.PlayerData;
import com.mineglade.icore.utils.PlayerNotLoggedException;

import net.md_5.bungee.api.chat.ComponentBuilder;
import xyz.derkades.derkutils.bukkit.Colors;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;

public class ColorPickerMenu extends IconMenu {

	private OfflinePlayer target;
	private Player player;
	private IColorType type;

	public ColorPickerMenu(Player player, OfflinePlayer target, IColorType type) throws IdiotException {
		super(ICore.instance, StringUtils.capitalize(type.toString().toLowerCase()) + " Colors", 3 * 9, player);
		this.target = target;
		this.player = player;
		this.type = type;
		PlayerData data = new PlayerData(target);
		if (player.equals(target)) {
			items.put(0, new ItemBuilder(Material.WATER_BUCKET).name("Reset").coloredLore("&7Reset your name color.")
					.damage(15).create());
			int i = 1;
			for (IColor iColor : IColor.values()) {
				if (player.hasPermission(iColor.getPermission(type))) {
					items.put(i, iColor.getItem(type));
				} else {
					items.put(i,
							new ItemBuilder(Material.BARRIER).coloredName("&c" + iColor.getName())
									.coloredLore("&7You need &a" + iColor.getPermission(type) + "&7 to set this color.")
									.create());
				}
				i++;
			}
		} else {
			try {
				items.put(0, new ItemBuilder(Material.WATER_BUCKET).name("Reset")
						.coloredLore("&7Reset " + data.getNickName() + "'s name color.").damage(15).create());
			} catch (PlayerNotLoggedException e) {
				throw new IdiotException();
			}
			int i = 1;
			for (IColor iColor : IColor.values()) {
				if (player.hasPermission(iColor.getPermission(type))) {
					items.put(i, iColor.getItem(type));
				} else {
					items.put(i,
							new ItemBuilder(Material.BARRIER).coloredName("&c" + iColor.getName())
									.coloredLore("&7You need &a" + iColor.getPermission(type) + "&7 to set this color.")
									.create());
				}
				i++;
			}
		}
		items.put(26, new ItemBuilder(Material.BARRIER)
				.coloredName("&4&lBack")
				.coloredLore("&cClick to go back.")
				.create());
	}

	@Override
	public boolean onOptionClick(OptionClickEvent event) throws IdiotException {
		PlayerData data = new PlayerData(target);
		if (event.getName().contains(Colors.stripColors("Back"))) {
			new ColorMenu(player, target).open();
			return false;
		}
		if (type == IColorType.NAME) {
			if (player.equals(target)) {
				if (event.getName().contains("Reset")) {
					data.resetNameColor();
					player.sendMessage(ICore.getPrefix() + 
							Colors.parseColors(ICore.messages.getString("color.name-color.reset.self")));
					return true;
				} else {
					IColor color = IColor.fromName(event.getName());
					if (!player.hasPermission(color.getPermission(IColorType.NAME))) {
						player.spigot().sendMessage(new ComponentBuilder("")
								.append(ICore.getPrefix())
								.append(Colors.toComponent(ICore.messages.getString("color.errors.no-permission")
										.replace("{color}", "&" + color.getColorCode() + color.getName())))
								.create());
						return true;
					}
					data.setNameColor(color.getColorCode());
					player.sendMessage(ICore.getPrefix() + 
							Colors.parseColors(ICore.messages.getString("color.name-color.set.self")
									.replace("{color}", "&" + color.getColorCode() + color.getName())));
					return true;
				}
			} else {
				if (event.getName().contains("Reset")) {
					data.resetNameColor();
					try {
						player.sendMessage(ICore.getPrefix() + 
								Colors.parseColors(ICore.messages.getString("color.name-color.reset.others")
								.replace("{target}", data.getNickName())));
					} catch (PlayerNotLoggedException e) {
						throw new IdiotException();
					}
					return true;
				} else {
					IColor color = IColor.fromName(event.getName());
					data.setNameColor(color.getColorCode());
					try {
						player.sendMessage(ICore.getPrefix() + 
								Colors.parseColors(ICore.messages.getString("color.name-color.set.others")
										.replace("{color}", "&" + color.getColorCode() + color.getName())
										.replace("{target}", data.getNickName())));
					} catch (PlayerNotLoggedException e) {
						throw new IdiotException();
					}
					return true;
				}
			}
		}
		if (type == IColorType.CHAT) {
			if (player.equals(target)) {
				if (event.getName().contains("Reset")) {
					data.resetChatColor();
					player.sendMessage(ICore.getPrefix()
							+ Colors.parseColors(ICore.messages.getString("color.chat-color.reset.self")));
					return true;
				} else if (event.getName().contains("Back")) {
					new ColorMenu(player, target).open();
					return false;
				} else {
					IColor color = IColor.fromName(event.getName());
					System.out.println(color.getPermission(IColorType.CHAT));
					if (!player.hasPermission(color.getPermission(IColorType.CHAT))) {
						player.spigot().sendMessage(new ComponentBuilder("").append(ICore.getPrefix())
								.append(Colors.toComponent(ICore.messages.getString("color.errors.no-permission")
										.replace("{color}", "&" + color.getColorCode() + color.getName())))
								.create());
						return true;
					}
					data.setChatColor(color.getColorCode());
					player.sendMessage(
							ICore.getPrefix() + Colors.parseColors(ICore.messages.getString("color.chat-color.set.self")
									.replace("{color}", "&" + color.getColorCode() + color.getName())));
					return true;
				}
			} else {
				if (event.getName().contains("Reset")) {
					data.resetNameColor();
					try {
						player.sendMessage(ICore.getPrefix() + Colors.parseColors(ICore.messages
								.getString("color.self.name-color.reset.others").replace("{target}", data.getNickName())));
					} catch (PlayerNotLoggedException e) {
						throw new IdiotException();
					}
					return true;
				} else {
					IColor color = IColor.fromName(event.getName());
					data.setChatColor(color.getColorCode());
					try {
						player.sendMessage(
								ICore.getPrefix() + Colors.parseColors(ICore.messages.getString("color.chat-color.set.others")
										.replace("{color}", "&" + color.getColorCode() + color.getName())
										.replace("{target}", data.getNickName())));
					} catch (PlayerNotLoggedException e) {
						throw new IdiotException();
					}
					return true;
				}
			}
		}
		return false;
	}
}