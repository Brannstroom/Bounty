package no.brannstrom.Bounty;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import no.brannstrom.Bounty.commands.BountyCommand;
import no.brannstrom.Bounty.listeners.MainListener;

public class BountyPlugin extends JavaPlugin {
	
	public FileConfiguration config;

	public static BountyPlugin instance;

	public void onEnable() {
		instance = this;
		loadListeners();
		loadCommands();

		getConfig();
		saveDefaultConfig();

		System.out.println(ChatColor.DARK_GREEN + "[Bounty] " + ChatColor.WHITE + "Successfully started Bounty v1.0");
		
	}

	public void onDisable() {
	
		
		System.out.println(ChatColor.DARK_RED + "[Bounty] " + ChatColor.WHITE + "Bounty shutting down");
	}

	private void loadListeners() {
		getServer().getPluginManager().registerEvents(new MainListener(), this);
	}

	private void loadCommands() {
		
		registerCommand("bounty", new BountyCommand());
		
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
}