package com.mineglade.moda.chat.colors;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import xyz.derkades.derkutils.bukkit.ItemBuilder;

public enum IColor {
	WHITE('f', "White", "&7Set your %s color to &%sWhite&7.", Material.CONCRETE, 0),
	AQUA('b', "Light Blue", "&7Set your %s color to &%sLight Blue&7.", Material.CONCRETE_POWDER, 3),
	BLACK('0', "Black", "&7Set your %s color to &%sBlack&7.", Material.CONCRETE, 15),
	BLUE('9', "Violet", "&7Set your %s color to &%sViolet&7.", Material.CONCRETE_POWDER, 11),
	DARK_AQUA('3', "Dark Aqua", "&7Set your %s color to &%sDark Aqua&7.", Material.CONCRETE, 3),
	DARK_BLUE('1', "Dark Blue", "&7Set your %s color to &%sDark Blue&7.", Material.CONCRETE, 11),
	DARK_GREEN('2', "Dark Green", "&7Set your %s color to &%sDark Green&7.", Material.CONCRETE, 13),
	DARK_GRAY('8', "Dark Gray", "&7Set your %s color to &%sDark Gray&7.", Material.CONCRETE, 7),
	DARK_PURPLE('5', "Purple", "&7Set your %s color to &%sPurple&7.", Material.CONCRETE, 10),
	DARK_RED('4', "Dark Red", "&7Set your %s color to &%sDark Red&7.", Material.CONCRETE, 14),
	GOLD('6', "Orange", "&7Set your %s color to &%sOrange&7.", Material.CONCRETE_POWDER, 1),
	GRAY('7', "Gray", "&7Set your %s color to &%sGray&7.", Material.CONCRETE, 8),
	GREEN('a', "Green", "&7Set your %s color to &%sGreen&7.", Material.CONCRETE_POWDER, 5),
	LIGHT_PURPLE('d', "Pink", "&7Set your %s color to &%sPink&7.", Material.CONCRETE, 2),
	RED('c', "Red", "&7Set your %s color to &%sRed&7.", Material.CONCRETE_POWDER, 14),
	YELLOW('e', "Yellow", "&7Set your %s color to &%sYellow", Material.CONCRETE, 4);
	
	private char colorCode;
	private String name;
	private String description;
	private Material item;
	private int itemData;
	
	IColor(char colorCode, String name, String description, Material item, int itemData) {
		this.colorCode = colorCode;
		this.name = name;
		this.description = description;
		this.item = item;
		this.itemData = itemData;
	}
	
	public char getColorCode() {
		return colorCode;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription(IColorType type) {
		return String.format(description, type.toString().toLowerCase(), this.colorCode);
	}
	
	public ItemStack getItem(IColorType type) {
		return new ItemBuilder(item)
				.coloredName("&" + colorCode + name)
				.coloredLore(getDescription(type))
				.damage(itemData)
				.create();
	}
	
	public String getPermission(IColorType type) {
		return String.format("icore.color.%s.%s", type.toString().toLowerCase(), this.toString().toLowerCase());
	}
	
	public static IColor fromName(String itemName) {
		for (IColor iColor : IColor.values()) {
			if (itemName.contains(iColor.getName())) {
				return iColor;
			}
		}
		return null;
	}
}
