package com.mineglade.moda.modules.vanish.storage;

import com.mineglade.moda.modules.Module;
import com.mineglade.moda.utils.storage.FileStorageHandler;

public class VanishFileStorageHandler extends FileStorageHandler implements VanishStorageHandler {

	public VanishFileStorageHandler(final Module<?> module) {
		super(module);
	}

}
