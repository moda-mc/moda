package cx.moda.moda.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import cx.moda.moda.Moda;
import cx.moda.moda.repo.InvalidMetadataException;
import cx.moda.moda.repo.ModuleMetaRepository;
import cx.moda.moda.repo.ModuleMetaVersion;
import cx.moda.moda.repo.Repositories;
import cx.moda.moda.repo.Repository;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;

public class BrowseModulesMenu extends IconMenu {

	public BrowseModulesMenu(final Player player) {
		super(Moda.instance, "Moda - Browse Modules", 5, player);
		this.addItems();
	}

	private void addItems() {
		final int i = 0;
		for (final Repository repository : Repositories.getRepositories()) {
			try {
				for (final ModuleMetaRepository module : repository.getModules()) {
					final List<String> lore = new ArrayList<>();
					lore.add("Description: " + module.getDescription());
					lore.add("Author:" + module.getAuthor());
					final Optional<ModuleMetaVersion> optVersion = module.getLatestVersionThatSupports(Moda.minecraftVersion);
					if (!optVersion.isPresent()) {
						lore.add("Your minecraft version is not supported by this module");
					} else {
						final ModuleMetaVersion version = optVersion.get();
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

					addItem(i, new ItemBuilder(Material.STONE)
							.name(module.getName())
							.lore(lore)
							.create());
				}
			} catch (final InvalidMetadataException e) {
				this.player.sendMessage("Invalid metadata for repository " + repository.getUrl().toString());
				this.player.sendMessage(e.getMessage());
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
