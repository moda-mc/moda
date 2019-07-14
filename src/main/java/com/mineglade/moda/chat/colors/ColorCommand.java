package com.mineglade.moda.chat.colors;

@Deprecated
public class ColorCommand {
	/*
	 * @Override public boolean onCommand(CommandSender sender, Command command,
	 * String label, String[] args) {
	 * 
	 * if (sender instanceof Player) { Player player = (Player) sender; if
	 * (args.length < 1) { new ColorMenu(player, player).open(); } else { try {
	 * OfflinePlayer target = Bukkit.getOfflinePlayer(UUIDFetcher.getUUID(args[0]));
	 * new ColorMenu(player, target).open(); } catch (IllegalArgumentException e) {
	 * sender.spigot().sendMessage(new ComponentBuilder("")
	 * .append(Moda.getPrefix()) .append(Colors.toComponent(Moda.messages.getString(
	 * "color.errors.target-invalid"))) .event(new
	 * ClickEvent(Action.SUGGEST_COMMAND, "/" + label + " ")) .create()); } } } else
	 * { sender.sendMessage("this command is not functional for the console yet.");
	 * } return true; }
	 */
}