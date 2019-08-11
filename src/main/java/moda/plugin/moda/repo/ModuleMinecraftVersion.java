package moda.plugin.moda.repo;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

public enum ModuleMinecraftVersion {

	// These values MUST be in opposite order
	V_1_16   (512, "1.16"),
	V_1_15   (256, "1.15"),
	V_1_14   (128, "1.14.x"),
	V_1_13   (64,  "1.13.x"),
	V_1_12   (32,  "1.12.x"),
	V_1_11   (16,  "1.11.x"),
	V_1_10   (8,   "1.10.x"),
	V_1_9    (4,   "1.9.x"),
	V_1_8    (2,   "1.8.x"),
	V_1_7_10 (1,   "1.7.10");

	private int value;
	private String string;

	ModuleMinecraftVersion(final int value, final String string){
		this.value = value;
		this.string = string;
	}

	public int getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return this.string;
	}

	public static List<ModuleMinecraftVersion> parse(int code){
		final List<ModuleMinecraftVersion> versions = new ArrayList<>();
		for (final ModuleMinecraftVersion version : values()) {
			if (code - version.getValue() > 0) {
				code -= version.getValue();
				versions.add(version);
			}
		}
		return versions;
	}

	public static ModuleMinecraftVersion getServerVersion() {
		System.out.println(Bukkit.getVersion());

		return ModuleMinecraftVersion.V_1_12; // TODO Implementation
	}

}
