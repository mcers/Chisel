package com.mik_c.chiselplugin;

import org.bukkit.entity.Player;

public class PlayerConfig {
	public Player player;
	public byte[] selection;	//Member selected of each BlockFamily
	
	public PlayerConfig(Player p){
		player = p;
		selection = new byte[ChiselPlugin.families.size()];
		for(int i=0; i<ChiselPlugin.families.size(); i++){
			selection[i] = 0;
		}
	}
}
