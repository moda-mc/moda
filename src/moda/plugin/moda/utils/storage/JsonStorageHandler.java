package moda.plugin.moda.utils.storage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import moda.plugin.moda.modules.Module;

public class JsonStorageHandler extends FileStorageHandler {

	private final JsonObject json;
	private final File file;
	
	public JsonStorageHandler(final Module<? extends ModuleStorageHandler> module) throws IOException {
		super(module);
		
		final File fileFileDir = new File(module.getDataFolder() + File.separator + "data");

		fileFileDir.mkdirs();

		this.file = new File(fileFileDir, "data.json");
		
		if (!this.file.exists()) {
			this.file.createNewFile();
			try (Writer writer = new FileWriter(this.file)){
				writer.write("{}");
				writer.flush();
			}
		}

		try (Reader reader = new FileReader(this.file)){
			try {
				this.json = new JsonParser().parse(reader).getAsJsonObject();
			} catch (final ClassCastException e) {
				throw new IOException(e);
			}
		}
	}
	
	public JsonObject getJson() {
		return this.json;
	}

	@Override
	public void save() throws IOException {
		try (Writer writer = new FileWriter(this.file)){
			writer.write(getJson().toString());
		}
	}

}
