package no.brannstrom.Bounty.commands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.milkbowl.vault.economy.Economy;
import no.brannstrom.Bounty.BountyPlugin;
import no.brannstrom.Bounty.handlers.InfoKeeper;
import no.brannstrom.Bounty.handlers.MainHandler;
import no.brannstrom.Bounty.handlers.MemoryHandler;

public class BountiesCommand implements CommandExecutor {

	Economy economy = BountyPlugin.getEconomy();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("This must be sent from ingame");
			return true;
		}

		Player p = (Player) sender;

		if(MemoryHandler.bounties.isEmpty()) {
			p.sendMessage(InfoKeeper.noPlayerWithBounty);
			return true;
		}

		if(args.length > 0 && args[0].equalsIgnoreCase("online")) {
			if(MainHandler.getOnlineMaxPage() == 0) {
				p.sendMessage(InfoKeeper.noOnlinePlayerWithBounty);
				return true;
			}
		}

		if(args.length == 2) {
			if(MainHandler.isNumeric(args[1])) {
				if(args[0].equalsIgnoreCase("online")) {
					int page = Integer.parseInt(args[1]);
					if(page <= MainHandler.getOnlineMaxPage()) {
						showBountyPage(p,page,MainHandler.getOnlineMaxPage(),true);
					} else {
						p.sendMessage(InfoKeeper.notEnoughPages.replaceAll("<page>", String.valueOf(page)));
					}
				} else if(args[0].equalsIgnoreCase("offline")) {
					int page = Integer.parseInt(args[1]);
					if(page <= MainHandler.getOfflineMaxPage()) {
						showBountyPage(p,page,MainHandler.getOfflineMaxPage(),false);
					} else {
						p.sendMessage(InfoKeeper.notEnoughPages.replaceAll("<page>", String.valueOf(page)));
					}
				} else {
					MainHandler.sendErrorMessage(p);
				}
			} else {
				p.sendMessage(InfoKeeper.pageMustBeNumber.replaceAll("<page>", args[1]));
			}
		} else {
			MainHandler.sendErrorMessage(p);
		}

		return true;
	}

	public void showBountyPage(Player p, int page, int maxPage, boolean online) {

		LinkedHashMap<String, Double> bounties = new LinkedHashMap<>();

		Comparator<Entry<String, Double>> cmp = Entry.comparingByValue();

		MemoryHandler.bounties.entrySet()
		.stream()
		.sorted(cmp.reversed())
		.forEachOrdered(entry ->
		bounties.put(entry.getKey(), entry.getValue()));

		List<String> uuids = new ArrayList<>(bounties.keySet());

		if(online) {
			List<String> toRemove = new ArrayList<>();
			for(String s : uuids) {
				Player t = Bukkit.getPlayer(UUID.fromString(s));
				if(t == null) {
					toRemove.add(s);
				}
			}
			uuids.removeAll(toRemove);
		}


		String status = "";
		if(online) {
			status += "Online";
		} else {
			status += "Offline";
		}

		p.sendMessage("");
		p.sendMessage("");
		p.sendMessage(ChatColor.DARK_RED + "-----[ Top " + status + " Bounties ]-----");
		for(int i = (page-1)*10; i < page*10; i++) {
			if(i <= uuids.size()-1) {
				String uuid = uuids.get(i);
				OfflinePlayer t = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
				double amount = MemoryHandler.bounties.get(uuid);
				p.sendMessage("" + ChatColor.DARK_RED + (i+1) + ChatColor.RED + ". " + ChatColor.DARK_RED + t.getName() + ChatColor.RED + " has a bounty of " + ChatColor.DARK_RED + "$" + amount + ChatColor.RED + ".");
			}
		}
		p.spigot().sendMessage(getComponentBuilder(status, page, maxPage));
	}

	private BaseComponent[] getComponentBuilder(String status, int page, int maxPage) {

		TextComponent previous = new TextComponent(MainHandler.colorText(ChatColor.DARK_RED, " ≪ Previous"));
		previous.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bounties " + status + " " + (page-1)));
		previous.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Show previous page")));

		TextComponent next = new TextComponent(MainHandler.colorText(ChatColor.DARK_RED, "Next ≫"));
		next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bounties " + status + " " + (page+1)));
		next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Show next page")));

		TextComponent previousFake = new TextComponent(MainHandler.colorText(ChatColor.DARK_RED, "≪ Previous"));
		previousFake.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Show previous page")));

		TextComponent nextFake = new TextComponent(MainHandler.colorText(ChatColor.DARK_RED, "Next ≫ "));
		nextFake.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Show next page")));

		TextComponent minMax = new TextComponent(ChatColor.DARK_RED + "| " + ChatColor.RED + page + ChatColor.DARK_RED + " of " + ChatColor.RED + maxPage + ChatColor.DARK_RED + " | ");
		minMax.setClickEvent(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, ""));
		minMax.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(" ")));

		if(page == maxPage) {
			if(page == 1) {
				return new ComponentBuilder().append(previousFake).append(minMax).append(nextFake).create();
			} else {
				return new ComponentBuilder().append(previous).append(minMax).append(nextFake).create();
			}
		} else {
			if(page == 1) {
				return new ComponentBuilder().append(previousFake).append(minMax).append(next).create();
			} else {
				return new ComponentBuilder().append(previous).append(minMax).append(next).create();
			}
		}		
	}
}