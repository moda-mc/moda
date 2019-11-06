package moda.plugin.moda.utils;

public class IdiotException extends RuntimeException {

	public IdiotException() {
		super("There was an error, probably due to a screwup by the end-user.\nhttps://www.merriam-webster.com/dictionary/stupid");
	}
	private static final long serialVersionUID = 4815211984684751970L;

}
