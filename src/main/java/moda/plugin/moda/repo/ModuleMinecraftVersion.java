package moda.plugin.moda.repo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum ModuleMinecraftVersion {

	// TODO optimize using actual binary operations

	V_1_7_10 (1,   "1.7.10"),
	V_1_8    (2,   "1.8.x"),
	V_1_9    (4,   "1.9.x"),
	V_1_10   (8,   "1.10.x"),
	V_1_11   (16,  "1.11.x"),
	V_1_12   (32,  "1.12.x"),
	V_1_13   (64,  "1.13.x"),
	V_1_14   (128, "1.14.x"),
	V_1_15   (256, "1.15"),
	V_1_16   (512, "1.16");

	private int b;
	private String string;

	ModuleMinecraftVersion(final int b, final String string){
		this.b = b;
		this.string = string;
	}

	public int getCode() {
		return this.b;
	}

	@Override
	public String toString() {
		return this.string;
	}

	private static Map<ModuleMinecraftVersion, Integer> map;

	public static List<ModuleMinecraftVersion> parse(int code){
		final List<ModuleMinecraftVersion> versions = new ArrayList<>();
		for (final Map.Entry<ModuleMinecraftVersion, Integer> entry : map.entrySet()) {
			if (code % entry.getValue() == 0) {
				code -= entry.getValue();
				versions.add(entry.getKey());
			}
		}
		return versions;
	}

	static {
		final Map<ModuleMinecraftVersion, Integer> map = new HashMap<>();
		for (final ModuleMinecraftVersion version : ModuleMinecraftVersion.values()) {
			map.put(version, version.getCode());
		}

		ModuleMinecraftVersion.map = map.entrySet().stream()
			.sorted((o1, o2) -> o1.getValue() > o2.getValue() ? -1 : o1.getValue() < o2.getValue() ? 1 : 0)
			.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
	}

}
