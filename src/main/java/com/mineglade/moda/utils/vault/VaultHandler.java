package com.mineglade.moda.utils.vault;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.mineglade.moda.modules.Module;
import com.mineglade.moda.utils.storage.ModuleStorageHandler;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class VaultHandler {

	private Permission permission;
	private Chat chat;
	private Economy economy;

	public VaultHandler(final Module<? extends ModuleStorageHandler> module) {
		if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
			throw new VaultNotAvailableException("Vault is not installed");
		}

		final RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServicesManager().getRegistration(Permission.class);
		if (permissionProvider != null) {
			this.permission = permissionProvider.getProvider();
		}

		final RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServicesManager().getRegistration(Chat.class);
		if (chatProvider != null) {
			this.chat = chatProvider.getProvider();
		}

		final RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
		if (economyProvider != null) {
			this.economy = economyProvider.getProvider();
		}
	}

	public Permission getPermission() {
		if (this.permission == null) {
			throw new VaultNotAvailableException("No permissions plugin is installed");
		}

		return this.permission;
	}

	public Chat getChat() {
		if (this.chat == null) {
			throw new VaultNotAvailableException("No chat plugin is installed");
		}

		return this.chat;
	}

	public Economy getEconomy() {
		if (this.economy == null) {
			throw new VaultNotAvailableException("No economy plugin is installed");
		}

		return this.economy;
	}

}
