package com.mik_c.chiselplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChiselWork {
	@SuppressWarnings("deprecation")
	public static void chiselReplace(PlayerInteractEvent event, int type, byte data){
		if(event.getPlayer().hasPermission("chisel.work")){
			BlockState oldblock = event.getClickedBlock().getState();
			event.getClickedBlock().setTypeId(type);
			event.getClickedBlock().setData(data);
			BlockPlaceEvent replace = new BlockPlaceEvent(event.getClickedBlock(), oldblock, event.getClickedBlock(), event.getItem(), event.getPlayer(), true);
			Bukkit.getServer().getPluginManager().callEvent(replace);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void chiselMode1Work(PlayerInteractEvent event){
		int type = event.getClickedBlock().getTypeId();
		byte data = event.getClickedBlock().getData();
		for(BlockFamily fam: ChiselPlugin.families){
			int i = fam.isThisFamily(type, data);
			if(i > -1){
				i = (i+1)%fam.type.length;
				chiselReplace(event,fam.type[i],fam.data[i]);
				return;
			}
		}
		return;
	}
	
	@SuppressWarnings("deprecation")
	public static void chiselMode2Work(PlayerInteractEvent event){
		int nconfig = ChiselPlugin.isPlayerConfig(event.getPlayer());
		if(nconfig < 0){
			ChiselPlugin.pconfig.add(new PlayerConfig(event.getPlayer()));
			nconfig = ChiselPlugin.isPlayerConfig(event.getPlayer());
		}
		
		int type = event.getClickedBlock().getTypeId();
		byte data = event.getClickedBlock().getData();
		for(int fam=0; fam<ChiselPlugin.families.size(); fam++){
			int i = ChiselPlugin.families.get(fam).isThisFamily(type, data);
			if(i > -1){
				int sel = ChiselPlugin.pconfig.get(nconfig).selection[fam];
				chiselReplace(event,ChiselPlugin.families.get(fam).type[sel],ChiselPlugin.families.get(fam).data[sel]);
				return;
			}
		}
		return;
	}
	
	@SuppressWarnings("deprecation")
	public static void setSelection(PlayerInteractEvent event){
		int nconfig = ChiselPlugin.isPlayerConfig(event.getPlayer());
		if(nconfig < 0){
			ChiselPlugin.pconfig.add(new PlayerConfig(event.getPlayer()));
			nconfig = ChiselPlugin.isPlayerConfig(event.getPlayer());
		}
		
		int type = event.getClickedBlock().getTypeId();
		byte data = event.getClickedBlock().getData();
		for(int fam=0; fam<ChiselPlugin.families.size(); fam++){
			int i = ChiselPlugin.families.get(fam).isThisFamily(type, data);
			if(i > -1){
				ChiselPlugin.pconfig.get(nconfig).selection[fam] = (byte)i;
				event.getPlayer().sendMessage(ChatColor.DARK_BLUE+"Chisel: "+ChatColor.WHITE+ChiselPlugin.families.get(fam).familyname
						+" -> "+ChiselPlugin.families.get(fam).lore[i]); 
				return;
			}
		}
		return;
	}
}
