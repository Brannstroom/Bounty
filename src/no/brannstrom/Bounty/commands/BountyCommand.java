package no.brannstrom.Bounty.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;
import no.brannstrom.Bounty.BountyPlugin;
import no.brannstrom.Bounty.handlers.InfoKeeper;
import no.brannstrom.Bounty.handlers.MainHandler;
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
						p.sendMessage(InfoKeeper.increasedBounty1.replaceAll("<player>", t.getName()).replaceAll("<amount>", String.valueOf(amount)).replaceAll("<newamount>", String.valueOf(bAmount)));
						t.sendMessage(InfoKeeper.increasedBounty2.replaceAll("<player>", p.getName()).replaceAll("<amount>", String.valueOf(amount)).replaceAll("<newamount>", String.valueOf(bAmount)));

					} else {
						p.sendMessage(InfoKeeper.setBounty1.replaceAll("<player>", t.getName()).replaceAll("<amount>", String.valueOf(amount)));
						t.sendMessage(InfoKeeper.setBounty2.replaceAll("<player>", p.getName()).replaceAll("<amount>", String.valueOf(amount)));
					}
				} else {
					p.sendMessage(InfoKeeper.insufficientFunds);
				}
			} else {
				p.sendMessage(InfoKeeper.playerNotFound);
			}
		} else {
			MainHandler.sendErrorMessage(p);
		}
		return true;
	}

}
