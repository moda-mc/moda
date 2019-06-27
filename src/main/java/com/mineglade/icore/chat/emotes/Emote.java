package com.mineglade.icore.chat.emotes;

public enum Emote {
	BEAR("ʕ•ᴥ•ʔ"),
	FLOWER("(◕‿◕✿)"),
	GIB("༼ つ ◕_◕ ༽つ"),
	LENNY("( ͡° ͜ʖ ͡°)"),
	LOVE("(づ￣ ³￣)づ\""),
	MAO("ฅ^•ﻌ•^ฅ"),
	SHRUG("¯\\_(ツ)_/¯"),
	TABLEFLIP("(╯°□°）╯︵ ┻━┻"),
	UNFLIP("┬─┬ ノ( ゜-゜ノ)");
	String emote;
	
	Emote(String emote) {
		this.emote = emote;
	}
}