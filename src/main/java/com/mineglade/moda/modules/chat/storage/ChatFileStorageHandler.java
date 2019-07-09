package com.mineglade.moda.modules.chat.storage;

import com.mineglade.moda.modules.Module;
import com.mineglade.moda.utils.storage.FileStorageHandler;

public class ChatFileStorageHandler extends FileStorageHandler implements ChatStorageHandler {

	public ChatFileStorageHandler(final Module<?> module) {
		super(module);
	}
}
