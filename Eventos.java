package me.SkyBauVirtual;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.SkyBauVirtual.Commands.Bau;
import me.SkyBauVirtual.Config.API;

@SuppressWarnings("deprecation")
public class Eventos implements Listener {

	public static HashMap<Player, Baus> playerNomeTrocando = new HashMap<>();

	@EventHandler
	public void quandoEntrar(PlayerJoinEvent e) {
		Main.getInstance().getAccountCommon().carregarPlayerProfile(e.getPlayer().getUniqueId());
	}

	@EventHandler
	public void quandoFecharBau(InventoryCloseEvent e) {
		for (Baus bau : Main.getInstance().getAccountCommon().getPlayerProfile(e.getPlayer().getUniqueId()).getBaus()) {
			if (e.getInventory().getTitle()
					.equalsIgnoreCase("§7Baú " + bau.getId() + " de " + e.getPlayer().getName())) {
				bau.setInventario(API.InventoryToString(e.getInventory()));

			}
		}
	}

	@EventHandler
	public void quandoClicarNoBauNormal(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (e.getInventory().getTitle().equalsIgnoreCase("Baús de " + p.getName())) {
			if (e.getCurrentItem() == null || e.getCurrentItem().getType() == null
					|| e.getCurrentItem().getType() == Material.AIR) {
				return;
			}
			e.setCancelled(true);
			if (e.getCurrentItem().isSimilar(Bau.setItemWithLore(Material.CHEST, 1, "§eComprar baú!",
					"§6Preço: " + Main.config.getString("bau-preco"), " ", "§7Clique para comprar um novo baú!"))) {
				abrirConfirmaçao(p, TipoDeCompra.BAU, 0);
			} else if (e.getCurrentItem().isSimilar(Bau.setItemWithLore(Material.ENDER_CHEST, 1, "§aEnderChest",
					"§e/enderchest", " ", "§7Clique para abrir seu enderchest"))) {
				p.openInventory(p.getEnderChest());
			} else {
				PlayerProfile pf = Main.getInstance().getAccountCommon().getPlayerProfile(p.getUniqueId());
				for (Baus bau : pf.getBaus()) {
					if (e.getCurrentItem().getType() == bau.getItem()
							&& e.getCurrentItem().getItemMeta().getDisplayName() == bau.getNome()) {
						if (e.isRightClick()) {
							bausDeOpcoes(p, bau.getId());
						} else if (e.isLeftClick()) {
							p.openInventory(bau.getInv());
						}
					}
				}
			}
		}
		if (e.getInventory().getTitle().equalsIgnoreCase("§7Confirmação") && e.getInventory().getSize() == 36) {
			if (e.getCurrentItem() == null || e.getCurrentItem().getType() == null
					|| e.getCurrentItem().getType() == Material.AIR) {
				return;
			}
			e.setCancelled(true);
			if (e.getCurrentItem().isSimilar(Bau.makedisplay(Material.WOOL, 1, 5, "§aAceitar (Leia abaixo)",
					"§7Tem certeza que deseja comprar este item?"))) {
				if (e.getInventory().getItem(13).getType() == Material.CHEST) {
					if (Main.getInstance().getAccountCommon().getPlayerProfile(p.getUniqueId()).getnBaus() >= 28) {
						p.sendMessage("§cVocê atingiu o numero maximo de baús que pode comprar!");
						Bau.abrirInventário(p);
					} else {
						if (Main.economy.getBalance(p.getName()) >= Main.config.getDouble("bau-preco")) {
							if (Main.temPermParaBau(p,
									Main.getInstance().getAccountCommon().getPlayerProfile(p.getUniqueId()).getnBaus()
											+ 1)) {
								Main.getInstance().getAccountCommon().getPlayerProfile(p.getUniqueId()).adicionarBau();
								p.sendMessage("§aVocê comprou o Baú #" + Main.getInstance().getAccountCommon()
										.getPlayerProfile(p.getUniqueId()).getnBaus());
								Bau.abrirInventário(p);
								Main.economy.withdrawPlayer(p.getName(), Main.config.getDouble("bau-preco"));
							} else {
								p.sendMessage("§cVocê não tem permissão para comprar este bau!");
							}
						} else {
							p.sendMessage("§cVocê não tem dinheiro suficiente.");
						}
					}
				} else if (e.getInventory().getItem(13).getType() == Material.ENDER_CHEST) {
					if (Main.economy.getBalance(p.getName()) >= Main.config.getDouble("bau-expandir")) {
						int bauzin = Integer.parseInt(e.getInventory().getItem(13).getItemMeta().getDisplayName()
								.replaceAll("§aExpandir baú #", ""));
						Main.getInstance().getAccountCommon().getPlayerProfile(p.getUniqueId()).getBauID(bauzin)
								.setSize(54);
						Main.economy.withdrawPlayer(p.getName(), Main.config.getDouble("bau-expandir"));
						p.sendMessage("§aVocê expandiu o Bau #" + bauzin);
						Bau.abrirInventário(p);
					} else {
						p.sendMessage("§cVocê não tem dinheiro suficiente.");
					}
				}
			} else if (e.getCurrentItem()
					.isSimilar(Bau.makedisplay(Material.WOOL, 1, 14, "§cNegar", "§7Cancelar esta operação."))) {
				if (e.getInventory().getItem(13).getType() == Material.CHEST) {
					p.sendMessage("§cVocê cancelou a compra do Baú");
				}else if(e.getInventory().getItem(13).getType() == Material.ENDER_CHEST) {
					p.sendMessage("§cVocê cancelou a expansão do Baú");
				}
				p.closeInventory();
			}
		}
		if (e.getInventory().getTitle().contains("§7Opções do baú ")) {
			if (e.getCurrentItem() == null || e.getCurrentItem().getType() == null
					|| e.getCurrentItem().getType() == Material.AIR) {
				return;
			}
			e.setCancelled(true);
			if (e.getCurrentItem()
					.isSimilar(Bau.setItemWithLore(Material.ENDER_CHEST, 1, "§eExpandir!",
							"§6Preço: §7" + Main.config.getString("bau-expandir"), " ",
							"§7Clique para comprar a expansão do baú."))) {
				int bauzin = Integer.parseInt(e.getInventory().getTitle().replaceAll("§7Opções do baú ", ""));
				abrirConfirmaçao(p, TipoDeCompra.EXPANDIR, bauzin);
			} else if (e.getCurrentItem().isSimilar(Bau.setItemWithLore(Material.CHEST, 1, "§eAlterar ícone", " ",
					"§7Clique para alterar o ícone desse baú."))) {
				int bauzin = Integer.parseInt(e.getInventory().getTitle().replaceAll("§7Opções do baú ", ""));
				AbrirBauzinDeOpcoes(p, bauzin);
			} else if (e.getCurrentItem().isSimilar(Bau.setItemWithLore(Material.BOOK_AND_QUILL, 1, "§eRenomear",
					"§7Clique para renomear este baú."))) {
				int bauzin = Integer.parseInt(e.getInventory().getTitle().replaceAll("§7Opções do baú ", ""));
				playerNomeTrocando.put(p,
						Main.getInstance().getAccountCommon().getPlayerProfile(p.getUniqueId()).getBauID(bauzin));
				p.closeInventory();
				p.sendMessage("§eDigite o novo nome do Baú #" + bauzin);
				p.sendMessage("§eCaso queira cancelar digite 'cancelar'.");
			}else if (e.getCurrentItem().isSimilar(Bau.setItem(Material.ARROW, 1, "§aVoltar"))) {
				Bau.abrirInventário(p);
			}
		}
		if (e.getInventory().getTitle().contains("§7Escolha um ícone para Baú ")) {
			if (e.getCurrentItem() == null || e.getCurrentItem().getType() == null
					|| e.getCurrentItem().getType() == Material.AIR) {
				return;
			}
			e.setCancelled(true);
			int bauzin = Integer.parseInt(e.getInventory().getTitle().replaceAll("§7Escolha um ícone para Baú ", ""));
			Main.getInstance().getAccountCommon().getPlayerProfile(p.getUniqueId()).getBauID(bauzin)
					.setItem(e.getCurrentItem().getType());
			p.sendMessage("§aÍcone alterado com sucesso");
			Bau.abrirInventário(p);
		}
	}

