package com.mineglade.icore.chat.colors;

import org.bukkit.Material;

public enum IFormat {
	BOLD("l", "&lBold", "&7Format your %s to &%sBold&7.", Material.COAL, 0),
	ITALIC("o", "&oItalic", "&7Format your %s to &%sItalic&7.", Material.ARROW, 0),
	MAGIC("k", "&fMagic", "&7Format your %s to &%sMagic&7.", Material.EXP_BOTTLE, 0),
	STRIKETHROUGH("m", "&mStrikethrough", "&7Format your %s to &%sStrikethrough&7.", Material.IRON_FENCE, 0),
	UNDERLINE("n", "&nUnderline", "&7Format your %s to &%sUnderline&7.", Material.STRING, 0);
	
	private String colorCode;
	private String name;
	private String description;
	private Material item;
	private int itemData;
	
	IFormat(String colorCode, String name, String description, Material item, int itemData) {
		this.colorCode = colorCode;
		this.name = name;
		this.description = description;
		this.item = item;
		this.itemData = itemData;
	}
	
	public String getPermission(IColorType type) {
		return String.format("icore.color.%s.%s", type.toString().toLowerCase(), this.toString().toLowerCase());
	}
	public String getDescription(IColorType type) {
		return String.format(description, this.colorCode, type.toString().toLowerCase());
	}
}
