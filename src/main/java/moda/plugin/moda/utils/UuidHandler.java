package moda.plugin.moda.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import moda.plugin.moda.Moda;
import xyz.derkades.derkutils.bukkit.UUIDFetcher;

public class UuidHandler {

	public static OfflinePlayer getOfflinePlayer(final String username) throws PlayerNotFoundException {
		return Bukkit.getOfflinePlayer(fetchUuid(username));
	}


	public static UUID fetchUuid(final String username) throws PlayerNotFoundException {
		final Player onlinePlayer = Bukkit.getPlayer(username);
		if (onlinePlayer != null) {
			return onlinePlayer.getUniqueId();
		}

		try {
			final UUID fromUserCache = getUuidFromUsercacheFile(username);
			if (fromUserCache != null){
				return fromUserCache;
			}
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			return UUIDFetcher.getUUID(username);
		} catch (final IllegalArgumentException e) {
			throw new PlayerNotFoundException();
		}
	}

	private static UUID getUuidFromUsercacheFile(final String username) throws FileNotFoundException {
		final File usercacheFile = new File(Moda.instance.getDataFolder().getParentFile().getParentFile(), "usercache.json");

		final JsonReader reader = new JsonReader(new FileReader(usercacheFile));
		final JsonParser parser = new JsonParser();
		final JsonArray array = parser.parse(reader).getAsJsonArray();
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

}
