package com.mik_c.chiselplugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
	protected static Map<UUID, PlayerConfig> pconfig;
	
	protected static Map<String, Integer> commands;
	
	protected static String output_header;
	protected static List<String> output_whenChiselIsAssigned;
	protected static List<String> output_whenChiselCantBeAssigned;
	protected static List<String> output_help;
	protected static List<String> output_list_header;
	protected static List<String> output_list_toRepeat;
	protected static List<String> output_whenChiselIsUnassigned;
	protected static List<String> output_whenConfigIsRefreshed;
	protected static List<String> output_whenHasNoPermision;
	protected static List<String> output_whenModeChangesToFixed;
	protected static List<String> output_whenModeChangesToRotate;
	
	private FileConfiguration cf = null;
	private File cfFile = null;
	
	public ChiselPlugin(){
		super();
		chisel = ChiselItems.chiselItem();
		chisel2 = ChiselItems.chiselItem2();
		pconfig = new Map<>();
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
	
	public void refreshOutput(){
		FileConfiguration config = getConfig();
		
		output_header = config.getString("output.header");
		output_whenChiselIsAssigned = config.getStringList("output.whenChiselIsAssigned");
		output_whenChiselCantBeAssigned = config.getStringList("output.whenChiselCantBeAssigned");
		output_help = config.getStringList("output.help");
		output_list_header = config.getStringList("output.list.header");
		output_list_toRepeat = config.getStringList("output.list.toRepeat");
		output_whenChiselIsUnassigned = config.getStringList("output.whenChiselIsUnassigned");
		output_whenChiselIsRefreshed = config.getStringList("output.whenConfigIsRefreshed");
		output_whenHasNoPermision = config.getStringList("output.whenHasNoPermision");
		output_whenModeChangesToFixed = config.getStringList("output.whenModeChangesToFixed");
		output_whenModeChangesToRotate = config.getStringList("output.whenModeChangesToRotate");
	}
    
	//Config de FamilyBlocks
	public void reloadConfig() {
		try {
			cf.load(cfFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		refreshFamilies();
		refreshOutput();
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
    
    public static String[] message(List<String> message, boolean header, String item, String family, String sel){
    	String[] m = new String[message.size()];
    	for(int i=0; i<message.size(); i++){
    		m[i] = message.get(i).replaceAll("%item", item);
    		m[i] = m[i].replaceAll("%family", family);
    		m[i] = m[i].replaceAll("%sel", sel);
    		if(i==0 && header){
    			m[i] = output_header + m[i];
    		}
    	}
    	return m;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
    	if(command.getName().equalsIgnoreCase("chisel")){
    		int com;
    		if(args.length < 1){
    			com = 1;
    		}
    		else{
    			com = getConfig().getInt("command."+args[0]);
    		}
    		switch(com){
    			case 1:
    				if(sender instanceof Player){
    					if(sender.hasPermission("chisel.command.get")){
    						if(!pconfig.containsKey(((Player) sender).getUniqueId())){
    							pconfig.put(((Player) sender).getUniqueId(), new PlayerConfig());
    						}
    						if(pconfig.get(((Player) sender).getUniqueId()).setItem(((Player) sender).getItemInHand())){
    							sender.sendMessage(message(output_whenChiselIsAssigned, true, 
    									((Player) sender).getItemInHand().getItemMeta().getDisplayName(), null, null));
    						}else{
    							sender.sendMessage(message(output_whenChiselCantBeAssigned, true,
    									((Player) sender).getItemInHand().getItemMeta().getDisplayName(), null, null));
    						}
    						return true;
    					}
                    }
    				break;
    			
    			case 2:
    				if(sender.hasPermission("chisel.command.help")){
    					sender.sendMessage(message(output_help, true, null, null, null));
    					return true;
    				}
    				break;
    			case 3:
    				if(sender.hasPermission("chisel.command.list")){
    					byte[] sel;
    					if(sender instanceof Player){
    						if(!pconfig.containsKey(((Player) sender).getUniqueId())){
        						this.pconfig.put(((Player) sender).getUniqueId(), new PlayerConfig());
        					}
        					sel = pconfig.get(((Player) sender).getUniqueId()).selection;
    					}else{
    						sel = new byte[families.size()];
    						for(int i=0; i<families.size(); i++){
    							sel[i] = 0;
    						}
    					}
    					sender.sendMessage(message(output_list_header, true, null, null, null));
            			sender.sendMessage(message(output_list_toRepeat, false, null, null, null)); 	
            			return true;
    				}
    				break;
    			case 4:
    				if(sender.hasPermission("chisel.command.reset")){
    					if(sender instanceof Player){
    						if(pconfig.containsKey(((Player) sender).getUniqueId())){
        						this.pconfig.get(((Player) sender).getUniqueId()).resetItem();
        						sender.sendMessage(message(output_whenChiselIsUnassigned, true, null, null, null));
        					}
    					}
    					return true;
    				}
    				break;
    			case 5:
    				if(sender.hasPermission("chisel.debug")){
    					reloadConfig();
    					sender.sendMessage(message(output_whenConfigIsRefreshed, true, null, null, null));
    					return true;
    				}
    				break;
    			default:	//Inexistent command
    				return false;
    		}
    	}
    	if(sender instanceof Player){
    		sender.sendMessage(message(output_whenHasNoPermision, true, null, null, null));
    	}
    	return true;
    }
}
