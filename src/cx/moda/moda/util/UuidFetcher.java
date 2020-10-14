package cx.moda.moda.util;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cx.moda.moda.Moda;
import xyz.derkades.derkutils.bukkit.UUIDFetcher;

public class UuidFetcher {

	public static Optional<OfflinePlayer> getOfflinePlayer(final String username) {
		final Optional<UUID> opt = getUuidFromName(username);
		if (opt.isPresent()) {
			return Optional.of(Bukkit.getOfflinePlayer(opt.get()));
		} else {
			return Optional.empty();
		}
	}

	public static Optional<UUID> getUuidFromName(final String username) {
		final Player onlinePlayer = Bukkit.getPlayer(username);
		if (onlinePlayer != null) {
			return Optional.of(onlinePlayer.getUniqueId());
		}

		final UUID fromUserCache = getUuidFromUsercacheFile(username);
		if (fromUserCache != null) {
			return Optional.of(fromUserCache);
		}

		try {
			return Optional.of(UUIDFetcher.getUUID(username));
		} catch (final IllegalArgumentException e) {
			return Optional.empty();
		}
	}

	private static UUID getUuidFromUsercacheFile(final String username) {
		try {
			final File usercacheFile = new File(Moda.instance.getDataFolder().getParentFile().getParentFile(), "usercache.json");
			try (Reader reader = new FileReader(usercacheFile)) {
				final JsonArray array = JsonParser.parseReader(reader).getAsJsonArray();
				final Iterator<JsonElement> iterator = array.iterator();
				while (iterator.hasNext()) {
					final JsonObject playerObject = iterator.next().getAsJsonObject();
					final String jsonUsername = playerObject.get("name").getAsString();
					if (username.equals(jsonUsername)) {
						final UUID uuid = UUID.fromString(playerObject.get("uuid").getAsString());
						return uuid;
					}
				}
				return null;
			}
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
