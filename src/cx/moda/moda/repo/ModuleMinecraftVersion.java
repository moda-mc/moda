package cx.moda.moda.repo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

public enum ModuleMinecraftVersion {

	V_1_7  ("1.7"),
	V_1_8  ("1.8"),
	V_1_9  ("1.9"),
	V_1_10 ("1.10"),
	V_1_11 ("1.11"),
	V_1_12 ("1.12"),
	V_1_13 ("1.13"),
	V_1_14 ("1.14"),
	V_1_15 ("1.15"),
	V_1_16 ("1.16"),
	
	;

	private String string;

	ModuleMinecraftVersion(final String string){
		this.string = string;
	}
	
	public int getFlag() {
		return 1 << this.ordinal() + 1;
	}

	@Override
	public String toString() {
		return this.string;
	}
	
	public boolean isSupported(final int flag) {
		return (this.getFlag() & flag) != 0;
	}

	public static List<ModuleMinecraftVersion> parse(final int flag) {
//		final List<ModuleMinecraftVersion> versions = new ArrayList<>();
//		for (final ModuleMinecraftVersion version : values()) {
//			if (code - version.getValue() > 0) {
//				code -= version.getValue();
//				versions.add(version);
//			}
//		}
//		return versions;
		
		return Arrays.stream(values()).filter(e -> e.isSupported(flag)).collect(Collectors.toList());
	}

	public static ModuleMinecraftVersion getServerVersion() {
		System.out.println(Bukkit.getVersion());

		return ModuleMinecraftVersion.V_1_12; // TODO Implementation
	}

}
