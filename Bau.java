package me.SkyBauVirtual.Commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.SkyBauVirtual.Baus;
import me.SkyBauVirtual.Eventos;
import me.SkyBauVirtual.Main;
import me.SkyBauVirtual.PlayerProfile;
import me.SkyBauVirtual.TipoDeCompra;

public class Bau implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("bau")) {
			if (!(sender instanceof Player)) {
				return true;
			}
			Player p = (Player) sender;
			if (Main.getInstance().getAccountCommon().getPlayerProfile(p.getUniqueId()).getBaus().size() == 0) {
				Eventos.abrirConfirmaçao(p, TipoDeCompra.BAU, 0);
				return true;
			}
			if (args.length == 0) {
				abrirInventário(p);
				return true;
			}
			if(isInteger(args[0])) {
				Integer num = Integer.parseInt(args[0]);
				if(ExisteBau(p, num) != null) {
					p.openInventory(ExisteBau(p, num).getInv());
				}else{
					p.sendMessage("§cVocê não possui esse bau!");
				}
			}else{
				abrirInventário(p);
			}
			

		}
		return false;
	}
	
	public Baus ExisteBau(Player p, int bauzin) {
		for (Baus bau : Main.getInstance().getAccountCommon().getPlayerProfile(p.getUniqueId()).getBaus()) {
			if(bau.getId() == bauzin) {
				return bau;
			}
		}
		return null;
	}
	
	public boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}

	public static void abrirInventário(Player p) {
		int slots = 27;
		PlayerProfile pf = Main.getInstance().getAccountCommon().getPlayerProfile(p.getUniqueId());
		int[] slotsPossiveis = { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37,
				38, 39, 40, 41, 42, 43 };
		if (pf.getBaus().size() > 7) {
			slots += 9;
		}
		if (pf.getBaus().size() > 14) {
			slots += 9;
		}
		if (pf.getBaus().size() > 21) {
			slots += 9;
		}
		Inventory inv = Bukkit.createInventory(null, slots, "Baús de " + p.getName());

		ItemStack enderChest = setItemWithLore(Material.ENDER_CHEST, 1, "§aEnderChest", "§e/enderchest", " ",
				"§7Clique para abrir seu enderchest");
		ItemStack bauDeCompra = setItemWithLore(Material.CHEST, 1, "§eComprar baú!",
				"§6Preço: " + Main.config.getString("bau-preco"), " ", "§7Clique para comprar um novo baú!");

		int derp = 0;
		for (Baus bau : pf.getBaus()) {
			inv.setItem(slotsPossiveis[derp], setItemWithLore(bau.getItem(), 1, bau.getNome(), "§e/bau " + bau.getId(),
					" ", "§7Baú " + bau.getTipo(), " ", "§7Botão esquerdo: §fAbrir baú", "§7Botão direito: §fOpções"));
			derp += 1;
		}

		if (inv.getSize() == 27) {
			inv.setItem(21, bauDeCompra);
			inv.setItem(23, enderChest);
		} else if (inv.getSize() == 36) {
			inv.setItem(30, bauDeCompra);
			inv.setItem(32, enderChest);
		} else if (inv.getSize() == 45) {
			inv.setItem(39, bauDeCompra);
			inv.setItem(41, enderChest);
		} else if (inv.getSize() == 54) {
			inv.setItem(48, bauDeCompra);
			inv.setItem(50, enderChest);
		}

		p.openInventory(inv);
	}

	public void adicionarItensTeste(Player p) {
		ItemStack bloquin = new ItemStack(Material.STONE);
		bloquin.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
		ItemMeta bloquinmeta = bloquin.getItemMeta();
		bloquinmeta.setDisplayName("§6Aquela corzinha bem lecau");
		ArrayList<String> lora = new ArrayList<>();
		lora.add("§aLinha 1");
		lora.add("§bLinha 2");
		lora.add("§cLinha 3");
		bloquinmeta.setLore(lora);
		bloquin.setItemMeta(bloquinmeta);
		p.getInventory().addItem(bloquin);
	}

	public static ItemStack setItem(Material m, int qnt, String nome) {
		ItemStack item = new ItemStack(m, qnt);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(nome);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack make(Material material, int amount, int shrt, String displayName) {
		ItemStack item = new ItemStack(material, amount, (short) shrt);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack setItemWithLore(Material material, int amount, String nome, String... lore) {
		ArrayList<String> lora = new ArrayList<String>();
		String[] arrayOfString;
		int j = (arrayOfString = lore).length;
		for (int i = 0; i < j; i++) {
			String coiso = arrayOfString[i];
			lora.add(coiso);
		}
		ItemStack item = new ItemStack(material, amount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(nome);
		meta.setLore(lora);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack makeWithNoName(Material material, int amount, int shrt) {
		ItemStack item = new ItemStack(material, amount, (short) shrt);
		ItemMeta meta = item.getItemMeta();
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack setSkin(String nick, String HeadName) {
		ItemStack skullItem = new ItemStack(Material.SKULL_ITEM);
		skullItem.setDurability((short) 3);
		SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
		skullMeta.setOwner(nick);
		skullMeta.setDisplayName(HeadName);
		skullItem.setItemMeta(skullMeta);
		return skullItem;
	}

	public static ItemStack makedisplay(Material material, int amount, int shrt, String displayName, String... lore) {
		ItemStack item = new ItemStack(material, amount, (short) shrt);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		ArrayList<String> lora = new ArrayList<String>();
		String[] arrayOfString;
		int j = (arrayOfString = lore).length;
		for (int i = 0; i < j; i++) {
			String coiso = arrayOfString[i];
			lora.add(coiso);
		}
		meta.setLore(lora);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack setSkinWithLore(Player p, String nome, String... lore) {
		ArrayList<String> lora = new ArrayList<String>();
		String[] arrayOfString;
		int j = (arrayOfString = lore).length;
		for (int i = 0; i < j; i++) {
			String coiso = arrayOfString[i];
			lora.add(coiso);
		}
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1);
		item.setDurability((short) 3);
		SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
		skullMeta.setOwner(p.getName());
		skullMeta.setDisplayName(nome);
		skullMeta.setLore(lora);
		item.setItemMeta(skullMeta);
		return item;
	}
}
