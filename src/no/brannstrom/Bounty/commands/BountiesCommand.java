package no.brannstrom.Bounty.commands;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;
import no.brannstrom.Bounty.BountyPlugin;
import no.brannstrom.Bounty.handlers.InfoKeeper;
import no.brannstrom.Bounty.handlers.MemoryHandler;

public class BountiesCommand implements CommandExecutor {

	Economy economy = BountyPlugin.getEconomy();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("This must be sent from ingame");
			return true;
		}

		Player p = (Player) sender;

		if(args.length == 0) {

			if(MemoryHandler.bounties.isEmpty()) {
				p.sendMessage(InfoKeeper.noPlayerWithBounty);
				return true;
			}

			showBountyPage(p);
		} else if(args.length == 1) {
			Player t = Bukkit.getPlayer(args[0]);
			if(t != null) {
				if(MemoryHandler.bounties.containsKey(t.getUniqueId().toString())) {
					double amount = MemoryHandler.bounties.get(t.getUniqueId().toString());
					p.sendMessage(InfoKeeper.specificPlayersBounty.replaceAll("<player>", t.getName()).replaceAll("<amount>", String.valueOf(amount)));
				} else {
					p.sendMessage(InfoKeeper.playerHasNoBounty);
				}
			} else {
				p.sendMessage(InfoKeeper.playerNotFound);
			}
		} else {
			p.sendMessage("--- Bounty ---");
			p.sendMessage(ChatColor.YELLOW + "Use: '" + ChatColor.WHITE + "/bounty <player> <amount>" + ChatColor.YELLOW + "' to set a bounty");
			p.sendMessage(ChatColor.YELLOW + "Use: '" + ChatColor.WHITE + "/bounties [Player]" + ChatColor.YELLOW + "' to see all bounties or a specific");
		}

		return true;
	}

	public void showBountyPage(Player p) {

		Map<String, Double> bounties = new LinkedHashMap<>();

		MemoryHandler.bounties.entrySet()
		.stream()
		.sorted(Map.Entry.comparingByValue())
		.forEachOrdered(entry ->
		bounties.put(entry.getKey(), entry.getValue()));


		int shownBounties = 10;
		List<String> uuids = bounties.entrySet().stream()
				.map(Map.Entry::getKey)
				.sorted()
				.limit(shownBounties)
				.collect(Collectors.toList());
		p.sendMessage(ChatColor.RED + "----- Top Online Bounties -----");
		for(int i = 0; i < uuids.size(); i++) {
			p.sendMessage("" + ChatColor.DARK_RED + (i+1) + ChatColor.RED + ". " + ChatColor.DARK_RED + Bukkit.getPlayer(UUID.fromString(uuids.get(i))).getName() + ChatColor.RED + " has a bounty of " + ChatColor.DARK_RED + MemoryHandler.bounties.get(uuids.get(i)) + "$" + ChatColor.RED + ".");
		}
		p.sendMessage(ChatColor.RED + "-----------------------------------------");
	}

	//	private void showBountyPage(Player p, int page, int maxPage) {
	//
	//		List<String> uuids = new ArrayList<>();
	//		List<Double> amounts = new ArrayList<>();
	//
	//		for(Map.Entry<String, Bounty> entry : MemoryHandler.bounties.entrySet()) {
	//			Player t = Bukkit.getPlayer(UUID.fromString(entry.getKey()));
	//			if(t.isOnline()) {
	//				Bounty b = entry.getValue();
	//				uuids.add(b.getBountiedUser());
	//				amounts.add(b.getAmount());
	//			}
	//		}
	//
	//		int min = (page*10);
	//		int max = (page*10)+10;
	//		p.sendMessage("--- Bounties ---");
	//		for(min = min-0; min < max; min++) {
	//			Bukkit.broadcastMessage("min: " + min + " | max: " + max + " | size: " + MemoryHandler.bounties.size());
	//			if(min < MemoryHandler.bounties.size()) {
	//				Player t = Bukkit.getPlayer(UUID.fromString(uuids.get(min)));
	//				p.sendMessage(min + ". " + t.getName() + " goes for " + amounts.get(min) + "$.");
	//			}
	//		}
	//
	//		TextComponent msg1 = new TextComponent(MainHandler.colorText(ChatColor.DARK_RED, "≪ Previous"));
	//		msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bounties " + (page-1)));
	//		msg1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Show previous page")));
	//
	//		TextComponent msg2 = new TextComponent(MainHandler.colorText(ChatColor.DARK_RED, "Next ≫"));
	//		msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bounties " + (page+1)));
	//		msg2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Show next page")));
	//
	//		if(page == 1) {
	//			if(!(page >= maxPage)) {
	//				p.sendMessage("        " + " | " + msg2);
	//			}
	//		} else {
	//			if(!(page >= maxPage)) {
	//				p.sendMessage(msg1 + " | ");
	//			} else {
	//				p.sendMessage(msg1 + " | " + msg2);
	//			}
	//		}
	//
	//	}
}