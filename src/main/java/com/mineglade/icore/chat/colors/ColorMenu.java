package com.mineglade.icore.chat.colors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.mineglade.icore.ICore;
import com.mineglade.icore.utils.PlayerData;
import com.mineglade.icore.utils.PlayerNotLoggedException;

import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;

public class ColorMenu extends IconMenu {
	
	private OfflinePlayer target;

	public ColorMenu(Player player, OfflinePlayer target) {
		super(ICore.instance, "Color Picker", 9, player);
		this.target = target;
		PlayerData data = new PlayerData(target);
		
		Bukkit.getScheduler().runTaskAsynchronously(ICore.instance, () -> {
			
			boolean targetExempt = ICore.permission.playerHas("global", target, "icore.command.color.name.others.exempt");
			
			Bukkit.getScheduler().runTask(ICore.instance, () -> {
				try {
					if (player.equals(target)) {
						if (!player.hasPermission("icore.command.color.name")) {
							items.put(2, new ItemBuilder(Material.BARRIER)
									.coloredName("&cName Color")
									.coloredLore("&c&oYou need &ficore.command.color.name\n&cto set your name color.")
									.create());
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
						} else {
							items.put(6, new ItemBuilder(Material.BOOK_AND_QUILL)
									.coloredName("&6Chat Color")
									.coloredLore("&e&oSet your chat color!")
									.create());
						}
					} else {
						
						if (!player.hasPermission("icore.command.color.name.others") && !targetExempt) {
							items.put(2, new ItemBuilder(Material.BARRIER)
									.coloredName("&cName Color")
									.coloredLore("&c&oYou need &ficore.command.color.name\n&cto set " + ChatColor.GOLD + data.getNickName() + "&c&o's name color.")
									.create());
						} else {
							items.put(2, new ItemBuilder(Material.NAME_TAG)
									.coloredName("&6Name Color")
									.coloredLore("&e&oSet " + ChatColor.GOLD + target.getName() + "&e&o's name color!")
									.create());
						}
						if (!player.hasPermission("icore.command.color.chat.others") && !targetExempt) {
							items.put(6, new ItemBuilder(Material.BARRIER)
									.coloredName("&cChat Color")
									.coloredLore("&c&oYou need &ficore.command.color.chat\n&cto set " + ChatColor.GOLD + data.getNickName() + "&c&o's name color.")
									.create());
						} else {
							items.put(6, new ItemBuilder(Material.BOOK_AND_QUILL)
									.coloredName("&6Chat Color")
									.coloredLore("&e&oSet " + ChatColor.GOLD + target.getName() + "&e&o's chat color!")
									.create());
						}
					}
					super.refreshItems();
				} catch (PlayerNotLoggedException e) {
					items.put(4, new ItemBuilder(Material.BARRIER)
							.coloredName("&4&lERROR")
							.coloredLore("&cYou can't set the color of a player", 
									"&cwho has never played on the server before.")
							.create());
				}
			});
			
		});
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
