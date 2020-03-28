package moda.plugin.moda.utils;

/**
 * handles all player data in either mysql or a yaml file.
 *
 * @author MineGlade
 */
@Deprecated
public class PlayerData {
//
//	private final OfflinePlayer player;
//
//	private final JavaPlugin iCore = Moda.instance;
//	private final boolean mysql = Moda.instance.getConfig().getBoolean("mysql.enabled");
//	private FileConfiguration dataFile;
//	private File dataFileFile;
//
//	private final boolean debug = Moda.instance.getConfig().getBoolean("debug");
//
//	/**
//	 * establishes player, writes and establishes dataFilefile.
//	 *
//	 * @param player
//	 */
//	public PlayerData(final OfflinePlayer player) {
//		this.player = player;
//		if (!this.mysql) {
//			final File dataFileFileFolder = new File(Moda.instance.getDataFolder(), "playerdata");
//			dataFileFileFolder.mkdirs();
//			this.dataFileFile = new File(dataFileFileFolder, player.getUniqueId() + ".yaml");
//			this.dataFile = YamlConfiguration.loadConfiguration(this.dataFileFile);
//		}
//	}
//
//	/**
//	 * Instance of PlayerData file.
//	 *
//	 * @param file
//	 */
//	public PlayerData(final File file) {
//		this.player = null;
//		this.dataFileFile = file;
//		this.dataFile = YamlConfiguration.loadConfiguration(this.dataFileFile);
//	}
//
//	/**
//	 * saves playerdata file.
//	 */
//	public void save() {
//		try {
//			this.dataFile.save(this.dataFileFile);
//		} catch (final IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public String getFileName() {
//		return this.dataFileFile.getName();
//	}
//
//
//
//
//	public static List<PlayerData> getAllDataFiles() {
//		final List<PlayerData> dataFileList = new ArrayList<>();
//		final File dataFileFileFolder = new File(Moda.instance.getDataFolder(), "playerdata");
//		dataFileFileFolder.mkdirs();
//		for (final File file : dataFileFileFolder.listFiles()) {
//			final PlayerData data = new PlayerData(file);
//			dataFileList.add(data);
//		}
//		return dataFileList;
//	}
//
//
//
//
//
//










//	/**
//	 * gets chat-color character from mysql or playerdata file.
//	 *
//	 * @return ChatColor object
//	 */
//	public ChatColor getChatColor() {
//		ChatColor chatColor;
//		if (mysql) {
//			try {
//				PreparedStatement statement = Moda.db
//						.prepareStatement("SELECT `color` FROM `playerChatColor` WHERE uuid=?", player.getUniqueId());
//				ResultSet result = statement.executeQuery();
//
//				if (result.next()) {
//					chatColor = ChatColor.getByChar(result.getString("color").charAt(0));
//				} else {
//					chatColor = ChatColor.getByChar(Moda.instance.getConfig().getString("chat.default-chat-color").charAt(0));
//				}
//
//			} catch (SQLException e) {
//				e.printStackTrace();
//				chatColor = ChatColor.getByChar(Moda.instance.getConfig().getString("chat.default-chat-color").charAt(0));
//			}
//		} else {
//			chatColor = ChatColor.getByChar(dataFile.getString("color.chat-color", Moda.instance.getConfig()
//							.getString("chat.default-chat-color"))
//					.charAt(0));
//		}
//		if (debug) {
//			try {
//				iCore.getLogger().info(getNickName() + "'s chat-color is " + chatColor.getName());
//			} catch (PlayerNotLoggedException e) {
//				e.printStackTrace();
//			}
//		}
//		return chatColor;
//	}
//
//	/**
//	 * sets a chat-color character in the mysql or playerdata file.
//	 *
//	 * @param color <br>
//	 *              &nbsp;&nbsp;example: <code>c</code>
//	 */
//	public void setChatColor(char color) {
//		if (mysql) {
//			Bukkit.getScheduler().runTaskAsynchronously(Moda.instance, () -> {
//				try {
//					PreparedStatement statement = Moda.db.prepareStatement(
//							"INSERT INTO playerChatColor (uuid, color) VALUES (?, ?) ON DUPLICATE KEY UPDATE color=?",
//							player.getUniqueId(), color, color);
//					statement.execute();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			});
//		} else {
//			dataFile.set("color.chat-color", color);
//			this.save();
//		}
//	}
//
//	/**
//	 * removes chat-color entry from mysql or playerdata file.
//	 */
//	public void resetChatColor() {
//		if (mysql) {
//			Bukkit.getScheduler().runTaskAsynchronously(Moda.instance, () -> {
//				try {
//					PreparedStatement statement = Moda.db.prepareStatement("DELETE FROM playerChatColor WHERE uuid=?",
//							player.getUniqueId());
//					statement.execute();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			});
//		} else {
//			dataFile.set("color.chat-color", null);
//			this.save();
//		}
//	}
//
//	/**
//	 * gets chat-format character from mysql or playerdata file.
//	 *
//	 * @return
//	 */
//	public ChatColor getChatFormat() {
//		if (mysql) {
//			try {
//				PreparedStatement statement = Moda.db
//						.prepareStatement("SELECT `color` FROM `playerChatFormat` WHERE uuid=?", player.getUniqueId());
//				ResultSet result = statement.executeQuery();
//
//				if (result.next()) {
//					return ChatColor.getByChar(result.getString("color").charAt(0));
//				} else {
//					return ChatColor
//							.getByChar(Moda.instance.getConfig().getString("chat.default-chat-format").charAt(0));
//				}
//
//			} catch (SQLException e) {
//				e.printStackTrace();
//				return ChatColor.getByChar(Moda.instance.getConfig().getString("chat.default-chat-format").charAt(0));
//			}
//		} else {
//			return ChatColor.getByChar(dataFile
//					.getString("color.chat-format", Moda.instance.getConfig().getString("chat.default-chat-format"))
//					.charAt(0));
//		}
//	}
//
//	/**
//	 * sets a chat-format character in the mysql or playerdata file.
//	 *
//	 * @param color <br>
//	 *              &nbsp;&nbsp;example: <code>c</code>
//	 */
//	public void setChatFormat(String color) {
//		if (mysql) {
//			Bukkit.getScheduler().runTaskAsynchronously(Moda.instance, () -> {
//				try {
//					PreparedStatement statement = Moda.db.prepareStatement(
//							"INSERT INTO playerChatFormat (uuid, color) VALUES (?, ?) ON DUPLICATE KEY UPDATE color=?",
//							player.getUniqueId(), color, color);
//					statement.execute();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			});
//		} else {
//			dataFile.set("color.chat-format", color);
//			this.save();
//		}
//	}
//
//	/**
//	 * removes the chatFormat entry from mysql or playerdata file.
//	 */
//	public void resetChatFormat() {
//		if (mysql) {
//			Bukkit.getScheduler().runTaskAsynchronously(Moda.instance, () -> {
//				try {
//					PreparedStatement statement = Moda.db.prepareStatement("DELETE FROM playerChatFormat WHERE uuid=?",
//							player.getUniqueId());
//					statement.execute();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			});
//		} else {
//			dataFile.set("color.chat-format", null);
//			this.save();
//		}
//		if (debug) {
//			try {
//				System.out.println(getNickName() + "'s Chat format has been reset.");
//			} catch (PlayerNotLoggedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * gets name-color from mysql or playerdata file.
//	 *
//	 * @return ChatColor object
//	 */
//	public ChatColor getNameColor() {
//		if (Moda.instance.getConfig().getBoolean("mysql.enabled")) {
//			try {
//				PreparedStatement statement = Moda.db
//						.prepareStatement("SELECT `color` FROM `playerNameColor` WHERE uuid=?", player.getUniqueId());
//				ResultSet result = statement.executeQuery();
//
//				if (result.next()) {
//					return ChatColor.getByChar(result.getString("color").charAt(0));
//				} else {
//					return ChatColor
//							.getByChar(Moda.instance.getConfig().getString("chat.default-name-color").charAt(0));
//				}
//
//			} catch (SQLException e) {
//				e.printStackTrace();
//				return ChatColor.getByChar(Moda.instance.getConfig().getString("chat.default-name-color").charAt(0));
//			}
//		} else {
//			return ChatColor.getByChar(dataFile
//					.getString("color.name-color", Moda.instance.getConfig().getString("chat.default-name-color"))
//					.charAt(0));
//		}
//	}
//
//	/**
//	 * sets a name-color character in the mysql or playerdata file.
//	 *
//	 * @param color <br>
//	 *              &nbsp;&nbsp;example: <code>c</code>
//	 */
//	public void setNameColor(char color) {
//		if (mysql) {
//			Bukkit.getScheduler().runTaskAsynchronously(Moda.instance, () -> {
//				try {
//					PreparedStatement statement = Moda.db.prepareStatement(
//							"INSERT INTO playerNameColor (uuid, color) VALUES (?, ?) ON DUPLICATE KEY UPDATE color=?",
//							player.getUniqueId(), color, color);
//					statement.execute();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			});
//		} else {
//			dataFile.set("color.name-color", color);
//			this.save();
//		}
//	}
//
//	/**
//	 * removes the name-color entry from mysql or playerdata file.
//	 */
//	public void resetNameColor() {
//		if (mysql) {
//			Bukkit.getScheduler().runTaskAsynchronously(Moda.instance, () -> {
//				try {
//					PreparedStatement statement = Moda.db.prepareStatement("DELETE FROM playerNameColor WHERE uuid=?",
//							player.getUniqueId());
//					statement.execute();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			});
//		} else {
//			dataFile.set("color.name-color", null);
//			this.save();
//		}
//	}
//
//	/**
//	 * gets chat-format from mysql or playerdata file.
//	 *
//	 * @return ChatColor object
//	 */
//	public ChatColor getNameFormat() {
//		if (Moda.instance.getConfig().getBoolean("mysql.enabled")) {
//			try {
//				PreparedStatement statement = Moda.db
//						.prepareStatement("SELECT `color` FROM `playerNameFormat` WHERE uuid=?", player.getUniqueId());
//				ResultSet result = statement.executeQuery();
//
//				if (result.next()) {
//					return ChatColor.getByChar(result.getString("color").charAt(0));
//				} else {
//					return ChatColor
//							.getByChar(Moda.instance.getConfig().getString("chat.default-name-format").charAt(0));
//				}
//
//			} catch (SQLException e) {
//				e.printStackTrace();
//				return ChatColor.getByChar(Moda.instance.getConfig().getString("chat.default-name-format").charAt(0));
//			}
//		} else {
//			return ChatColor.getByChar(dataFile
//					.getString("color.name-format", Moda.instance.getConfig().getString("chat.default-name-format"))
//					.charAt(0));
//		}
//
//	}
//
//	/**
//	 * sets a name-format character in the mysql or playerdata file.
//	 *
//	 * @param color <br>
//	 *              &nbsp;&nbsp;example: <code>c</code>
//	 */
//	public void setNameFormat(String color) {
//		if (mysql) {
//			Bukkit.getScheduler().runTaskAsynchronously(Moda.instance, () -> {
//				try {
//					PreparedStatement statement = Moda.db.prepareStatement(
//							"INSERT INTO playerNameFormat (uuid, color) VALUES (?, ?) ON DUPLICATE KEY UPDATE color=?",
//							player.getUniqueId(), color, color);
//					statement.execute();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			});
//		} else {
//			dataFile.set("color.name-format", color);
//			this.save();
//		}
//	}
//
//	/**
//	 * removes the name-format entry from mysql or playerdata file.
//	 */
//	public void resetNameFormat() {
//		if (mysql) {
//			Bukkit.getScheduler().runTaskAsynchronously(Moda.instance, () -> {
//				try {
//					PreparedStatement statement = Moda.db.prepareStatement("DELETE FROM playerNameFormat WHERE uuid=?",
//							player.getUniqueId());
//					statement.execute();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			});
//		} else {
//			dataFile.set("color.name-format", null);
//			this.save();
//		}
//	}
//
//	/**
//	 * sets a nickname in the mysql or playerdata file. (maximum of 16 characters,
//	 * ignoring color codes).
//	 *
//	 * @param nickName <br>
//	 *                 &nbsp;&nbsp;example: <code>&aThis&cIs&bMy&eNickname</code>
//	 * @return
//	 */
//	public void setNickName(CommandSender sender, String nickName, Consumer<Boolean> callback) {
//
//		Bukkit.getScheduler().runTaskAsynchronously(Moda.instance, () -> {
//
//			String lastUserName;
//			String storedNickName;
//			try {
//				lastUserName = getLastUserName();
//				storedNickName = getNickName();
//			} catch (PlayerNotLoggedException e) {
//				lastUserName = "";
//				storedNickName = "";
//			}
//			boolean nickNameExists = false;
//			// Checking if the nickname already exists
//
//			// Don't even start checking if the sender has the permission to set an existing
//			// nickname.
//			if (!sender.hasPermission("icore.command.nickname.existing")
//					&& !Colors.stripColors(nickName).equalsIgnoreCase(Colors.stripColors(storedNickName))
//					&& !Colors.stripColors(nickName).equalsIgnoreCase(lastUserName)) {
//				// MySQL check
//				if (mysql) {
//					try {
//						PreparedStatement nameStatement = Moda.db.prepareStatement(
//								"SELECT `nickname`,`username` FROM `playerNickName`,`playerUserName`");
//						ResultSet nameResults = nameStatement.executeQuery();
//						while (nameResults.next()) {
//							if (Colors.stripColors(nameResults.getString("nickname"))
//									.equalsIgnoreCase(Colors.stripColors(nickName))
//									|| nameResults.getString("username")
//											.equalsIgnoreCase(Colors.stripColors(nickName))) {
//								nickNameExists = true;
//								break;
//							}
//						}
//					} catch (SQLException e) {
//						e.printStackTrace();
//						return;
//					}
//				}
//
//				// flatfile check
//				else {
//
//					for (PlayerData file : getAllDataFiles()) {
//						if ((player.getUniqueId() + ".yaml").equals(file.getFileName())) {
//							break;
//						}
//						String existingNickName = ChatColor.stripColor(Colors.parseColors(storedNickName));
//						String existingName = lastUserName;
//						if (existingNickName.equalsIgnoreCase(ChatColor.stripColor(Colors.parseColors(nickName)))
//								|| existingName.equalsIgnoreCase(ChatColor.stripColor(Colors.parseColors(nickName)))) {
//							nickNameExists = true;
//							break;
//						}
//					}
//
//				}
//			}
//
//			// Setting the nickname
//
//			// Only if the nickname doesn't exist.
//			if (!nickNameExists) {
//				if (mysql) {
//					try {
//						PreparedStatement setStatement = Moda.db.prepareStatement(
//								"INSERT INTO playerNickName (uuid, nickname) VALUES (?, ?) ON DUPLICATE KEY UPDATE nickname=?",
//								player.getUniqueId(), nickName, nickName);
//						setStatement.execute();
//					} catch (SQLException e) {
//
//					}
//				} else {
//					dataFile.set("nickname", nickName);
//					save();
//				}
//				if (player.isOnline()) {
//					Player onlinePlayer = (Player) player;
//					onlinePlayer.setDisplayName(ChatColor.stripColor(nickName));
//				}
//				callback.accept(!nickNameExists);
//			}
//
//			// If the nickname does exist:
//			else {
//				callback.accept(!nickNameExists);
//			}
//
//		});
//	}
//
//	/**
//	 * gets a nickname from the mysql or playerdata file. (maximum of 16 characters,
//	 * ignoring color codes).
//	 *
//	 * @return iCore nickname <br>
//	 *         &nbsp;&nbsp;example: <code>&aThis&cIs&bMy&eNickname</code>
//	 * @throws PlayerNotLoggedException
//	 */
//	public String getNickName() throws PlayerNotLoggedException {
//
//		if (mysql) {
//			try {
//				PreparedStatement statement = Moda.db
//						.prepareStatement("SELECT `nickname` FROM `playerNickName` WHERE uuid=?", player.getUniqueId());
//				ResultSet result = statement.executeQuery();
//
//				if (result.next()) {
//					return Colors.parseColors(result.getString("nickname"));
//				} else {
//					if (player.isOnline()) {
//						Player onlinePlayer = (Player) player;
//						return onlinePlayer.getDisplayName();
//					} else {
//						return getLastUserName();
//					}
//				}
//
//			} catch (SQLException e) {
//				e.printStackTrace();
//				if (player.isOnline()) {
//					Player onlinePlayer = (Player) player;
//					return onlinePlayer.getDisplayName();
//				} else {
//					return getLastUserName();
//				}
//			}
//		} else {
//			return dataFile.getString("nickname", getLastUserName());
//
//		}
//	}
//
//	/**
//	 * removes the nickname entry from the mysql or playerdata file.
//	 */
//	public void resetNickName() {
//
//		if (mysql) {
//			Bukkit.getScheduler().runTaskAsynchronously(Moda.instance, () -> {
//				try {
//					PreparedStatement statement = Moda.db.prepareStatement("DELETE FROM playerNickName WHERE uuid=?",
//							player.getUniqueId());
//					statement.execute();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			});
//		} else {
//			dataFile.set("nickname", null);
//			save();
//		}
//		if (player.isOnline()) {
//			Player onlinePlayer = (Player) player;
//			onlinePlayer.setDisplayName(onlinePlayer.getName());
//		}
//	}
//
//	/**
//	 * gets the last username a player connected to the server with from the
//	 * playerdatabase.
//	 *
//	 * @return Minecraft username
//	 * @throws PlayerNotLoggedException
//	 */
//	public String getLastUserName() throws PlayerNotLoggedException {
//		if (player.isOnline()) {
//			return player.getName();
//		}
//		if (mysql) {
//			try {
//				PreparedStatement statement = Moda.db
//						.prepareStatement("SELECT `username` FROM `playerUserName` WHERE uuid=?", player.getUniqueId());
//				ResultSet result = statement.executeQuery();
//
//				if (result.next()) {
//					return result.getString("username");
//				} else {
//					throw new PlayerNotLoggedException(player.getUniqueId() + " has never played on this server before.");
//				}
//
//			} catch (SQLException e) {
//				e.printStackTrace();
//				return player.getName();
//			}
//		} else {
//			if (!player.hasPlayedBefore()) {
//				throw new PlayerNotLoggedException(player.getUniqueId() + "has never played on this server before.");
//			} else {
//				return dataFile.getString("last-login.username");
//			}
//		}
//	}
//
//	/**
//	 * sets the username a player is currently connected to the server with to the
//	 * playerdatabase.
//	 *
//	 * @param username (player.getName())
//	 */
//	public void setLastUsername(String userName) {
//		if (mysql) {
//			Bukkit.getScheduler().runTaskAsynchronously(Moda.instance, () -> {
//				try {
//					PreparedStatement statement = Moda.db.prepareStatement(
//							"INSERT INTO playerUserName (uuid, username) VALUES (?, ?) ON DUPLICATE KEY UPDATE username=?",
//							player.getUniqueId(), userName, userName);
//					statement.execute();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			});
//		} else {
//			dataFile.set("last-login.username", userName);
//			save();
//		}
//	}
//
//	/**
//	 * gets the IP-address the player last connected to the server with from the
//	 * playerdatabase.
//	 *
//	 * @return IP-address
//	 */
//	public String getLastConnectionAddress() {
//		return "temp";
//	}
//
//	/**
//	 * Sets the IP-address the player is currently connected to the server with in
//	 * the playerdatabase.
//	 */
//	public void setLastConnectionAddress() {
//
//	}


}