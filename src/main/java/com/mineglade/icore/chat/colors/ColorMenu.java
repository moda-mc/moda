package com.mineglade.icore.chat.colors;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.mineglade.icore.ICore;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;

public class ColorMenu extends IconMenu {
	
	private Player target;

	public ColorMenu(Player player, Player target) {
		super(ICore.instance, "Color Picker", 9, player);
		this.target = target;
		
		if (player.equals(target)) {
			if (!player.hasPermission("icore.command.color.name")) {
				items.put(2, new ItemBuilder(Material.BARRIER)
						.coloredName("&cName Color")
						.coloredLore("&c&oYou need &ficore.command.color.name\n&cto set your name color.")
						.create());
				return;
			} else {
				items.put(2, new ItemBuilder(Material.NAME_TAG)
						.coloredName("&6Name Color")
						.coloredLore("&e&oSet your name color!")
						.create());
			}
			if (!player.hasPermission("icore.command.color.chat")) {
				items.put(6, new ItemBuilder(Material.BARRIER)
						.coloredName("&cChat Color")
						.coloredLore("&c&oYou need &ficore.command.color.chat\n&cto set your name color.")
						.create());
				return;
			} else {
				items.put(6, new ItemBuilder(Material.BOOK_AND_QUILL)
						.coloredName("&6Chat Color")
						.coloredLore("&e&oSet your chat color!")
						.create());
			}
		} else {
			if (!player.hasPermission("icore.command.color.name.others")) {
				items.put(2, new ItemBuilder(Material.BARRIER)
						.coloredName("&cName Color")
						.coloredLore("&c&oYou need &ficore.command.color.name\n&cto set " + ChatColor.GOLD + target.getName() + "&c&o's name color.")
						.create());
				return;
			} else {
				items.put(2, new ItemBuilder(Material.NAME_TAG)
						.coloredName("&6Name Color")
						.coloredLore("&e&oSet " + ChatColor.GOLD + target.getName() + "&e&o's name color!")
						.create());
			}
			if (!player.hasPermission("icore.command.color.chat.others")) {
				items.put(6, new ItemBuilder(Material.BARRIER)
						.coloredName("&cChat Color")
						.coloredLore("&c&oYou need &ficore.command.color.chat\n&cto set " + ChatColor.GOLD + target.getName() + "&c&o's name color.")
						.create());
				return;
			} else {
				items.put(6, new ItemBuilder(Material.BOOK_AND_QUILL)
						.coloredName("&6Chat Color")
						.coloredLore("&e&oSet " + ChatColor.GOLD + target.getName() + "&e&o's chat color!")
						.create());
			}
		}
	}

	@Override
	public boolean onOptionClick(OptionClickEvent event) {
		if (event.getPosition() == 2 && !event.getItemStack().getType().equals(Material.BARRIER)) {
			new ColorPickerMenu(event.getPlayer(), target, IColorType.NAME).open();
		}
		if (event.getPosition() == 6 && !event.getItemStack().getType().equals(Material.BARRIER)) {
			new ColorPickerMenu(event.getPlayer(), target, IColorType.CHAT).open();
			
		}
		return false;
	}
	
	
	
}
