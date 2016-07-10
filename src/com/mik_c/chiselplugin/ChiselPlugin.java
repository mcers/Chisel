package com.mik_c.chiselplugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ChiselPlugin extends JavaPlugin{
	protected static List<BlockFamily> families;
	protected static ItemStack chisel;
	protected static ItemStack chisel2;
	protected static List<PlayerConfig> pconfig;
	
	private FileConfiguration cf = null;
	private File cfFile = null;
	
	public ChiselPlugin(){
		super();
		chisel = ChiselItems.chiselItem();
		chisel2 = ChiselItems.chiselItem2();
		pconfig = new ArrayList<PlayerConfig>();
	}
	
	public void refreshFamilies(){
    	FileConfiguration config = getConfig();
    	List<BlockFamily> fam = new ArrayList<BlockFamily>();
    	
    	for(int i=1; i<=config.getInt("familyblock.numfamilies"); i++){
    		List<Integer> type = config.getIntegerList("familyblock."+i+".type");
    		List<Integer> data = config.getIntegerList("familyblock."+i+".data");
    		List<String> lore = config.getStringList("familyblock."+i+".lore");
    		fam.add(new BlockFamily(config.getString("familyblock."+i+".familyname"),type,data,lore));
    	}		
    	families = fam;
    	pconfig.clear();
    }
	
	public static int isPlayerConfig(Player p){
		for(int i=0; i<pconfig.size(); i++){
			if(pconfig.get(i).player.equals(p)){
				return i;
			}
		}
		return -1;
	}
    
	//Config de FamilyBlocks
	public void reloadConfig() {
		try {
			cf.load(cfFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		refreshFamilies();
	}
	
	private void firstRun() throws Exception {
		if(!cfFile.exists()){
			cfFile.getParentFile().mkdirs();
			copy(getResource("config.yml"), cfFile);
		}
	}
	    
	private void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	        	out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public FileConfiguration getConfig(){
		if(cf == null){
			reloadConfig();
		}
		return cf;
	}
	
	// Fired when plugin is first enabled
    @Override
    public void onEnable() {
    	getServer().getPluginManager().registerEvents(new ChiselListener(), this);
    	//getServer().addRecipe(ChiselItems.chiselCraftRecipe(getServer())); //Recipe will be removed

    	cfFile = new File(getDataFolder(), "config.yml");
    	
    	try {
            firstRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
    	cf = new YamlConfiguration();
    	reloadConfig();
    	/*try {
			cf.save(cfFile);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
    }
    
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
    	try {
			cf.save(cfFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
    	if(command.getName().equalsIgnoreCase("chisel")){
    		int com;
    		if(args.length < 1){
    			com = 0;
    		}
    		else{
    			com = getConfig().getInt("command."+args[0]);
    		}
    		switch(com){
    			case 1:
    				if(sender instanceof Player){
    					if(sender.hasPermission("chisel.get")){
    						((Player) sender).getInventory().addItem(chisel);
    						return true;
    					}
                    }
    			case 2:
    				byte[] sel;
        			if(sender instanceof Player){
        				if(sender.hasPermission("chisel.show")){
        					int nconfig = ChiselPlugin.isPlayerConfig((Player) sender);
        					if(nconfig < 0){
        						ChiselPlugin.pconfig.add(new PlayerConfig((Player) sender));
        						nconfig = ChiselPlugin.isPlayerConfig((Player) sender);
        					}
        					sel = pconfig.get(nconfig).selection;
        				}
        				else{
        					return false;
        				}
                    }
        			else{
        				sel = new byte[families.size()];
        				for(int i=0; i<families.size(); i++){
        					sel[i] = 0;
        				}
        			}
        			
        			sender.sendMessage(ChatColor.GOLD+" "+ChatColor.BOLD+"[Chisel]: ");
        			sender.sendMessage(ChatColor.GOLD+"  [Familia]    ->   [Bloque]");
        			for(int i=0; i<families.size(); i++){
        				sender.sendMessage(ChatColor.WHITE+" "+String.format("%-16s %s ", families.get(i).familyname, families.get(i).lore[sel[i]]));
        			}  	
        			return true;
    			case 3:
    				if(sender.hasPermission("chisel.reload")){
    					reloadConfig();
    					sender.sendMessage(ChatColor.GOLD+" "+ChatColor.BOLD+"[Chisel]: "+ChatColor.WHITE+"Grupos de bloques reestablecidos.");
    					return true;
    				}
    				else 
    					return false;
    			default:
    				sender.sendMessage(ChatColor.GOLD+" "+ChatColor.BOLD+"[Chisel]: "+ChatColor.WHITE+"'/chisel get', '/chisel show' or '/chisel refresh'");
    				return true;
    		}
    	}
    	return false;
    }
}
