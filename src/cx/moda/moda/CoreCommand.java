package cx.moda.moda;

@Deprecated
public class CoreCommand {
	/*
	 * // TODO refactor everything to Moda in stead of iCore
	 * 
	 * @Override public boolean onCommand(final CommandSender sender, final Command
	 * command, final String label, final String[] args) {
	 * 
	 * if (args.length < 1) { Bukkit.dispatchCommand(sender, label + " help");
	 * return true; }
	 * 
	 * else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h")) {
	 * sender.sendMessage(Moda.getPrefix() + ChatColor.DARK_GRAY + "==== " +
	 * ChatColor.GREEN + "All iCore Commands" + ChatColor.DARK_GRAY + " ====");
	 * CommandUtil.helpListEntry(sender, label, "help",
	 * "returns a list of all iCore Commands", "icore.command.help", "help", "h",
	 * ""); CommandUtil.helpListEntry(sender, label, "support",
	 * "ask the staff team for support.", "icore.command.support", "support", "sp");
	 * CommandUtil.helpListEntry(sender, label, "reload",
	 * "reloads all iCore configs.", "icore.command.reload", "reload ", "rl"); }
	 * 
	 * else if (args[0].equalsIgnoreCase("reload") ||
	 * args[0].equalsIgnoreCase("rl")) {
	 * 
	 * String permission = "icore.command.reload"; if
	 * (!sender.hasPermission(permission)) { sender.spigot() .sendMessage(new
	 * ComponentBuilder("").append(Moda.getPrefix())
	 * .append("You do not have permission to use this command.").color(ChatColor.
	 * RED) .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + label +
	 * " support ")) .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new
	 * ComponentBuilder("you need ").color(ChatColor.GRAY)
	 * .append(permission).color(ChatColor.GREEN)
	 * .append(" to run this command").color(ChatColor.GRAY) .create())) .create());
	 * return true; } Moda.instance.reloadConfig();
	 * sender.sendMessage(Moda.getPrefix() + ChatColor.GREEN +
	 * "all iCore configs have been reloaded.");
	 * Moda.instance.initDataBaseConnection(); sender.sendMessage(Moda.getPrefix() +
	 * ChatColor.GREEN + "all database connections have been reloaded."); if
	 * (Moda.discord != null) {
	 * Bukkit.getScheduler().runTaskAsynchronously(Moda.instance,
	 * Moda.discord::restart); sender.sendMessage(Moda.getPrefix() + ChatColor.GREEN
	 * + "iCore's JDA hook has been reloaded (Discord)."); } }
	 * 
	 * else if (args[0].equalsIgnoreCase("tp")) { Bukkit.dispatchCommand(sender,
	 * "/iteleport " + String.join(" ",
	 * ListUtils.removeFirstStringFromArray(args))); }
	 * 
	 * else if (args[0].equalsIgnoreCase("support") ||
	 * args[0].equalsIgnoreCase("sp") ||
	 * args[0].equalsIgnoreCase("helpivefallenandicantgetup")) { if (args.length <
	 * 2) { sender.spigot() .sendMessage(new ComponentBuilder("")
	 * .append("You need to provide a description for this command") .event(new
	 * ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + label + " support "))
	 * .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("")
	 * .append("Usage: /" + label + " support <description>").create())) .create());
	 * } else { final String description = String.join(" ",
	 * ListUtils.removeFirstStringFromArray(args));
	 * sender.sendMessage(Moda.getPrefix() + ChatColor.GREEN +
	 * "Your support query has been sent to the staff team."); Bukkit.broadcast(
	 * Moda.getPrefix() + "[Support] " + sender.getName() + ": " + description,
	 * "icore.command.support.receive"); } }
	 * 
	 * else { Bukkit.dispatchCommand(sender, label + " help"); return true; }
	 * 
	 * return true; }
	 */
}
