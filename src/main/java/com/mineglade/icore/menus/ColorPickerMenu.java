package com.mineglade.icore.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.mineglade.icore.ICore;

import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;

public class ColorPickerMenu extends IconMenu {
	
	private Player target;

	public ColorPickerMenu(Player player, Player target) {
		super(ICore.instance, "Color Picker", 9, player);
		this.target = target;
		if (player.hasPermission("icore.command.color.name.others") 
				|| (player.equals(target) && player.hasPermission("icore.command.color.name"))) {
			items.put(2, new ItemBuilder(Material.NAME_TAG)
					.coloredName("&6Name Color")
					.coloredLore("&e&oSet your name color!")
					.create());
		} else {
			items.put(2, new ItemBuilder(Material.BARRIER)
					.coloredName("&cName Color")
					.coloredLore("&c&oYou need &ficore.command.color.name\n&cto set your name color.")
					.create());
		}
		
		if (player.hasPermission("icore.command.color.chat.others") 
				|| (player.equals(target) && player.hasPermission("icore.command.color.chat"))) {
			items.put(6, new ItemBuilder(Material.BOOK_AND_QUILL)
					.coloredName("&6Chat Color")
					.coloredLore("&e&oSet your chat color!")
					.create());
		} else {
			items.put(6, new ItemBuilder(Material.BOOK_AND_QUILL)
					.coloredName("&cChat Color")
					.coloredLore("&c&oYou need &ficore.command.color.chat\n&cto set your chat color.")
					.create());
		}
	}

	@Override
	public boolean onOptionClick(OptionClickEvent event) {
		if (event.getPosition() == 2 && !event.getItemStack().getType().equals(Material.BARRIER)) {
			new NameColorPickerMenu(event.getPlayer(), target).open();
		}
		if (event.getPosition() == 6 && !event.getItemStack().getType().equals(Material.BARRIER)) {
			
		}
		return false;
	}
	
	
	
}
