package moda.plugin.moda.menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import moda.plugin.moda.Moda;
import moda.plugin.moda.repo.ModuleMeta;
import moda.plugin.moda.repo.ModuleMetaVersion;
import moda.plugin.moda.repo.Repositories;
import moda.plugin.moda.repo.Repository;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;

public class BrowseModulesMenu extends IconMenu {

	public BrowseModulesMenu(final Player player) {
		super(Moda.instance, "Moda - Browse Modules", 5*9, player);
		this.addItems();
	}

	private void addItems() {
		final int i = 0;
		for (final Repository repository : Repositories.getRepositories()) {
			try {
				for (final ModuleMeta module : repository.getModules()) {
					final List<String> lore = new ArrayList<>();
					lore.add("Description: " + module.getDescription());
					lore.add("Author:" + module.getAuthor());
					final ModuleMetaVersion version = module.getLatestVersionThatSupports(Moda.minecraftVersion);
					if (version == null) {
						lore.add("Your minecraft version is not supported by this module");
					} else {
						lore.add(String.format("Version: %s (%s)", version.getVersion(), version.getBuild()));
						lore.add("Minecraft versions: " + version.getSupportedMinecraftVersionsAsCommaSeparatedString());
						lore.add("Download URL: " + version.getDownloadUrl());
					}

					if (module.getSourceUrl() == null) {
						lore.add("No source code");
					} else {
						lore.add("Source code: " + module.getSourceUrl());
					}

					if (module.getWebsiteUrl() == null) {
						lore.add("No website");
					} else {
						lore.add("Website: " + module.getWebsiteUrl());
					}

					this.items.put(i, new ItemBuilder(Material.STONE)
							.name(module.getName())
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
		//final ItemStack clicked = event.getItemStack();
		// TODO Click action
		return false;
	}

}
