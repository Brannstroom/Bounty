package no.brannstrom.Bounty;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import net.milkbowl.vault.economy.Economy;
import no.brannstrom.Bounty.commands.BountiesCommand;
import no.brannstrom.Bounty.commands.BountyCommand;
import no.brannstrom.Bounty.handlers.MemoryHandler;
import no.brannstrom.Bounty.listeners.MainListener;

public class BountyPlugin extends JavaPlugin {

	public FileConfiguration config;

	public File bountiesConfigFile;
	public FileConfiguration bountiesConfig;

	private static final Logger log = Logger.getLogger("Minecraft");
	private static Economy econ = null;

	public static BountyPlugin instance;

	public void onEnable() {

		instance = this;

		loadListeners();
		loadCommands();

		if (!setupEconomy() ) {
			log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		config = getConfig();
		saveDefaultConfig();

		createBountiesConfig();

		restoreBounties();

		System.out.println(ChatColor.DARK_GREEN + "[Bounty] " + ChatColor.WHITE + "Successfully started Bounty v1.0");

	}

	public void onDisable() {
		if(!MemoryHandler.bounties.isEmpty()) {
			saveBounties();
		}
	}

	private void loadListeners() {
		getServer().getPluginManager().registerEvents(new MainListener(), this);
	}

	private void loadCommands() {

		registerCommand("bounty", new BountyCommand());
		registerCommand("bounties", new BountiesCommand());

	}

	public FileConfiguration getBountiesConfig() {
		return this.bountiesConfig;
	}

	public void createBountiesConfig() {
		bountiesConfigFile = new File(getDataFolder(), "bountiesConfig.yml");
		if(!bountiesConfigFile.exists()) {
			bountiesConfigFile.getParentFile().mkdirs();
			saveResource("bountiesConfig.yml", true);
		}

		bountiesConfig = new YamlConfiguration();
		try {
			bountiesConfig.load(bountiesConfigFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

	}

	public void saveBounties() {
		bountiesConfig.getConfigurationSection("bounties").getKeys(false).forEach(key -> {
			bountiesConfig.set("bounties." + key, null);
		});
		
		for(Map.Entry<String, Double> entry : MemoryHandler.bounties.entrySet()) {
			double amount = entry.getValue();
			bountiesConfig.set("bounties." + entry.getKey() + ".amount", amount);
		}
		try {
			bountiesConfig.save(bountiesConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void restoreBounties() {
			bountiesConfig.getConfigurationSection("bounties").getKeys(false).forEach(key -> {
				double amount = bountiesConfig.getDouble("bounties." + key + ".amount");
				MemoryHandler.bounties.put(key, amount);
			});
	}

	public void registerCommand(String name, CommandExecutor executor, String... aliases) {
		try {
			Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			constructor.setAccessible(true);

			PluginCommand command = constructor.newInstance(name, this);

			command.setExecutor(executor);
			command.setAliases(Lists.newArrayList(aliases));
			if (executor instanceof TabCompleter) {
				command.setTabCompleter((TabCompleter) executor);
			}
			this.getCommandMap().register("bounty", command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private CommandMap getCommandMap() {
		try {
			org.bukkit.Server server = Bukkit.getServer();
			Field commandMap = server.getClass().getDeclaredField("commandMap");
			commandMap.setAccessible(true);
			return (CommandMap) commandMap.get(server);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
			return null;
		}
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	public static Economy getEconomy() {
		return econ;
	}
}