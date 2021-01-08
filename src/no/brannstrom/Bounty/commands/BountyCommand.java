package no.brannstrom.Bounty.commands;

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

public class BountyCommand implements CommandExecutor {
	
	Economy economy = BountyPlugin.getEconomy();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("This must be sent from ingame");
			return true;
		}
		
		Player p = (Player) sender;
		
		if(args.length == 2) {
			Player t = Bukkit.getPlayer(args[0]);
			if(t != null) {
				
				if(t.getName().equals(p.getName())) {
					p.sendMessage(InfoKeeper.bountiedYourself);
					return true;
				}
				
				Double amount = Double.parseDouble(args[1]);
				Double balance = BountyPlugin.getEconomy().getBalance(p);
				if(amount <= balance) {
					BountyPlugin.getEconomy().withdrawPlayer(p, amount);
					Double bAmount = amount;
					boolean hadBounty = false;
					if(MemoryHandler.bounties.containsKey(t.getUniqueId().toString())) {
						double b = MemoryHandler.bounties.get(t.getUniqueId().toString());
						MemoryHandler.bounties.remove(t.getUniqueId().toString());
						bAmount = amount+b;
						hadBounty = true;
					}
					MemoryHandler.bounties.put(t.getUniqueId().toString(), bAmount);
					
					if(hadBounty) {
						p.sendMessage("You increased the bounty on " + t.getName() + " by " + amount + "$, to a total of " + bAmount + "$.");
						t.sendMessage(p.getName() + " increased your bounty by " + amount + "$, to a total of " + bAmount + "$.");
					} else {
						p.sendMessage("You put a " + amount + "$ bounty on " + t.getName() + ".");
						t.sendMessage(p.getName() + " put a " + amount + "$ bounty on your head.");
					}
				} else {
					p.sendMessage(InfoKeeper.insufficientFunds);
				}
			} else {
				p.sendMessage(InfoKeeper.playerNotFound);
			}
		} else {
			p.sendMessage("--- Bounty ---");
			p.sendMessage(ChatColor.YELLOW+ "Use: '" + ChatColor.WHITE + "/bounty <player> <amount>" + ChatColor.YELLOW + "' to set a bounty");
			p.sendMessage(ChatColor.YELLOW + "Use: '" + ChatColor.WHITE + "/bounties [Page]" + ChatColor.YELLOW + "' to see all bounties");
		}
		return true;
	}

}
