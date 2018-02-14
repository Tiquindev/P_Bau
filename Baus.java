package me.SkyBauVirtual;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import me.SkyBauVirtual.Config.API;

public class Baus {
	
	private UUID owner;
	private String inventario;
	private int id;
	private int size;
	private String nome;
	private Material item;
	
	public Baus(UUID owner, int id) {
		this.owner = owner;
		this.size = 27;
		this.id = id;
		this.nome = "§aBau #"+id;
		this.item = Material.CHEST;
		inventario = API.InventoryToString(Bukkit.createInventory(null, size));
	}
	
	public String getTipo() {
		if(size == 27) {
			return "básico";
		}else if(size == 54) {
			return "completo!";
		}else{
			return "ERRO";
		}
	}
	
	public Material getItem() {
		return item;
	}

	public void setItem(Material item) {
		this.item = item;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome.replaceAll("&", "§");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Inventory getInv() {
		try {
			return API.StringToInventory(getInventario(), size, "§7Baú "+id+" de " + Bukkit.getPlayer(owner).getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public UUID getOwner() {
		return owner;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
	}

	public String getInventario() {
		return inventario;
	}

	public void setInventario(String inventario) {
		this.inventario = inventario;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