	@EventHandler
	public void quandoFalar(PlayerChatEvent e) {
		Player p = e.getPlayer();
		if (playerNomeTrocando.containsKey(p)) {
			if (e.getMessage().equalsIgnoreCase("cancelar")) {
				playerNomeTrocando.remove(p);
				p.sendMessage("§cTroca de nome cancelada com sucesso!");
				Bau.abrirInventário(p);
			} else {
				playerNomeTrocando.get(p).setNome(e.getMessage());
				playerNomeTrocando.remove(p);
				Bau.abrirInventário(p);
			}
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void quandoSair(PlayerQuitEvent e) {
		Main.getInstance().getAccountCommon()
				.savePlayerProfile(Main.getInstance().getAccountCommon().getPlayerProfile(e.getPlayer().getUniqueId()));
	}

	public static void AbrirBauzinDeOpcoes(Player p, int bau) {
		Inventory inv = Bukkit.createInventory(null, 54, "§7Escolha um ícone para Baú " + bau);

		inv.addItem(new ItemStack(Material.DIAMOND_HELMET));
		inv.addItem(new ItemStack(Material.DIAMOND_CHESTPLATE));
		inv.addItem(new ItemStack(Material.DIAMOND_LEGGINGS));
		inv.addItem(new ItemStack(Material.DIAMOND_BOOTS));
		inv.addItem(new ItemStack(Material.DIAMOND_SWORD));
		inv.addItem(new ItemStack(Material.DIAMOND_PICKAXE));
		inv.addItem(new ItemStack(Material.BOW));
		inv.addItem(new ItemStack(Material.ACACIA_DOOR));
		inv.addItem(new ItemStack(Material.DIAMOND_SPADE));
		inv.addItem(new ItemStack(Material.MOB_SPAWNER));
		inv.addItem(new ItemStack(Material.IRON_INGOT));
		inv.addItem(new ItemStack(Material.GOLD_INGOT));
		inv.addItem(new ItemStack(Material.DIAMOND));
		inv.addItem(new ItemStack(Material.EMERALD));
		inv.addItem(new ItemStack(Material.ENDER_PEARL));
		inv.addItem(new ItemStack(Material.IRON_BLOCK));
		inv.addItem(new ItemStack(Material.GOLD_BLOCK));
		inv.addItem(new ItemStack(Material.DIAMOND_BLOCK));
		inv.addItem(new ItemStack(Material.EMERALD_BLOCK));
		inv.addItem(new ItemStack(Material.TNT));
		inv.addItem(new ItemStack(Material.OBSIDIAN));
		inv.addItem(new ItemStack(Material.SPONGE));
		inv.addItem(new ItemStack(Material.getMaterial(98)));
		inv.addItem(new ItemStack(Material.CHEST));
		inv.addItem(new ItemStack(Material.ENDER_CHEST));
		inv.addItem(new ItemStack(Material.PAPER));
		inv.addItem(new ItemStack(Material.BOOK));
		inv.addItem(new ItemStack(Material.QUARTZ_BLOCK));
		inv.addItem(new ItemStack(Material.GOLDEN_APPLE));
		inv.addItem(Bau.makeWithNoName(Material.POTION, 1, 8257));
		inv.addItem(new ItemStack(Material.SLIME_BALL));
		inv.addItem(new ItemStack(Material.BEACON));
		inv.addItem(new ItemStack(Material.BEDROCK));
		inv.addItem(new ItemStack(Material.ANVIL));
		inv.addItem(new ItemStack(Material.PACKED_ICE));
		inv.addItem(new ItemStack(Material.ENDER_STONE));
		inv.addItem(new ItemStack(Material.ENCHANTMENT_TABLE));
		inv.addItem(new ItemStack(Material.BOOK_AND_QUILL));
		inv.addItem(new ItemStack(Material.SAND));
		inv.addItem(new ItemStack(Material.FURNACE));
		inv.addItem(new ItemStack(Material.BUCKET));
		inv.addItem(new ItemStack(Material.WATER_BUCKET));
		inv.addItem(new ItemStack(Material.LAVA_BUCKET));
		inv.addItem(new ItemStack(Material.EXP_BOTTLE));
		inv.addItem(new ItemStack(Material.GLOWSTONE));
		inv.addItem(new ItemStack(Material.SOUL_SAND));
		inv.addItem(Bau.makeWithNoName(Material.GOLDEN_APPLE, 1, 1));
		inv.addItem(new ItemStack(Material.FIREWORK));
		inv.addItem(new ItemStack(Material.FIREWORK_CHARGE));
		inv.addItem(new ItemStack(Material.COOKED_BEEF));
		inv.addItem(new ItemStack(Material.BLAZE_ROD));
		inv.addItem(Bau.makeWithNoName(Material.WOOL, 1, 0));
		inv.addItem(Bau.makeWithNoName(Material.WOOL, 1, 3));
		inv.addItem(Bau.makeWithNoName(Material.WOOL, 1, 10));
		inv.addItem(Bau.makeWithNoName(Material.WOOL, 1, 12));

		p.openInventory(inv);
	}

	public static void bausDeOpcoes(Player p, int bau) {
		Inventory inv = Bukkit.createInventory(null, 36, "§7Opções do baú " + bau);

		if (Main.getInstance().getAccountCommon().getPlayerProfile(p.getUniqueId()).getBau(bau).getSize() == 27) {
			inv.setItem(11,
					Bau.setItemWithLore(Material.ENDER_CHEST, 1, "§eExpandir!",
							"§6Preço: §7" + Main.config.getString("bau-expandir"), " ",
							"§7Clique para comprar a expansão do baú."));
		}
		inv.setItem(13, Bau.setItemWithLore(Material.CHEST, 1, "§eAlterar ícone", " ",
				"§7Clique para alterar o ícone desse baú."));
		inv.setItem(15,
				Bau.setItemWithLore(Material.BOOK_AND_QUILL, 1, "§eRenomear", "§7Clique para renomear este baú."));
		inv.setItem(31, Bau.setItem(Material.ARROW, 1, "§aVoltar"));
		p.openInventory(inv);
	}

	public static void abrirConfirmaçao(Player p, TipoDeCompra tipo, int bau) {
		Inventory inv = Bukkit.createInventory(null, 36, "§7Confirmação");
		if (tipo == TipoDeCompra.BAU) {
			inv.setItem(13, Bau.setItemWithLore(Material.CHEST, 1,
					"§aBaú #" + Math.addExact(Main.getInstance().getAccountCommon().getPlayerProfile(p.getUniqueId()).getnBaus(), 1),
					"§e/bau " + Math.addExact(Main.getInstance().getAccountCommon().getPlayerProfile(p.getUniqueId()).getnBaus(), 1),
					" ", "§7Baú básico!"));
		} else if (tipo == TipoDeCompra.EXPANDIR) {
			inv.setItem(13,
					Bau.setItemWithLore(Material.ENDER_CHEST, 1, "§aExpandir baú #" + bau, " ", "§7Expandir baú!"));
		}
		inv.setItem(20, Bau.makedisplay(Material.WOOL, 1, 5, "§aAceitar (Leia abaixo)",
				"§7Tem certeza que deseja comprar este item?"));
		inv.setItem(24, Bau.makedisplay(Material.WOOL, 1, 14, "§cNegar", "§7Cancelar esta operação."));
		p.openInventory(inv);
	}

}
