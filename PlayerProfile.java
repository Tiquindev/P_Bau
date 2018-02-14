package me.SkyBauVirtual;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.inventory.Inventory;

public class PlayerProfile {
	
	private UUID uuid;
	private ArrayList<Baus> baus;
	private int nBaus;
	
	public PlayerProfile(UUID uuid) {
		this.uuid = uuid;
		baus = new ArrayList<>();
		nBaus=0;
	}
	
	public int getnBaus() {
		return nBaus;
	}

	public void setnBaus(int nBaus) {
		this.nBaus = nBaus;
	}

	public Inventory getBau(int id) {
		for (Baus bau : baus) {
			if(bau.getId() == id) {
				return bau.getInv();
			}
		}
		return null;
	}
	
	public Baus getBauID(int id) {
		for (Baus bau : baus) {
			if(bau.getId() == id) {
				return bau;
			}
		}
		return null;
	}

	public void adicionarBau() {
		nBaus+=1;
		baus.add(new Baus(getUuid(), getnBaus()));
	}
	
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public ArrayList<Baus> getBaus() {
		return baus;
	}

	public void setBaus(ArrayList<Baus> baus) {
		this.baus = baus;
	}

}
