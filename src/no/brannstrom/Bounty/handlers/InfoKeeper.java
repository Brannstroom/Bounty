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
	public static String noOnlinePlayerWithBounty = ChatColor.translateAlternateColorCodes('&', config.getString("No Online Player With Bounty"));
	public static String notEnoughPages = ChatColor.translateAlternateColorCodes('&', config.getString("Not Enough Pages"));
	public static String pageMustBeNumber = ChatColor.translateAlternateColorCodes('&', config.getString("Page Must Be Number"));
	
	public static String setBounty1 = ChatColor.translateAlternateColorCodes('&', config.getString("Set Bounty 1"));
	public static String setBounty2 = ChatColor.translateAlternateColorCodes('&', config.getString("Set Bounty 2"));
	public static String increasedBounty1 = ChatColor.translateAlternateColorCodes('&', config.getString("Increased Bounty 1"));
	public static String increasedBounty2 = ChatColor.translateAlternateColorCodes('&', config.getString("Increased Bounty 2"));
	public static String claimedBountyKiller = ChatColor.translateAlternateColorCodes('&', config.getString("Claimed Bounty Killer"));
	public static String claimedBountyKilled = ChatColor.translateAlternateColorCodes('&', config.getString("Claimed Bounty Killed"));
	public static String claimedBountyBroadcast = ChatColor.translateAlternateColorCodes('&', config.getString("Claimed Bounty Broadcast"));
	public static String configReloaded = ChatColor.translateAlternateColorCodes('&', config.getString("Config Reloaded"));
	
	public static String overHead = ChatColor.translateAlternateColorCodes('&', config.getString("Over Head"));
	public static Boolean taxes = config.getBoolean("Taxes");
	public static int taxPercent = Integer.parseInt(config.getString("Tax Percentage"));
	
	public static void updateValues() { 
		bountiedYourself = ChatColor.translateAlternateColorCodes('&', config.getString("Bountied Yourself"));
		insufficientFunds = ChatColor.translateAlternateColorCodes('&', config.getString("Insufficient Funds"));
		playerNotFound = ChatColor.translateAlternateColorCodes('&', config.getString("Player Not Found"));
		playerHasNoBounty = ChatColor.translateAlternateColorCodes('&', config.getString("Player Has No Bounty"));
		specificPlayersBounty = ChatColor.translateAlternateColorCodes('&', config.getString("Specific Players Bounty"));
		noPlayerWithBounty = ChatColor.translateAlternateColorCodes('&', config.getString("No Player With Bounty"));
		noOnlinePlayerWithBounty = ChatColor.translateAlternateColorCodes('&', config.getString("No Online Player With Bounty"));
		notEnoughPages = ChatColor.translateAlternateColorCodes('&', config.getString("Not Enough Pages"));
		pageMustBeNumber = ChatColor.translateAlternateColorCodes('&', config.getString("Page Must Be Number"));
		
		setBounty1 = ChatColor.translateAlternateColorCodes('&', config.getString("Set Bounty 1"));
		setBounty2 = ChatColor.translateAlternateColorCodes('&', config.getString("Set Bounty 2"));
		increasedBounty1 = ChatColor.translateAlternateColorCodes('&', config.getString("Increased Bounty 1"));
		increasedBounty2 = ChatColor.translateAlternateColorCodes('&', config.getString("Increased Bounty 2"));
		claimedBountyKiller = ChatColor.translateAlternateColorCodes('&', config.getString("Claimed Bounty Killer"));
		claimedBountyKilled = ChatColor.translateAlternateColorCodes('&', config.getString("Claimed Bounty Killed"));
		claimedBountyBroadcast = ChatColor.translateAlternateColorCodes('&', config.getString("Claimed Bounty Broadcast"));
		
		overHead = ChatColor.translateAlternateColorCodes('&', config.getString("Over Head"));
		taxes = config.getBoolean("Taxes");
		taxPercent = Integer.parseInt(config.getString("Tax Percentage"));
	}
}
