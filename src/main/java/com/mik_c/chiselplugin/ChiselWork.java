package com.mik_c.chiselplugin;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChiselWork {
	@SuppressWarnings("deprecation")
	public static void chiselReplace(PlayerInteractEvent event, int type, byte data){
		if(event.getPlayer().hasPermission("chisel.work.rotate") || event.getPlayer().hasPermission("chisel.work.fixed")){
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
		if(!ChiselPlugin.pconfig.containsKey(event.getPlayer().getUniqueId())){
			ChiselPlugin.pconfig.put(event.getPlayer().getUniqueId(), new PlayerConfig());
		}
		
		int type = event.getClickedBlock().getTypeId();
		byte data = event.getClickedBlock().getData();
		byte[] selection = ChiselPlugin.pconfig.get(event.getPlayer().getUniqueId()).getSelection();
		for(int fam=0; fam<ChiselPlugin.families.size(); fam++){
			int i = ChiselPlugin.families.get(fam).isThisFamily(type, data);
			if(i > -1){
				int sel = selection[fam];
				chiselReplace(event,ChiselPlugin.families.get(fam).type[sel],ChiselPlugin.families.get(fam).data[sel]);
				return;
			}
		}
		return;
	}
	
	@SuppressWarnings("deprecation")
	public static void setSelection(PlayerInteractEvent event){
		if(!ChiselPlugin.pconfig.containsKey(event.getPlayer().getUniqueId())){
			ChiselPlugin.pconfig.put(event.getPlayer().getUniqueId(), new PlayerConfig());
		}
		
		int type = event.getClickedBlock().getTypeId();
		byte data = event.getClickedBlock().getData();
		for(int fam=0; fam<ChiselPlugin.families.size(); fam++){
			int i = ChiselPlugin.families.get(fam).isThisFamily(type, data);
			if(i > -1){
				ChiselPlugin.pconfig.get(event.getPlayer().getUniqueId()).setSelection(fam, (byte)i);
				event.getPlayer().sendMessage(ChiselPlugin.message(ChiselPlugin.output_whenBlockSelectionMade, 
						true, event.getPlayer().getItemInHand().getItemMeta().getDisplayName(), ChiselPlugin.families.get(fam).familyname, 
						ChiselPlugin.families.get(fam).lore[i]));
				return;
			}
		}
		return;
	}
}
