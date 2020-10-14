package cx.moda.moda.placeholder;

import org.bukkit.entity.Player;

import com.google.common.base.Function;

public class ModaPlayerPlaceholder implements IPlaceholder, IPlayerPlaceholder {
	
	private final String name;
	private final Function<Player, String> value;
	private final boolean async;
	
	public ModaPlayerPlaceholder(final String name, final Function<Player, String> value) {
		this(name, value, false);
	}
	
	public ModaPlayerPlaceholder(final String name, final Function<Player, String> value, final boolean async) {
		this.name = name;
		this.value = value;
		this.async = async;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getValue(final Player player) {
		return this.value.apply(player);
	}
	
	public boolean isAsync() {
		return this.async;
	}

}
