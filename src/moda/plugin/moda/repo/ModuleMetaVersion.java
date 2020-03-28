package moda.plugin.moda.repo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import moda.plugin.moda.Moda;

public class ModuleMetaVersion {

	private final ModuleMeta moduleMeta;
	private final JsonObject json;

	ModuleMetaVersion(final ModuleMeta moduleMeta, final JsonObject json) {
		this.moduleMeta = moduleMeta;
		this.json = json;
	}

	public ModuleMeta getModuleMeta() {
		return this.moduleMeta;
	}

	public JsonObject getJson() {
		return this.json;
	}

	public int getBuild() {
		return this.json.get("build").getAsInt();
	}

	public String getVersion() {
		return this.json.get("version").getAsString();
	}

	public String getChangelog() {
		return this.json.get("changelog").getAsString();
	}

	public URL getDownloadUrl() throws MalformedURLException {
		return new URL(this.json.get("download").getAsString());
	}

	public int getSupportedMinecraftVersionsAsCode() {
		return this.json.get("minecraft_versions").getAsInt();
	}

	public List<ModuleMinecraftVersion> getSupportedMinecraftVersionsAsList() {
		return ModuleMinecraftVersion.parse(this.getSupportedMinecraftVersionsAsCode());
	}

	public String getSupportedMinecraftVersionsAsCommaSeparatedString() {
		return String.join(", ",
				this.getSupportedMinecraftVersionsAsList().stream().map(Object::toString).collect(Collectors.toList()));
	}

	public void download() throws IOException {
		final String fileName = this.getModuleMeta().getName().toString();
		final File jarFile = new File("modules", fileName + ".jar");
		final File metaFile = new File("modules", fileName + ".json");

		if (jarFile.exists()) jarFile.delete();
		if (metaFile.exists()) metaFile.delete();

		final JsonObject meta = this.moduleMeta.getJson();
		meta.addProperty("downloaded_build", this.getBuild());
		FileUtils.write(metaFile, meta.toString(), Charset.forName("UTF-8"), false);

		final URL url = this.getDownloadUrl();

		FileUtils.copyURLToFile(url, jarFile,
				Moda.instance.getConfig().getInt("timeout"), Moda.instance.getConfig().getInt("timeout"));
	}

	public boolean isDownloaded() throws IOException {
		final String fileName = this.getModuleMeta().getName().toString();
		final File jarFile = new File("modules", fileName + ".jar");
		final File metaFile = new File("modules", fileName + ".json");

		if (!jarFile.exists() || !metaFile.exists()) return false;

		final int downloadedBuild = new JsonParser().parse(new FileReader(metaFile)).getAsJsonObject()
				.get("downloaded_build").getAsInt();

		return downloadedBuild == this.getBuild();
	}

}
