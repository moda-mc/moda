package moda.plugin.moda;

@Deprecated
public class PingCommand {

	/*
	 * @Override public boolean onCommand(CommandSender sender, Command command,
	 * String label, String[] args) {
	 *
	 * Player player = (Player) sender; if
	 * (!sender.hasPermission("icore.command.ping")) {
	 * sender.spigot().sendMessage(new ComponentBuilder("")
	 * .append(Moda.getPrefix())
	 * .append(Colors.toComponent(Moda.messages.getString("errors.no-permission")
	 * .replace("{command}", "/" + label))) .event(new
	 * HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("")
	 * .append(Colors.toComponent(
	 * "&7you need &aicore.command.ping&7 to check your ping") ) .create()))
	 * .create()); return true; }
	 *
	 * if (args.length > 0) { if
	 * (!sender.hasPermission("icore.command.ping.others")) {
	 * sender.spigot().sendMessage(new ComponentBuilder("")
	 * .append(Moda.getPrefix())
	 * .append(Colors.toComponent(Moda.messages.getString("errors.no-permission")
	 * .replace("{command}", "/" + label + " [target]"))) .event(new
	 * HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("")
	 * .append(Colors.toComponent(
	 * "&7you need &aicore.command.ping.others&7 to check other people's ping") )
	 * .create())) .create()); return true; } Player target =
	 * Bukkit.getPlayer(args[0]); if (target == null) {
	 * sender.spigot().sendMessage(new ComponentBuilder("")
	 * .append(Moda.getPrefix()) .append(Colors.toComponent(Moda.messages.getString(
	 * "ping.errors.target-invalid"))) .event(new ClickEvent(Action.SUGGEST_COMMAND,
	 * "/" + label + " ")) .create()); return true; } PlayerData data = new
	 * PlayerData(target); String targetNickName; try { targetNickName =
	 * data.getNickName(); final int ping = ReflectionUtil.getPing(target);
	 * player.spigot().sendMessage(new ComponentBuilder("")
	 * .append(Moda.getPrefix()) .append(Colors.toComponent(Moda.messages.getString(
	 * "ping.responses.others.ping") .replace("{ping}", ping + "")
	 * .replace("{target_nickname}", targetNickName))) .create()); return true; }
	 * catch (PlayerNotLoggedException e) { e.printStackTrace(); }
	 *
	 * }
	 *
	 * final int ping = ReflectionUtil.getPing(player);
	 *
	 * if (label.equalsIgnoreCase("hi")) { player.spigot().sendMessage( new
	 * ComponentBuilder("") .append(Moda.getPrefix())
	 * .append(Colors.toComponent(Moda.messages.getString("ping.responses.self.hi").
	 * replace("{ping}", ping + ""))) .create()); return true; }
	 * player.spigot().sendMessage(new ComponentBuilder("")
	 * .append(Moda.getPrefix())
	 * .append(Colors.toComponent(Moda.messages.getString("ping.responses.self.ping"
	 * ).replace("{ping}", ping + ""))) .create()); return true; }
	 */
}
