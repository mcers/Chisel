package com.mik_c.chiselplugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ChiselItems {
	//Chisel items
	public static ItemStack chiselItem(){
		ItemStack chisel = new ItemStack(Material.STICK);
		ItemMeta m = chisel.getItemMeta();
		m.setDisplayName(ChatColor.DARK_BLUE+""+ChatColor.BOLD+"Chisel");
		
		List<String> lore = new ArrayList<String>(); 
	    lore.add(ChatColor.GRAY + "Modo rotativo."); 
	    m.setLore(lore);
	    m.addEnchant(Enchantment.LURE, 1, false);
	    m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	    chisel.setItemMeta(m);
		return chisel;
	}
	
	public static ItemStack chiselItem2(){
		ItemStack chisel = new ItemStack(Material.STICK);
		ItemMeta m = chisel.getItemMeta();
		m.setDisplayName(ChatColor.DARK_BLUE+""+ChatColor.BOLD+"Chisel");
		
		List<String> lore = new ArrayList<String>(); 
	    lore.add(ChatColor.GRAY + "Modo fijo."); 
	    m.setLore(lore);
	    m.addEnchant(Enchantment.LURE, 1, false);
	    m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	    chisel.setItemMeta(m);
		return chisel;
	}
}
