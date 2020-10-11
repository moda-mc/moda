package moda.plugin.moda.placeholder;

import java.util.function.Supplier;

public class ModaPlaceholder implements IPlaceholder, IGlobalPlaceholder {
	
	private final String name;
	private final Supplier<String> value;
	private final boolean async;
	
	public ModaPlaceholder(final String name, final Supplier<String> value) {
		this(name, value, false);
	}
	
	public ModaPlaceholder(final String name, final Supplier<String> value, final boolean async) {
		this.name = name;
		this.value = value;
		this.async = async;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getValue() {
		return this.value.get();
	}
	
	public boolean isAsync() {
		return this.async;
	}

}
