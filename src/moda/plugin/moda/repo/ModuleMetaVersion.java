package moda.plugin.moda.repo;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.Validate;

import com.google.gson.JsonObject;

public class ModuleMetaVersion implements Comparable<ModuleMetaVersion> {
	
	private final ModuleMeta meta;
	private int build;
	private String version;
	private Optional<String> changelog;
	private URL download;
	private int minecraftVersionsFlag;
	
	ModuleMetaVersion(final JsonObject json, final ModuleMeta meta) throws InvalidMetadataException {
		Validate.notNull(json);
		Validate.notNull(meta);
		
		this.meta = meta;
		
		if (json.has("build")) {
			try {
				this.build = json.get("build").getAsInt();
			} catch (final ClassCastException e) {
				throw new InvalidMetadataException("Key 'build' has wrong type");
			}
		} else {
			throw new InvalidMetadataException("Missing key 'build'");
		}
		
		if (json.has("version")) {
			try {
				this.version = json.get("version").getAsString();
			} catch (final ClassCastException e) {
				throw new InvalidMetadataException("Key 'version' has wrong type");
			}
		} else {
			throw new InvalidMetadataException("Missing key 'version'");
		}
		
		if (json.has("changelog")) {
			try {
				this.changelog = Optional.of(json.get("changelog").getAsString());
			} catch (final ClassCastException e) {
				throw new InvalidMetadataException("Key 'changelog' has wrong type");
			}
		} else {
			this.changelog = Optional.empty();
		}
		
		if (json.has("minecraft_versions")) {
			try {
				this.minecraftVersionsFlag = json.get("minecraft_versions").getAsInt();
			} catch (final ClassCastException e) {
				throw new InvalidMetadataException("Key 'minecraft_versions' has wrong type");
			}
		} else {
			throw new InvalidMetadataException("Missing key 'minecraft_versions'");
		}
	}
	
	public ModuleMeta getModuleMeta() {
		return this.meta;
	}

	public int getBuild() {
		return this.build;
	}
	
	public String getVersion() {
		return this.version;
	}
	
	public Optional<String> getChangelog() {
		return this.changelog;
	}
	
	public URL getDownloadUrl() {
		return this.download;
	}
	
	public int getSupportedMinecraftVersionsFlag() {
		return this.minecraftVersionsFlag;
	}
	
	public List<ModuleMinecraftVersion> getSupportedMinecraftVersions() {
		return ModuleMinecraftVersion.parse(this.minecraftVersionsFlag);
	}

	public String getSupportedMinecraftVersionsAsCommaSeparatedString() {
		return String.join(", ",
				this.getSupportedMinecraftVersions().stream().map(Object::toString).collect(Collectors.toList()));
	}

	@Override
	public int compareTo(final ModuleMetaVersion other) {
		return this.getBuild() - other.getBuild();
	}

//	public void download() throws IOException {
//		final String fileName = this.getModuleMeta().getName().toString();
//		final File jarFile = new File("modules", fileName + ".jar");
//		final File metaFile = new File("modules", fileName + ".json");
//
//		if (jarFile.exists()) {
//			jarFile.delete();
//		}
//		if (metaFile.exists()) {
//			metaFile.delete();
//		}
//
//		final JsonObject meta = this.moduleMeta.getJson();
//		meta.addProperty("downloaded_build", this.getBuild());
//		FileUtils.write(metaFile, meta.toString(), Charset.forName("UTF-8"), false);
//
//		final URL url = this.getDownloadUrl();
//
//		FileUtils.copyURLToFile(url, jarFile,
//				Moda.instance.getConfig().getInt("timeout"), Moda.instance.getConfig().getInt("timeout"));
//	}
//
//	public boolean isDownloaded() throws IOException {
//		final String fileName = this.getModuleMeta().getName().toString();
//		final File jarFile = new File("modules", fileName + ".jar");
//		final File metaFile = new File("modules", fileName + ".json");
//
//		if (!jarFile.exists() || !metaFile.exists()) {
//			return false;
//		}
//
//		final int downloadedBuild = new JsonParser().parse(new FileReader(metaFile)).getAsJsonObject()
//				.get("downloaded_build").getAsInt();
//
//		return downloadedBuild == this.getBuild();
//	}

}
