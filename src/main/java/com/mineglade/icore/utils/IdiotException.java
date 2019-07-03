package com.mineglade.icore.utils;

public class IdiotException extends RuntimeException {

	public IdiotException() {
		super("There was an error, probably due to the user's own idiocy.\nhttps://www.merriam-webster.com/dictionary/stupid");
	}
	private static final long serialVersionUID = 4815211984684751970L;

}
