package no.brannstrom.Bounty.handlers;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MainHandler {
	
	public static void setBounty(Player setter, Player receiver, int amount) {
		
	}
	
	public static String colorText(ChatColor chatColor, String string) {
		String[] args = string.split(" ");
		String result = "";
		for(String str : args) {
			result += chatColor + str + " ";
		}
		
		return result;
	}
	
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        int i = Integer.parseInt(strNum);
	        i = i+1;
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	public static void sendErrorMessage(Player p) {
		p.sendMessage(ChatColor.RED + "-------[ Bounty ]------");
		p.sendMessage(ChatColor.DARK_RED + "Use: '" + ChatColor.RED + "/bounty <player> <amount>" + ChatColor.DARK_RED + "' to set a bounty");
		p.sendMessage(ChatColor.DARK_RED + "Use: '" + ChatColor.RED + "/bounties [player/online/offline]" + ChatColor.DARK_RED + "' to see a specific or all bounties");
	}
	
	public static int getOfflineMaxPage() {
		return (int) Math.ceil((double) MemoryHandler.bounties.size()/10);
	}
	
	public static int getOnlineMaxPage() {
		int i = 0;
		for(Map.Entry<String, Double> entry : MemoryHandler.bounties.entrySet()) {
			UUID uuid = UUID.fromString(entry.getKey());
			if(Bukkit.getPlayer(uuid) != null) {
				i++;
			}
		}
		
		return (int) Math.ceil((double) i/10);
	}
}
