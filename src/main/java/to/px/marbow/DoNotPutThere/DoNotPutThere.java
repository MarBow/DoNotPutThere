package to.px.marbow.DoNotPutThere;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class DoNotPutThere extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {

		ConfigLoad();

		getLogger().info("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

		getLogger()
				.info("DoNotPutThereは" + (DoNotPutThere.flgDNPTEnabled ? ChatColor.GREEN + "有効" : ChatColor.RED + "無効")
						+ ChatColor.RESET + "です。");

		getLogger().info("設置禁止一覧");
		ShowInfo(dicPlase);

		// getLogger().info("使用禁止一覧");
		// ShowInfo(dicUse);

		getLogger().info("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

		// getCommand("aeos").setExecutor(new SwitchCommand());
		getServer().getPluginManager().registerEvents(this, this);
	}

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	@Override
	public void onDisable() {

		ConfigSave();

		getLogger().info("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

		getLogger()
				.info("DoNotPutThereは" + (DoNotPutThere.flgDNPTEnabled ? ChatColor.GREEN + "有効" : ChatColor.RED + "無効")
						+ ChatColor.RESET + "でした。");

		getLogger().info("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
	}

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	public void ShowInfo(Map<String, Map<String, Item>> dic) {

		Object[] lstWorld = dic.keySet().toArray();
		for (int i = 0; i < lstWorld.length; i++) {

			getLogger().info((String) lstWorld[i]);

			Map<String, Item> dicSub = dic.get(lstWorld[i]);

			Object[] lstMaterial = dicSub.keySet().toArray();

			for (int j = 0; j < lstMaterial.length; j++) {

				getLogger().info("  " + lstMaterial[j]);

				Item itm = dicSub.get(lstMaterial[j]);

				if (itm.Disqualiry) {
					getLogger().info("    取り上げ有効");
				}

				if (itm.Target != null) {
					getLogger().info("    " + itm.Target.name());
				}
			}
		}
	}

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	public void SendInfo(Map<String, Map<String, Item>> dic, CommandSender sender, Object[] lstWorld) {

		for (int i = 0; i < lstWorld.length; i++) {

			sender.sendMessage((String) lstWorld[i]);

			Map<String, Item> dicSub = dic.get(lstWorld[i]);

			Object[] lstMaterial = dicSub.keySet().toArray();

			for (int j = 0; j < lstMaterial.length; j++) {

				sender.sendMessage("  " + lstMaterial[j]);

				Item itm = dicSub.get(lstMaterial[j]);

				if (itm.Disqualiry) {
					sender.sendMessage("    取り上げ有効");
				}

				if (itm.Target != null) {
					sender.sendMessage("    " + itm.Target.name());
				}
			}
		}
	}

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	boolean SetDic(CommandSender sender, String strWorld, String strMaterial, boolean flgEnable, boolean flgDisquality,
			Material mtrlTarget, Map<String, Map<String, Item>> dic) {

		strWorld = strWorld.toLowerCase();
		strMaterial = strMaterial.toLowerCase();

		boolean flgRef = true;

		World wld = getServer().getWorld(strWorld);
		Material mtrl = Material.getMaterial(strMaterial.toUpperCase());

		if (wld != null) {
			if (mtrl != null) {
				Map<String, Item> dicSub = null;

				if (dic.containsKey(strWorld)) {

					dicSub = dic.get(strWorld);

				} else {

					dicSub = new HashMap<String, Item>();

					dic.put(strWorld, dicSub);
				}

				Item itm = null;

				if (dicSub.containsKey(strMaterial)) {

					itm = dicSub.get(strMaterial);

				} else {

					itm = new Item();

					dicSub.put(strMaterial, itm);
				}

				itm.Enabled = flgEnable;
				itm.Disqualiry = flgDisquality;
				itm.Target = mtrlTarget;

				if (!flgEnable) {
					dicSub.remove(strMaterial);
				}

				if (dicSub.values().size() <= 0) {

					dic.remove(strWorld);
				}
			} else {
				if (sender != null) {
					sender.sendMessage(strMaterial + "は有効なマテリアルではありません。");
				}
				flgRef = false;
			}
		} else {
			if (sender != null) {
				sender.sendMessage(strWorld + "は有効なワールドではありません。");
			}
			flgRef = false;
		}
		return flgRef;
	}

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	public static Item ChkDic(String strWorld, String strMaterial, Map<String, Map<String, Item>> dic) {

		Item itmRef = new Item();

		if (dic.containsKey(strWorld)) {

			Map<String, Item> dicSub = dic.get(strWorld);
			if (dicSub != null) {
				if (dicSub.containsKey(strMaterial)) {

					itmRef = dicSub.get(strMaterial);
				}
			}
		}

		return itmRef;
	}

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	private void ConfigSave() {
		FileConfiguration fc = getConfig();

		fc.set("DNPTEnable", flgDNPTEnabled);

		ConfigSave(fc, "Plase", dicPlase);

		ConfigSave(fc, "Use", dicUse);

		saveConfig();
	}

	// ==========================================================

	public void ConfigSave(FileConfiguration fc, String strRootSection, Map<String, Map<String, Item>> dic) {
		ConfigurationSection cs = fc.createSection(strRootSection);

		Object[] lstWorld = dic.keySet().toArray();
		for (int i = 0; i < lstWorld.length; i++) {

			ConfigurationSection cs2 = cs.createSection((String) lstWorld[i]);

			Map<String, Item> dicSub = dic.get(lstWorld[i]);

			Object[] lstMaterial = dicSub.keySet().toArray();

			for (int j = 0; j < lstMaterial.length; j++) {

				ConfigurationSection cs3 = cs2.createSection((String) lstMaterial[j]);

				Item itm = dicSub.get((String) lstMaterial[j]);

				cs3.set("enabled", true);
				if (itm.Disqualiry) {
					cs3.set("disquality", itm.Disqualiry);
				}
				if (itm.Target != null) {
					cs3.set("target", itm.Target.name());
				}
			}
		}
	}

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	private void ConfigLoad() {

		List<World> lstWorld = getServer().getWorlds();

		Material[] mtrls = Material.values();

		FileConfiguration fc = getConfig();

		flgDNPTEnabled = fc.getBoolean("DNPTEnable");

		ConfigLoad(fc, lstWorld, mtrls, "Plase", dicPlase);

		ConfigLoad(fc, lstWorld, mtrls, "Use", dicUse);
	}

	// ==========================================================

	public void ConfigLoad(FileConfiguration fc, List<World> lstWorld, Material[] mtrls, String strRoot,
			Map<String, Map<String, Item>> dic) {

		dic.clear();

		ConfigurationSection cs = fc.getConfigurationSection(strRoot);
		if (cs != null) {
			for (int i = 0; i < lstWorld.size(); i++) {

				String strWorld = lstWorld.get(i).getName();

				ConfigurationSection cs2 = cs.getConfigurationSection(strWorld);

				if (cs2 != null) {

					for (int j = 0; j < mtrls.length; j++) {

						String strMaterial = mtrls[j].name().toLowerCase();

						ConfigurationSection cs3 = cs2.getConfigurationSection(strMaterial);

						if (cs3 != null) {

							boolean flgEnabled = cs3.getBoolean("enabled");
							boolean flgDisqualiry = cs3.getBoolean("disqualiry");
							String strTarget = cs3.getString("target");
							Material mtrlTarget = null;
							if (strTarget != "") {
								mtrlTarget = Material.getMaterial(strTarget);
							}

							SetDic(null, strWorld, strMaterial, flgEnabled, flgDisqualiry, mtrlTarget, dic);
						}
					}
				}
			}
		}
	}

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	static public boolean flgDNPTEnabled = true;

	public static class Item {
		public boolean Enabled = false;
		public boolean Disqualiry = false;
		public Material Target = null;
	}

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		boolean flgRef = true;

		// if ((sender instanceof Player)) {
		// Player playerCmd = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("dnpt")) {

			if (args.length > 0) {
				Player other = (Player) sender;

				if (other != null) {

					switch (args[0].toLowerCase()) {
					case "on":
						flgDNPTEnabled = true;
						sender.sendMessage("設置禁止は" + ChatColor.GREEN + "有効" + ChatColor.RESET + "になりました。");
						ConfigSave();
						break;

					case "off":
						flgDNPTEnabled = false;
						sender.sendMessage("設置禁止は" + ChatColor.RED + "無効" + ChatColor.RESET + "になりました。");
						ConfigSave();
						break;

					case "set":
						if (args.length == 3) {
							if (SetDic(sender, args[1], args[2], true, false, null, dicPlase)) {
								sender.sendMessage(args[1] + "で" + args[2] + "を設置禁止に" + ChatColor.GREEN + "設定"
										+ ChatColor.RESET + "しました");
								ConfigSave();
							}
						} else {
							sender.sendMessage("パラメーターが一致しません");
							sender.sendMessage("/dnpt set WorldName MaterialName");
							sender.sendMessage("で指定してください");
						}
						break;

					case "del":
						if (args.length == 3) {
							if (SetDic(sender, args[1], args[2], false, false, null, dicPlase)) {
								sender.sendMessage(args[1] + "で" + args[2] + "の設置禁止を" + ChatColor.RED + "解除"
										+ ChatColor.RESET + "しました");
								ConfigSave();
							}
						} else {
							sender.sendMessage("パラメーターが一致しません");
							sender.sendMessage("/dnpt del WorldName MaterialName");
							sender.sendMessage("で指定してください");
						}
						break;

					case "info":
						Object[] lstWorldPlase = dicPlase.keySet().toArray();
						if (lstWorldPlase.length > 0) {
							sender.sendMessage("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
							SendInfo(dicPlase, sender, lstWorldPlase);
							sender.sendMessage("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
						} else {
							sender.sendMessage("設置禁止設定はありません");
						}
						break;

					case "use":
						if (args.length > 1) {
							switch (args[1].toLowerCase()) {
							case "info":
								Object[] lstWorldUse = dicUse.keySet().toArray();
								if (args.length > 2) {
									lstWorldUse = new String[] { args[2] };
								}

								if (lstWorldUse.length > 0) {
									sender.sendMessage("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
									SendInfo(dicUse, sender, lstWorldUse);
									sender.sendMessage("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
								} else {
									sender.sendMessage("使用禁止設定はありません");
								}

								break;

							case "set":
								if (args.length == 6) {
									boolean flgDisqualiry = Boolean.parseBoolean(args[4]);
									Material mtrlTarget = Material.getMaterial(args[4]);

									if (flgDisqualiry) {
										mtrlTarget = Material.getMaterial(args[5]);
									}

									if (mtrlTarget != null) {
										if (SetDic(sender, args[2], args[3], true, flgDisqualiry, mtrlTarget, dicUse)) {
											sender.sendMessage(args[2] + "で" + args[3] + "の" + mtrlTarget.name()
													+ "への使用禁止を" + ChatColor.GREEN + "設定" + ChatColor.RESET + "しました");
											if (flgDisqualiry) {
												sender.sendMessage("使用しようとしたアイテムの取り上げが有効です");
											}
											ConfigSave();
										}
									} else {
										if (flgDisqualiry) {
											sender.sendMessage(args[5] + "は有効なブロックではありません。");
										} else {
											sender.sendMessage(args[4] + "は有効なブロックではありません。");
										}
									}
								} else if (args.length == 5) {
									boolean flgDisqualiry = Boolean.parseBoolean(args[4]);
									Material mtrlTarget = Material.getMaterial(args[4]);
									if (flgDisqualiry) {
										if (SetDic(sender, args[2], args[3], true, flgDisqualiry, null, dicUse)) {
											sender.sendMessage(args[2] + "で" + args[3] + "の使用禁止を" + ChatColor.GREEN
													+ "設定" + ChatColor.RESET + "しました");
											sender.sendMessage("使用しようとしたアイテムの取り上げが有効です");
											ConfigSave();
										}
									} else {
										if (mtrlTarget != null) {
											if (SetDic(sender, args[2], args[3], true, false, mtrlTarget, dicUse)) {
												sender.sendMessage(args[2] + "で" + args[3] + "の" + args[4] + "への使用禁止を"
														+ ChatColor.GREEN + "設定" + ChatColor.RESET + "しました");
												ConfigSave();
											}
										} else {
											sender.sendMessage(args[4] + "は有効なブロックではありません。");
										}
									}
								} else if (args.length == 4) {
									if (SetDic(sender, args[2], args[3], true, false, null, dicUse)) {
										sender.sendMessage(args[2] + "で" + args[3] + "の使用禁止を" + ChatColor.GREEN + "設定"
												+ ChatColor.RESET + "しました");
										ConfigSave();
									}
								} else {
									sender.sendMessage("パラメーターが一致しません");
									sender.sendMessage("/dnpt use set WorldName MaterialName 又は");
									sender.sendMessage("/dnpt use set WorldName MaterialName BlockName 若しくは");
									sender.sendMessage("/dnpt use set WorldName MaterialName true BlockName");
									sender.sendMessage("で指定してください");
								}
								break;

							case "del":
								if (args.length == 5) {
									Material mtrlTarget = Material.getMaterial(args[4]);
									if (mtrlTarget != null) {
										if (SetDic(sender, args[2], args[3], false, false, mtrlTarget, dicUse)) {
											sender.sendMessage(args[2] + "で" + args[3] + "の" + args[4] + "への使用禁止を"
													+ ChatColor.RED + "解除" + ChatColor.RESET + "しました");
											ConfigSave();
										}
									} else {
										sender.sendMessage(args[4] + "は有効なブロックではありません。");
									}
								} else if (args.length == 4) {
									if (SetDic(sender, args[2], args[3], false, false, null, dicUse)) {
										sender.sendMessage(args[2] + "で" + args[3] + "の使用禁止を" + ChatColor.RED + "解除"
												+ ChatColor.RESET + "しました");
										ConfigSave();
									}
								} else {
									sender.sendMessage("パラメーターが一致しません");
									sender.sendMessage("/dnpt del WorldName MaterialName　又は");
									sender.sendMessage("/dnpt del WorldName MaterialName BlockName");
									sender.sendMessage("で指定してください");
								}
								break;

							}
						} else {
							sender.sendMessage("/dnpt use info   現在の使用禁止一覧を表示します。");
							sender.sendMessage("/dnpt use info WorldName   指定ワールドでの使用禁止一覧を表示します。");
							sender.sendMessage("/dnpt use set WorldName MaterialName");
							sender.sendMessage("      指定ワールドで指定アイテムの使用禁止を設定します。");
							sender.sendMessage("/dnpt use set WorldName MaterialName true");
							sender.sendMessage("      指定ワールドで指定アイテムの使用禁止を設定し、アイテム取り上げを有効にします。");
							sender.sendMessage("/dnpt use set WorldName MaterialName BlockName");
							sender.sendMessage("      指定ワールドで指定アイテムの指定ブロックへの使用禁止を設定します。");
							sender.sendMessage("/dnpt use set WorldName MaterialName true BlockName");
							sender.sendMessage("      指定ワールドで指定アイテムの指定ブロックへの使用禁止を設定し、アイテム取り上げを有効にします。");
							sender.sendMessage("/dnpt use del WorldName MaterialName");
							sender.sendMessage("      指定ワールドで指定アイテムの使用禁止を解除します。");
							sender.sendMessage("/dnpt use del WorldName MaterialName BlockName");
							sender.sendMessage("      指定ワールドで指定アイテムの指定ブロックへの使用禁止を解除します。");
						}

						break;

					default:
						ShowHelp(sender);
						break;
					}
				}
			} else {
				ShowHelp(sender);
			}
		} else

		{
			flgRef = false;
		}

		return flgRef;
	}

	// ==========================================================

	void ShowHelp(CommandSender sender) {
		sender.sendMessage("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		sender.sendMessage(
				"DoNotPutThereは" + (DoNotPutThere.flgDNPTEnabled ? ChatColor.GREEN + "有効" : ChatColor.RED + "無効")
						+ ChatColor.RESET + "です。");
		sender.sendMessage("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		sender.sendMessage("/dnpt   使用法および現在の状態を表示します。");
		sender.sendMessage("/dnpt [on|off]   プラグインの有効・無効を切り替えます。");
		sender.sendMessage("/dnpt info   現在の設置禁止一覧を表示します。");
		sender.sendMessage("/dnpt set WorldName MaterialName");
		sender.sendMessage("      指定ワールドで指定ブロックの設置禁止を設定します。");
		sender.sendMessage("/dnpt del WorldName MaterialName");
		sender.sendMessage("      指定ワールドで指定ブロックの設置禁止を解除します。");
		sender.sendMessage("/dnpt use info   現在の使用禁止一覧を表示します。");
		sender.sendMessage("/dnpt use info WorldName   指定ワールドでの使用禁止一覧を表示します。");
		sender.sendMessage("/dnpt use set WorldName MaterialName");
		sender.sendMessage("      指定ワールドで指定アイテムの使用禁止を設定します。");
		sender.sendMessage("/dnpt use set WorldName MaterialName true");
		sender.sendMessage("      指定ワールドで指定アイテムの使用禁止を設定し、アイテム取り上げを有効にします。");
		sender.sendMessage("/dnpt use set WorldName MaterialName BlockName");
		sender.sendMessage("      指定ワールドで指定アイテムの指定ブロックへの使用禁止を設定します。");
		sender.sendMessage("/dnpt use set WorldName MaterialName true BlockName");
		sender.sendMessage("      指定ワールドで指定アイテムの指定ブロックへの使用禁止を設定し、アイテム取り上げを有効にします。");
		sender.sendMessage("/dnpt use del WorldName MaterialName");
		sender.sendMessage("      指定ワールドで指定アイテムの使用禁止を解除します。");
		sender.sendMessage("/dnpt use del WorldName MaterialName BlockName");
		sender.sendMessage("      指定ワールドで指定アイテムの指定ブロックへの使用禁止を解除します。");
	}

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	public Player getPlayer(String name) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getName().equals(name)) {
				return player;
			}
		}
		return null;
	}

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	public static Map<String, Map<String, Item>> dicPlase = new HashMap<String, Map<String, Item>>();

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockPlase(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		World world = player.getWorld();
		Block block = event.getBlockPlaced();
		String strHandMaterial = block.getType().name().toLowerCase();

		if (DoNotPutThere.flgDNPTEnabled)// プラグインが有効の場合
		{
			if (DoNotPutThere.ChkDic(world.getName(), strHandMaterial, dicPlase).Enabled) {
				if (!player.hasPermission("donotputthere")) {

					player.sendMessage(strHandMaterial + "の設置は許可されていません");
					getLogger().info(player.getName() + "が" + world.getName() + "で" + strHandMaterial + "を設置しようとしました");
					getLogger().info("場所は" + String.valueOf(player.getLocation().getX()) + ","
							+ String.valueOf(player.getLocation().getY()) + ","
							+ String.valueOf(player.getLocation().getZ()) + "です");
					event.setCancelled(true);
				} else {
					player.sendMessage(strHandMaterial + "の設置は権限により許可されています");
				}
			}
		}
	}

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	public static Map<String, Map<String, Item>> dicUse = new HashMap<String, Map<String, Item>>();

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.hasItem()) {
			if (DoNotPutThere.flgDNPTEnabled)// プラグインが有効の場合
			{
				if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

					Player player = event.getPlayer();
					World world = player.getWorld();

					String strHandMaterial = event.getItem().getType().name().toLowerCase();

					Item itm = DoNotPutThere.ChkDic(world.getName(), strHandMaterial, dicUse);

					if (itm != null) {
						if (itm.Enabled) {
							if (itm.Target == null) {
								if (!player.hasPermission("donotputthere")) {

									player.sendMessage(strHandMaterial + "の使用は許可されていません");

									getLogger().info(player.getName() + "が" + world.getName() + "で" + strHandMaterial
											+ "を使用しようとしました");
									getLogger().info("場所は" + String.valueOf(player.getLocation().getX()) + ","
											+ String.valueOf(player.getLocation().getY()) + ","
											+ String.valueOf(player.getLocation().getZ()) + "です");

									event.setCancelled(true);

									if (itm.Disqualiry) {
										// 使おうとしたエッグを取り上げる
										PlayerInventory inventory = player.getInventory();
										inventory.removeItem(player.getItemInHand());
									}

								} else {
									player.sendMessage(strHandMaterial + "の使用は権限により許可されています");
								}
							} else {
								Block block = event.getClickedBlock();
								String strTargetMaterial = block.getType().name().toLowerCase();

								if (block.getType() == itm.Target) {
									if (!player.hasPermission("donotputthere")) {

										player.sendMessage(
												strHandMaterial + "の" + strTargetMaterial + "への使用は許可されていません");

										getLogger().info(player.getName() + "が" + world.getName() + "で"
												+ strHandMaterial + "を" + strTargetMaterial + "へ使用しようとしました");
										getLogger().info("場所は" + String.valueOf(player.getLocation().getX()) + ","
												+ String.valueOf(player.getLocation().getY()) + ","
												+ String.valueOf(player.getLocation().getZ()) + "です");

										event.setCancelled(true);

										if (itm.Disqualiry) {
											// 使おうとしたエッグを取り上げる
											PlayerInventory inventory = player.getInventory();
											inventory.removeItem(player.getItemInHand());
										}

									} else {
										player.sendMessage(
												strHandMaterial + "の" + strTargetMaterial + "への使用は権限により許可されています");
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
