package com.mik_c.chiselplugin;

import org.bukkit.entity.Player;

public class PlayerConfig {
	private Integer item;		//Item acting as the chisel
	private Integer mode;		//Mode selected
	public final static Integer modes = 2;	//Number of modes
	private byte[] selection;	//Member selected of each BlockFamily
	
	public PlayerConfig(){
		item = -1;
		mode = 0;
		selection = new byte[ChiselPlugin.families.size()];
		for(int i=0; i<ChiselPlugin.families.size(); i++){
			selection[i] = 0;
		}
	}
	
	public Integer getItem(){
		return item;
	}
	
	public boolean setItem(Integer i){
		if(i <= 255){
			return false;
		}
		item = i;
		return true;
	}
	
	public void resetItem(){
		item = -1;
	}
	
	public Integer getMode(){
		return mode;
	}
	
	public void rotateMode(){
		mode = (mode+1)%modes;
	}
	
	public byte[] getSelection(){
		return selection;
	}
	
	public void setSelection(int family, byte sel){
		if(family >= 0 && family < ChiselPlugin.families.size() && sel >= 0 &&  sel < ChiselPlugin.families.get(family).type.length){
			selection[family] = sel;
		}
	}
}
