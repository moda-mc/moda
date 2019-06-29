package com.mineglade.icore.chat.colors;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.mineglade.icore.ICore;
import com.mineglade.icore.utils.PlayerData;

import net.md_5.bungee.api.chat.ComponentBuilder;
import xyz.derkades.derkutils.bukkit.Colors;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;

public class ColorPickerMenu extends IconMenu{

	private Player target;
	private Player player;
	private IColorType type;
	
	public ColorPickerMenu(Player player, Player target, IColorType type) {
		super(ICore.instance, StringUtils.capitalize(type.toString().toLowerCase()) + " Colors", 3*9, player);
		this.target = target;
		this.player = player;
		this.type = type;
		items.put(0, new ItemBuilder(Material.WATER_BUCKET)
				.name("Reset")
				.coloredLore("&7Reset your name color.")
				.damage(15)
				.create());
		int i = 1;
		for (IColor iColor : IColor.values()) {
			if (player.hasPermission(iColor.getPermission(type))) {
				items.put(i, iColor.getItem(type));
			} else {
				items.put(i, new ItemBuilder(Material.BARRIER)
						.coloredName("&c" + iColor.getName())
						.coloredLore("&7You need &a" + iColor.getPermission(type) + "&7 to set this color.")
						.create());
			}
			i++;
		}
		items.put(26, new ItemBuilder(Material.BARRIER)
				.name("Back")
				.coloredLore("&7Go back to the previous menu.")
				.create());
	}

	@Override
	public boolean onOptionClick(OptionClickEvent event) {
		PlayerData data = new PlayerData(target);
		if (type == IColorType.NAME) {
			if (event.getName().contains("Reset")) {
				data.resetNameColor();
				player.sendMessage(ICore.getPrefix() + Colors.parseColors(ICore.messages.getString("color.name.color.reset")));
				return true;
			} else if (event.getName().contains("Back")) {
				new ColorMenu(player, target).open();
				return false;
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
				player.sendMessage(ICore.getPrefix() + Colors.parseColors(ICore.messages.getString("color.name.color.set")
						.replace("{color}", "&" + color.getColorCode() + color.getName())));
				return true;
			}
		}
		if (type == IColorType.CHAT) {
			if (event.getName().contains("Reset")) {
				data.resetChatColor();
				player.sendMessage(ICore.getPrefix() + Colors.parseColors(ICore.messages.getString("color.chat.color.reset")));
				return true;
			} else if (event.getName().contains("Back")) {
				new ColorMenu(player, target).open();
				return false;
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
				data.setChatColor(color.getColorCode());
				player.sendMessage(ICore.getPrefix() + Colors.parseColors(ICore.messages.getString("color.chat.color.set")
						.replace("{color}", "&" + color.getColorCode() + color.getName())));
				return true;
		
			}
		}
		return false;
	}
}