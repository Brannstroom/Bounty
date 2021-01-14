package no.brannstrom.Bounty.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import no.brannstrom.Bounty.BountyPlugin;
import no.brannstrom.Bounty.handlers.InfoKeeper;
import no.brannstrom.Bounty.handlers.MainHandler;
import no.brannstrom.Bounty.handlers.MemoryHandler;

public class MainListener implements Listener {


	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		if(MemoryHandler.bounties.containsKey(p.getUniqueId().toString())) {
			if(p.getLastDamageCause() != null && p.getLastDamageCause().getCause() != null) {
				if (p.getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK || p.getLastDamageCause().getCause() == DamageCause.PROJECTILE){
					EntityDamageByEntityEvent dmgEvent = (EntityDamageByEntityEvent) p.getLastDamageCause();
					if(dmgEvent.getCause() == DamageCause.PROJECTILE) {
						Projectile arrow = (Projectile) dmgEvent.getDamager();
						Entity killer = (Entity) arrow.getShooter();
						if (killer instanceof Player) {
							Player k = (Player) killer;
							double amount = MemoryHandler.bounties.get(p.getUniqueId().toString());
							if(InfoKeeper.taxes) {
								double amountTax = InfoKeeper.taxPercent;
								double amountAfterTax = amount-((amount*amountTax)/100);
								BountyPlugin.getEconomy().depositPlayer(k, amountAfterTax);
							} else {
								BountyPlugin.getEconomy().depositPlayer(k, amount);
							}
							MemoryHandler.bounties.remove(p.getUniqueId().toString());
							p.sendMessage(InfoKeeper.claimedBountyKilled.replaceAll("<player>", k.getName()).replaceAll("<amount>", String.valueOf(amount)));
							k.sendMessage(InfoKeeper.claimedBountyKiller.replaceAll("<player>", p.getName()).replaceAll("<amount>", String.valueOf(amount)).replaceAll("<tax>", String.valueOf(InfoKeeper.taxPercent)));
							Bukkit.broadcastMessage(InfoKeeper.claimedBountyBroadcast.replaceAll("<killer>", k.getName()).replaceAll("<killed>", p.getName()).replaceAll("<amount>", String.valueOf(amount)));
							MainHandler.removeOverName(p);
						}
					} else {
						if(dmgEvent.getDamager() instanceof Player) {
							Player k = (Player) dmgEvent.getDamager();
							double amount = MemoryHandler.bounties.get(p.getUniqueId().toString());
							if(InfoKeeper.taxes) {
								double amountTax = InfoKeeper.taxPercent;
								double amountAfterTax = amount-((amount*amountTax)/100);
								BountyPlugin.getEconomy().depositPlayer(k, amountAfterTax);
							} else {
								BountyPlugin.getEconomy().depositPlayer(k, amount);
							}
							MemoryHandler.bounties.remove(p.getUniqueId().toString());
							p.sendMessage(InfoKeeper.claimedBountyKilled.replaceAll("<player>", k.getName()).replaceAll("<amount>", String.valueOf(amount)));
							k.sendMessage(InfoKeeper.claimedBountyKiller.replaceAll("<player>", p.getName()).replaceAll("<amount>", String.valueOf(amount)).replaceAll("<tax>", String.valueOf(InfoKeeper.taxPercent)));
							Bukkit.broadcastMessage(InfoKeeper.claimedBountyBroadcast.replaceAll("<killer>", k.getName()).replaceAll("<killed>", p.getName()).replaceAll("<amount>", String.valueOf(amount)));
							MainHandler.removeOverName(p);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if(MemoryHandler.bounties.containsKey(p.getUniqueId().toString())) {
			double amount = MemoryHandler.bounties.get(p.getUniqueId().toString());
			MainHandler.setOverName(p, amount);
		} else {
			MainHandler.removeOverName(p);
		}
	}
}
