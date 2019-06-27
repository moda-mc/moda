package com.mineglade.icore.chat.colors;

import org.bukkit.entity.Player;

import com.mineglade.icore.ICore;
import com.mineglade.icore.utils.PlayerData;

import xyz.derkades.derkutils.bukkit.Colors;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;

public class NameColorPickerMenu extends IconMenu{

	private Player target;
	private Player player;
	
	public NameColorPickerMenu(Player player, Player target) {
		super(ICore.instance, "Name Colors", 2*9, player);
		this.target = target;
		this.player = player;
		
	}

	@Override
	public boolean onOptionClick(OptionClickEvent event) {
		PlayerData data = new PlayerData(target);
		String nameColor;
		if (event.getPosition() == 0) {
			data.setNameColor("r");	
			nameColor = "&fwhite";
		} else if (event.getPosition() == 1 ) {
			data.setNameColor("b");
			nameColor = "&blight blue";
		} else {
			nameColor = "null";
		}
		data.save();
		player.sendMessage(ICore.getPrefix() + 
				Colors.parseColors(ICore.messages.getString("color.name").replace("{name-color}", nameColor)));
		return true;
	}
	
	
	
}