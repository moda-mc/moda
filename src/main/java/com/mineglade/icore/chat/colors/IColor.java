package com.mineglade.icore.chat.colors;

import org.bukkit.Material;

public enum IColor {
	RESET("r", "&rReset", "&%sReset&7 your %s color.", Material.INK_SACK, 15),
	AQUA("b", "&bLight Blue", "&7Set your %s color to &%sLight Blue&7.", Material.INK_SACK, 12),
	BLACK("0", "&fBlack", "&7Set your %s color to &%sBlack&7.", Material.INK_SACK, 0),
	BLUE("9", "&9Violet", "&7Set your %s color to &%sViolet&7.", Material.INK_SACK, 13),
	DARK_AQUA("3", "&3Dark Aqua", "&7Set your %s color to &%sDark Aqua&7.", Material.INK_SACK, 6),
	DARK_BLUE("1", "&1Dark Blue", "&7Set your %s color to &%sDark Blue&7.", Material.INK_SACK, 4),
	DARK_GREEN("2", "&2Dark Green", "&7Set your %s color to &%sDark Green&7.", Material.INK_SACK, 2),
	DARK_GRAY("8", "&fDark Gray", "&7Set your %s color to &%sDark Gray&7.", Material.INK_SACK, 8),
	DARK_PURPLE("5", "&5Purple", "&7Set your %s color to &%sPink&7.", Material.INK_SACK, 9),
	DARK_RED("4", "&4Dark Red", "&7Set your %s color to &%sDark Red&7.", Material.NETHER_WARTS, 0),
	GOLD("6", "&6Orange", "&7Set your %s color to &%sOrange&7.", Material.INK_SACK, 14),
	GRAY("7", "&7Gray", "&7Set your %s color to &%sGray&7.", Material.INK_SACK, 7),
	GREEN("a", "&aGreen", "&7Set your %s color to &%sGreen&7.", Material.INK_SACK, 10),
	LIGHT_PURPLE("d", "&dPink", "&7Set your %s color to &%sPink&7.", Material.INK_SACK, 9),
	RED("c", "&cRed", "&7Set your %s color to &%sRed&7.", Material.INK_SACK, 1),
	WHITE("f", "&fWhite", "&7Set your %s color to &%sWhite&7.", Material.INK_SACK, 15),
	YELLOW("e", "&eYellow", "&7Set your %s color to &%sYellow", Material.INK_SACK, 11),
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
	
	IColor(String colorCode, String name, String description, Material item, int itemData) {
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
