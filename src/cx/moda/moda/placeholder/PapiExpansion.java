package cx.moda.moda.placeholder;

import java.util.Optional;

import org.bukkit.entity.Player;

import cx.moda.moda.Moda;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PapiExpansion extends PlaceholderExpansion {

	@Override
	public String onPlaceholderRequest(final Player player, final String identifier) {
		return ModaPlaceholderAPI.parseSinglePlaceholder(Optional.ofNullable(player), identifier);
	}

	@Override
	public String getAuthor() {
		return String.join(", ", Moda.instance.getDescription().getAuthors());
	}

	@Override
	public String getIdentifier() {
		return "moda";
	}

	@Override
	public String getVersion() {
		return Moda.instance.getDescription().getVersion();
	}

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

}
