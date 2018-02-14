package me.SkyBauVirtual;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;

public class AccountCommon {

	private HashMap<UUID, PlayerProfile> players = new HashMap<UUID, PlayerProfile>();

	public void loadPlayerProfile(UUID uuid, PlayerProfile player) {
		if (this.players.containsKey(uuid)) {
			return;
		}
		this.players.put(uuid, player);
	}

	public PlayerProfile getPlayerProfile(UUID uuid) {
		if (!this.players.containsKey(uuid)) {
			return null;
		}
		return (PlayerProfile) this.players.get(uuid);
	}
	
	public void carregarPlayerProfile(UUID uuid) {
		if(Main.jogadores.contains(uuid.toString())) {
			PlayerProfile pf = (PlayerProfile) Main.getInstance().getGson().fromJson(Main.jogadores.getString(uuid.toString()),
					PlayerProfile.class);
			loadPlayerProfile(uuid, pf);
		}else{
			loadPlayerProfile(uuid, new PlayerProfile(uuid));
			savePlayerProfile(getPlayerProfile(uuid));
		}
	}

	public void unloadPlayerProfile(UUID uuid) {
		if (this.players.containsKey(uuid)) {
			this.players.remove(uuid);
		} else {
			Bukkit.getLogger().log(Level.SEVERE, "NAO FOI POSSIVEL ENCONTRAR " + uuid.toString());
		}
	}

	public void savePlayerProfile(PlayerProfile player) {
		String json = Main.getInstance().getGson().toJson(player);
		try {
			Main.jogadores.set(player.getUuid().toString(), json);
			Main.jogadores.saveConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Collection<PlayerProfile> getPlayers() {
		return this.players.values();
	}

}
