package no.brannstrom.Bounty.handlers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.neznamy.tab.api.EnumProperty;
import me.neznamy.tab.api.TABAPI;
import me.neznamy.tab.api.TabPlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import no.brannstrom.Bounty.BountyPlugin;

public class MainHandler {

	public static void setBounty(Player p, Player t, double amount) {
		BountyPlugin.getEconomy().withdrawPlayer(p, amount);
		Double bAmount = amount;
		boolean hadBounty = false;
		if(MemoryHandler.bounties.containsKey(t.getUniqueId().toString())) {
			double b = MemoryHandler.bounties.get(t.getUniqueId().toString());
			MemoryHandler.bounties.remove(t.getUniqueId().toString());
			bAmount = amount+b;
			hadBounty = true;
		}
		
		BigDecimal bd = new BigDecimal(bAmount).setScale(2, RoundingMode.HALF_UP);
		double newAmount = bd.doubleValue();
		
		MemoryHandler.bounties.put(t.getUniqueId().toString(), newAmount);
		
		if(hadBounty) {
			p.sendMessage(InfoKeeper.increasedBounty1.replaceAll("<player>", t.getName()).replaceAll("<amount>", String.valueOf(amount)).replaceAll("<newamount>", String.valueOf(newAmount)));
			t.sendMessage(InfoKeeper.increasedBounty2.replaceAll("<player>", p.getName()).replaceAll("<amount>", String.valueOf(amount)).replaceAll("<newamount>", String.valueOf(newAmount)));
			setOverName(t,newAmount);
		} else {
			p.sendMessage(InfoKeeper.setBounty1.replaceAll("<player>", t.getName()).replaceAll("<amount>", String.valueOf(amount)));
			t.sendMessage(InfoKeeper.setBounty2.replaceAll("<player>", p.getName()).replaceAll("<amount>", String.valueOf(amount)));
			setOverName(t,newAmount);
		}
		

	}
	
	public static void setOverName(Player p, double amount) {
		TabPlayer tp = TABAPI.getPlayer(p.getUniqueId());
		tp.setValuePermanently(EnumProperty.ABOVENAME, ChatColor.DARK_RED + "Bounty: $" + amount);
	}
	
	public static void removeOverName(Player p) {
		TabPlayer tp = TABAPI.getPlayer(p.getUniqueId());
		try {
			tp.setValuePermanently(EnumProperty.ABOVENAME, "");
		} catch(Exception e) {
			e.printStackTrace();
		}
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
		p.sendMessage(ChatColor.DARK_RED + "-------[ Bounty ]------");
		p.sendMessage(ChatColor.RED + "Use '" + ChatColor.DARK_RED + "/bounty <player> <amount>" + ChatColor.RED + "' to set a bounty");
		p.sendMessage(ChatColor.RED + "Use '" + ChatColor.DARK_RED + "/bounty <player>" + ChatColor.RED + "' to see a players bounty");
		
		TextComponent online = new TextComponent(MainHandler.colorText(ChatColor.DARK_RED, "[SHOW]"));
		online.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bounties online 1"));
		online.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Show all online bounties")));
		
		TextComponent offline = new TextComponent(MainHandler.colorText(ChatColor.DARK_RED, "[SHOW]"));
		offline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bounties offline 1"));
		offline.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Show all bounties")));
		
		p.spigot().sendMessage(new ComponentBuilder().append(ChatColor.RED + "Show all bounties: ").append(offline).create());
		p.spigot().sendMessage(new ComponentBuilder().append(ChatColor.RED + "Show online bounties: ").append(online).create());
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