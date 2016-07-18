package com.mik_c.chiselplugin;

import java.util.List;

public class BlockFamily {
	public String familyname;
	public int[] type;
	public byte[] data;
	public String[] lore;
	
	public BlockFamily(String n, List<Integer> t, List<Integer> d, List<String> l){
		familyname = n;
		
		type = new int[t.size()];
		for(int i=0; i<t.size(); i++){
			type[i] = (int)t.get(i);
		}
		data = new byte[d.size()];
		for(int i=0; i<d.size(); i++){
			data[i] = (byte)(int)d.get(i);
		}
		lore = new String[l.size()];
		for(int i=0; i<l.size(); i++){
			lore[i] = l.get(i);
		}
	}
	
	public int isThisFamily(int t, byte d){
		for(int i = 0; i < type.length; i++){
			if(type[i] == t && data[i] == d){
				return i;
			}
		}
		return -1;
	}
}
