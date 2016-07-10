package com.mik_c.chiselplugin;

import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;


public class ChiselListener implements Listener{
	@EventHandler(priority=EventPriority.HIGH)
	public void onBlockBreak(PlayerInteractEvent event){
		if(ChiselPlugin.chisel.isSimilar(event.getItem())||ChiselPlugin.chisel2.isSimilar(event.getItem())){
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerUse(PlayerInteractEvent event){
		if(ChiselPlugin.chisel.isSimilar(event.getItem())){
			if(event.getAction().equals(Action.RIGHT_CLICK_AIR)){
				event.getItem().setItemMeta(ChiselPlugin.chisel2.getItemMeta());
				event.getPlayer().sendMessage(ChatColor.DARK_BLUE+"Chisel: "+ChatColor.WHITE+"Modo cambiado a fijo.");
			}
			else if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
				ChiselWork.setSelection(event);
			}
			else if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
				ChiselWork.chiselMode1Work(event);
			}
		}
		else if(ChiselPlugin.chisel2.isSimilar(event.getItem())){
			if(event.getAction().equals(Action.RIGHT_CLICK_AIR)){
				event.getItem().setItemMeta(ChiselPlugin.chisel.getItemMeta());
				event.getPlayer().sendMessage(ChatColor.DARK_BLUE+"Chisel: "+ChatColor.WHITE+"Modo cambiado a rotativo.");
			}
			else if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
				ChiselWork.setSelection(event);
			}
			else if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
				ChiselWork.chiselMode2Work(event);
			}
		}
		return;
	}	
}
