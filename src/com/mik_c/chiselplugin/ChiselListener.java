package com.mik_c.chiselplugin;

import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class ChiselListener implements Listener{
	@EventHandler(priority=EventPriority.HIGH)
	public void onBlockBreak(PlayerInteractEvent event){
		if(ChiselPlugin.chisel.isSimilar(event.getItem())||ChiselPlugin.chisel2.isSimilar(event.getItem())){
			event.setCancelled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerUse(PlayerInteractEvent event){
		//La implementación con item chisel será cambiada
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
		
		//Nueva implementación:
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
							event.getPlayer().sendMessage(ChatColor.GOLD+"Chisel: "+ChatColor.WHITE+"Modo cambiado a fijo.");
						}
						else if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
							if(p.hasPermission("chisel.work.rotate")) ChiselWork.chiselMode1Work(event);
							else event.getPlayer().sendMessage(ChatColor.RED+" No tienes permiso para eso");	
						}
						break;
					case 1: //Fixed mode
						if(event.getAction().equals(Action.RIGHT_CLICK_AIR)){
							pc.rotateMode();
							event.getPlayer().sendMessage(ChatColor.GOLD+"Chisel: "+ChatColor.WHITE+"Modo cambiado a rotativo.");
						}
						else if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
							if(p.hasPermission("chisel.work.fixed")) ChiselWork.chiselMode2Work(event);
							else p.sendMessage(ChatColor.RED+" No tienes permiso para eso");
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
