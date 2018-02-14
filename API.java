package me.SkyBauVirtual.Config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class API {
	
	public static String InventoryToString(Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            
            dataOutput.writeInt(inventory.getSize());
            
            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }
            
            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }
	public static Inventory StringToInventory(String data, int slots, String nome) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, slots, nome.replaceAll("&", "§"));
    
            int bauzin = dataInput.readInt();
            // Read the serialized inventory
            for (int i = 0; i < bauzin; i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }
            
            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

	/*public static String InventoryToString(Inventory invInventory) {
		String serialization = invInventory.getSize() + ";";
		for (int i = 0; i < invInventory.getSize(); i++) {
			ItemStack is = invInventory.getItem(i);
			if (is != null) {
				String serializedItemStack = new String();

				String isType = String.valueOf(is.getType().getId());
				serializedItemStack += "t@" + isType;

				if (is.getDurability() != 0) {
					String isDurability = String.valueOf(is.getDurability());
					serializedItemStack += ":d@" + isDurability;
				}

				if (is.getAmount() != 1) {
					String isAmount = String.valueOf(is.getAmount());
					serializedItemStack += ":a@" + isAmount;
				}

				if (is.hasItemMeta()) {
					String isMeta = String.valueOf(is.getItemMeta().getDisplayName());
					serializedItemStack += ":m@" + isMeta;
				}

				if (is.hasItemMeta()) {
					String isLore = String.valueOf(is.getItemMeta().getLore());
					if (is.getItemMeta().hasLore()) {
						serializedItemStack += ":l@" + isLore;
					}
				}

				Map<Enchantment, Integer> isEnch = is.getEnchantments();
				if (isEnch.size() > 0) {
					for (Entry<Enchantment, Integer> ench : isEnch.entrySet()) {
						serializedItemStack += ":e@" + ench.getKey().getId() + "@" + ench.getValue();
					}
				}
				
				serialization += i + "#" + serializedItemStack + ";";
			}
		}
		return serialization;
	}*/

	/*public static Inventory StringToInventory(String invString, int slots, String nome) {
		String[] serializedBlocks = invString.split(";");
		String invInfo = serializedBlocks[0];
		Inventory deserializedInventory = Bukkit.getServer().createInventory(null, slots, nome.replaceAll("&", "§"));

		for (int i = 1; i < serializedBlocks.length; i++) {
			String[] serializedBlock = serializedBlocks[i].split("#");
			int stackPosition = Integer.valueOf(serializedBlock[0]);

			if (stackPosition >= deserializedInventory.getSize()) {
				continue;
			}

			ItemStack is = null;
			Boolean createdItemStack = false;

			String[] serializedItemStack = serializedBlock[1].split(":");
			for (String itemInfo : serializedItemStack) {
				String[] itemAttribute = itemInfo.split("@");
				if (itemAttribute[0].equals("t")) {
					is = new ItemStack(Material.getMaterial(Integer.valueOf(itemAttribute[1])));
					createdItemStack = true;
				}
				if (itemAttribute[0].equals("d") && createdItemStack) {
					is.setDurability(Short.valueOf(itemAttribute[1]));
				}
				if (itemAttribute[0].equals("a") && createdItemStack) {
					is.setAmount(Integer.valueOf(itemAttribute[1]));
				}
				if (itemAttribute[0].equals("m") && createdItemStack) {
					ItemMeta isM = is.getItemMeta();
					isM.setDisplayName(itemAttribute[1]);
					is.setItemMeta(isM);
				}
				if (itemAttribute[0].equals("l") && createdItemStack) {
					ItemMeta isM = is.getItemMeta();
					String removeBuckle = itemAttribute[1].substring(1, itemAttribute[1].length() - 1);
					ArrayList<String> l = new ArrayList<String>();
					for (String podpis : removeBuckle.split(", ")) {
						l.add(podpis);
					}
					isM.setLore(l);
					is.setItemMeta(isM);
				}
				if (itemAttribute[0].equals("e") && createdItemStack) {
					is.addUnsafeEnchantment(Enchantment.getById(Integer.valueOf(itemAttribute[1])),
							Integer.valueOf(itemAttribute[2]));
				}
				if(itemAttribute[0].equalsIgnoreCase("c") && createdItemStack) {
					EnchantmentStorageMeta meta = (EnchantmentStorageMeta) is.getItemMeta();
					meta.addStoredEnchant(Enchantment.getById(Integer.valueOf(itemAttribute[1])),
							Integer.valueOf(itemAttribute[2]), true);
				}
			}
			deserializedInventory.setItem(stackPosition, is);
		}

		return deserializedInventory;
	}*/

}
