package com.mineglade.moda.chat.colors;

@Deprecated
public class ColorMenu {
	/*
	 * private OfflinePlayer target;
	 * 
	 * public ColorMenu(Player player, OfflinePlayer target) { super(Moda.instance,
	 * "Color Picker", 9, player); this.target = target; PlayerData data = new
	 * PlayerData(target);
	 * 
	 * Bukkit.getScheduler().runTaskAsynchronously(Moda.instance, () -> {
	 * 
	 * boolean targetExempt = Moda.permission.playerHas("global", target,
	 * "icore.command.color.name.others.exempt");
	 * 
	 * Bukkit.getScheduler().runTask(Moda.instance, () -> { try { if
	 * (player.equals(target)) { if
	 * (!player.hasPermission("icore.command.color.name")) { items.put(2, new
	 * ItemBuilder(Material.BARRIER) .coloredName("&cName Color")
	 * .coloredLore("&c&oYou need &ficore.command.color.name\n&cto set your name color."
	 * ) .create()); } else { items.put(2, new ItemBuilder(Material.NAME_TAG)
	 * .coloredName("&6Name Color") .coloredLore("&e&oSet your name color!")
	 * .create()); } if (!player.hasPermission("icore.command.color.chat")) {
	 * items.put(6, new ItemBuilder(Material.BARRIER) .coloredName("&cChat Color")
	 * .coloredLore("&c&oYou need &ficore.command.color.chat\n&cto set your name color."
	 * ) .create()); } else { items.put(6, new ItemBuilder(Material.BOOK_AND_QUILL)
	 * .coloredName("&6Chat Color") .coloredLore("&e&oSet your chat color!")
	 * .create()); } } else {
	 * 
	 * if (!player.hasPermission("icore.command.color.name.others") &&
	 * !targetExempt) { items.put(2, new ItemBuilder(Material.BARRIER)
	 * .coloredName("&cName Color")
	 * .coloredLore("&c&oYou need &ficore.command.color.name\n&cto set " +
	 * ChatColor.GOLD + data.getNickName() + "&c&o's name color.") .create()); }
	 * else { items.put(2, new ItemBuilder(Material.NAME_TAG)
	 * .coloredName("&6Name Color") .coloredLore("&e&oSet " + ChatColor.GOLD +
	 * data.getNickName() + "&e&o's name color!") .create()); } if
	 * (!player.hasPermission("icore.command.color.chat.others") && !targetExempt) {
	 * items.put(6, new ItemBuilder(Material.BARRIER) .coloredName("&cChat Color")
	 * .coloredLore("&c&oYou need &ficore.command.color.chat\n&cto set " +
	 * ChatColor.GOLD + data.getNickName() + "&c&o's name color.") .create()); }
	 * else { items.put(6, new ItemBuilder(Material.BOOK_AND_QUILL)
	 * .coloredName("&6Chat Color") .coloredLore("&e&oSet " + ChatColor.GOLD +
	 * data.getNickName() + "&e&o's chat color!") .create()); } } } catch
	 * (PlayerNotLoggedException e) { items.put(4, new ItemBuilder(Material.BARRIER)
	 * .coloredName("&4&lERROR")
	 * .coloredLore("&cYou can't set the color of a player",
	 * "&cwho has never played on the server before.") .create()); }
	 * super.refreshItems(); });
	 * 
	 * }); }
	 * 
	 * @Override public boolean onOptionClick(OptionClickEvent event) { if
	 * (event.getPosition() == 2 &&
	 * !event.getItemStack().getType().equals(Material.BARRIER)) { new
	 * ColorPickerMenu(event.getPlayer(), target, IColorType.NAME).open(); } if
	 * (event.getPosition() == 6 &&
	 * !event.getItemStack().getType().equals(Material.BARRIER)) { new
	 * ColorPickerMenu(event.getPlayer(), target, IColorType.CHAT).open();
	 * 
	 * } return false; }
	 */
}
