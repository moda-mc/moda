package moda.plugin.moda.menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import moda.plugin.moda.Moda;
import moda.plugin.moda.repo.Repository;
import moda.plugin.moda.repo.ModuleMeta;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;

public class BrowseModulesMenu extends IconMenu {

	public BrowseModulesMenu(final Player player) {
		super(Moda.instance, "Moda", 5*9, player);
		this.addItems();
	}

	private void addItems() {
		final int i = 0;
		for (final Repository repository : Moda.repositories) {
			try {
				for (final ModuleMeta module : repository.getModules()) {
					final List<String> lore = new ArrayList<>();
					lore.add("Name: " + module.getName());
					lore.add("Description: " + module.getDescription());
					lore.add("Author:" + module.getAuthor());
					lore.add("Version: " + module.getVersion());
					lore.add("Minecraft versions: " + String.join(", ", module.getMinecraftVersions().stream().map(Object::toString).collect(Collectors.toList())));

					if (module.getSourceURL() == null) {
						lore.add("No source code");
					} else {
						lore.add("Source code: " + module.getSourceURL());
					}

					if (module.getWebsiteURL() == null) {
						lore.add("No website");
					} else {
						lore.add("Website: " + module.getWebsiteURL());
					}

					this.items.put(i, new ItemBuilder(Material.STONE)
							.name(module.getId() + "")
							.lore(lore)
							.create());
				}
			} catch (final IOException e) {
				this.player.sendMessage("Failed to load repository " + repository.getUrl().toString());
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		final ItemStack clicked = event.getItemStack();

		return false;
	}

}
