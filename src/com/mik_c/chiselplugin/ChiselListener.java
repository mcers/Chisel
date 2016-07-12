package com.mik_c.chiselplugin;

import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class ChiselListener implements Listener{
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.HIGH)
	public void onBlockBreak(PlayerInteractEvent event){
		if(ChiselPlugin.pconfig.containsKey(event.getPlayer().getUniqueId())){
			if(event.getItem().getTypeId() == ChiselPlugin.pconfig.get(event.getPlayer().getUniqueId()).getItem()){
				event.setCancelled(true);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerUse(PlayerInteractEvent event){
		if(ChiselPlugin.pconfig.containsKey(event.getPlayer().getUniqueId())){				//If the player has a config
			PlayerConfig pc = ChiselPlugin.pconfig.get(event.getPlayer().getUniqueId());	//Get de config
			Player p = event.getPlayer();
			if(pc.getItem() == event.getItem().getTypeId()){								//If the item is a chisel
				event.setCancelled(true);													//Don't use the item if it's a chisel for that player
				if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
					ChiselWork.setSelection(event);
				}else{
					switch(pc.getMode()){
					case 0: //Rotate mode
						if(event.getAction().equals(Action.RIGHT_CLICK_AIR)){
							pc.rotateMode();
							event.getPlayer().sendMessage(ChiselPlugin.message(ChiselPlugin.output_whenModeChangesToFixed, 
									true, null, null, null));
						}
						else if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
							if(p.hasPermission("chisel.work.rotate")) ChiselWork.chiselMode1Work(event);
							else event.getPlayer().sendMessage(ChiselPlugin.message(ChiselPlugin.output_whenHasNoPermision, 
									true, null, null, null));
						}
						break;
					case 1: //Fixed mode
						if(event.getAction().equals(Action.RIGHT_CLICK_AIR)){
							pc.rotateMode();
							event.getPlayer().sendMessage(ChiselPlugin.message(ChiselPlugin.output_whenModeChangesToRotate, 
									true, null, null, null));
						}
						else if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
							if(p.hasPermission("chisel.work.fixed")) ChiselWork.chiselMode2Work(event);
							else event.getPlayer().sendMessage(ChiselPlugin.message(ChiselPlugin.output_whenHasNoPermision, 
									true, null, null, null));
						}
						break;
					}
				}
			}
		}
		return;
	}	
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerQuit(PlayerQuitEvent event){
		if(ChiselPlugin.pconfig.containsKey(event.getPlayer().getUniqueId())){
			ChiselPlugin.pconfig.remove(event.getPlayer().getUniqueId());
		}
	}
}
