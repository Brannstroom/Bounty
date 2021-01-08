package no.brannstrom.Bounty.handlers;

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
}
