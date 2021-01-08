package no.brannstrom.Bounty.handlers;

import org.bukkit.configuration.file.FileConfiguration;

import net.md_5.bungee.api.ChatColor;
import no.brannstrom.Bounty.BountyPlugin;

public class InfoKeeper {
	
	static FileConfiguration config = BountyPlugin.getPlugin(BountyPlugin.class).getConfig();
	
	public static String bountiedYourself = ChatColor.translateAlternateColorCodes('&', config.getString("Bountied Yourself"));
	public static String insufficientFunds = ChatColor.translateAlternateColorCodes('&', config.getString("Insufficient Funds"));
	public static String playerNotFound = ChatColor.translateAlternateColorCodes('&', config.getString("Player Not Found"));
	public static String playerHasNoBounty = ChatColor.translateAlternateColorCodes('&', config.getString("Player Has No Bounty"));
	public static String specificPlayersBounty = ChatColor.translateAlternateColorCodes('&', config.getString("Specific Players Bounty"));
	public static String noPlayerWithBounty = ChatColor.translateAlternateColorCodes('&', config.getString("No Player With Bounty"));
	
}
