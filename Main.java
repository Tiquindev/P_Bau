package me.SkyBauVirtual;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;

import me.SkyBauVirtual.Commands.Bau;
import me.SkyBauVirtual.Config.MyConfig;
import me.SkyBauVirtual.Config.MyConfigManager;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin{
	
	private static Main plugin;
	MyConfigManager manager;
	public static MyConfig jogadores, config;
	public Gson gson;
	private AccountCommon accountCommon;
	public static Economy economy = null;
	
	public void onLoad() {
		plugin = this;
		manager = new MyConfigManager(this);
		accountCommon = new AccountCommon();
		gson = new Gson();
	}
	
	public void onEnable() {
		jogadores = manager.getNewConfig("jogadores.yml");
		config = manager.getNewConfig("config.yml");
		setarConfig();
		getCommand("bau").setExecutor(new Bau());
		Bukkit.getPluginManager().registerEvents(new Eventos(), this);
		if(Bukkit.getOnlinePlayers().size() > 0) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				Main.getInstance().getAccountCommon().carregarPlayerProfile(p.getUniqueId());
			}
		}
		setupEconomy();
	}
	
	public static boolean temPermParaBau(Player p, int num) {
		int Permissao = 0;
		for (int i = 28; i > 0; i--) {
			if(p.hasPermission("sbau." + i)) {
				Permissao = i;
				break;
			}
		}
		if(num <= Permissao) {
			return true;
		}else{
			return false;
		}
	}
	
	private boolean setupEconomy()
	{
	RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
	if (economyProvider != null) {
	economy = economyProvider.getProvider();
	}
	 
	return (economy != null);
	}
	
	public void setarConfig() {
		if(!config.contains("bau-preco")) {
			config.set("bau-preco", 5000);
		}
		if(!config.contains("bau-expandir")) {
			config.set("bau-expandir", 5000);
		}
		config.saveConfig();
	}
	
	public void onDisable() {
		for (PlayerProfile pf : getAccountCommon().getPlayers()) {
			getAccountCommon().savePlayerProfile(pf);
		}
	}

	public AccountCommon getAccountCommon() {
		return accountCommon;
	}

	public static Main getInstance() {
		return plugin;
	}

	public Gson getGson() {
		return gson;
	}

}
