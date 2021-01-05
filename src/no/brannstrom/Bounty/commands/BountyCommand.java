package no.brannstrom.Bounty.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BountyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if(args.length == 0) {
			p.sendMessage("-- Bounty --");
			p.sendMessage("Use: '/bounty <player> <amount>' to set a bounty on a player.");
			p.sendMessage("Use: '/bounty list' to see a list of all bounties");
		} else {
			if(args.length == 1 && (args[0].equalsIgnoreCase("list") ||  args[0].equalsIgnoreCase("lists"))) {
				
			} else if(args.length == 2) {
				Player target = Bukkit.getPlayer(args[1]);
				if(target != null) {
					
				} else {
					p.sendMessage("Player is not found.");
				}
			}
		}
		return true;
	}

}
