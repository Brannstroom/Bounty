package no.brannstrom.Bounty.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
		if(args.length == 1) {
			OfflinePlayer offlineTarget = Bukkit.getPlayer(args[0]);
			if(offlineTarget != null) {
				if(MemoryHandler.bounties.containsKey(offlineTarget.getUniqueId().toString())) {
					double amount = MemoryHandler.bounties.get(offlineTarget.getUniqueId().toString());
					p.sendMessage(InfoKeeper.specificPlayersBounty.replaceAll("<player>", offlineTarget.getName()).replaceAll("<amount>", String.valueOf(amount)));
				} else {
					p.sendMessage(InfoKeeper.playerHasNoBounty);
				}
			} else {
				p.sendMessage(InfoKeeper.playerNotFound);
			}
		} else if(args.length == 2) {
			Player t = Bukkit.getPlayer(args[0]);
			if(t != null) {
				if(t.getName().equals(p.getName())) {
					p.sendMessage(InfoKeeper.bountiedYourself);
					return true;
				}
				if(MainHandler.isNumeric(args[1])) {
					Double amount = Double.parseDouble(args[1]);
					Double balance = BountyPlugin.getEconomy().getBalance(p);
					if(amount <= balance) {
						MainHandler.setBounty(p, t, amount);
					} else {
						p.sendMessage(InfoKeeper.insufficientFunds);
					}
				} else {
					MainHandler.sendErrorMessage(p);
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
