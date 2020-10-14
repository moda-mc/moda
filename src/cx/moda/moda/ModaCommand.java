package cx.moda.moda;

import cx.moda.moda.menu.ModaMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModaCommand implements CommandExecutor {

	String prefix = Moda.getPrefix();

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (args.length == 0) {
			if (sender instanceof Player) {
				if (sender.hasPermission("moda.core")) {
					new ModaMenu(((Player) sender));
				} else {
					sender.sendMessage("no");
				}
			} else {
				sender.sendMessage("You cannot open the GUI from the console");
			}
		}
		return true;
	}


}
