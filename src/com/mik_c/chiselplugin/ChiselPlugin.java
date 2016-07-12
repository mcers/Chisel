package com.mik_c.chiselplugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ChiselPlugin extends JavaPlugin{
	protected static List<BlockFamily> families;
	protected static Map<UUID, PlayerConfig> pconfig;
	protected static Map<String, Integer> commands;
	
	protected static String output_header;
	
	protected static List<String> output_help;
	protected static List<String> output_list_header;
	protected static List<String> output_list_toRepeat;
	protected static List<String> output_list_concrete_header;
	protected static List<String> output_list_concrete_toRepeat;
	protected static List<String> output_list_concrete_chosen;
	
	protected static List<String> output_whenChiselIsAssigned;
	protected static List<String> output_whenChiselCantBeAssigned;
	protected static List<String> output_whenChiselIsUnassigned;
	
	protected static List<String> output_whenModeChangesToFixed;
	protected static List<String> output_whenModeChangesToRotate;
	protected static List<String> output_whenBlockSelectionMade;
	
	protected static List<String> output_whenHasNoPermision;
	protected static List<String> output_whenFamilyDoesntExist;
	
	protected static List<String> output_whenConfigIsReloaded;
	protected static List<String> output_whenConfigDefault;
	
	private FileConfiguration cf = null;
	private File cfFile = null;
	
	public ChiselPlugin(){
		super();
		commands = new HashMap<String, Integer>();
		pconfig = new HashMap<UUID, PlayerConfig>();
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
		commands.clear();
		commands.put(config.getString("command.1").toLowerCase(), 1);
		commands.put(config.getString("command.2").toLowerCase(), 2);
		commands.put(config.getString("command.3").toLowerCase(), 3);
		commands.put(config.getString("command.4").toLowerCase(), 4);
		commands.put(config.getString("command.5").toLowerCase(), 5);
		commands.put(config.getString("command.6").toLowerCase(), 6);
		
		output_header = config.getString("output.header");
		
		output_help = config.getStringList("output.help");
		output_list_header = config.getStringList("output.list.header");
		output_list_toRepeat = config.getStringList("output.list.toRepeat");
		output_list_concrete_header = config.getStringList("output.list.concrete.header");
		output_list_concrete_toRepeat = config.getStringList("output.list.concrete.toRepeat");
		output_list_concrete_chosen = config.getStringList("output.list.concrete.chosen");
		
		output_whenChiselIsAssigned = config.getStringList("output.whenChiselIsAssigned");
		output_whenChiselCantBeAssigned = config.getStringList("output.whenChiselCantBeAssigned");
		output_whenChiselIsUnassigned = config.getStringList("output.whenChiselIsUnassigned");
		
		output_whenModeChangesToFixed = config.getStringList("output.whenModeChangesToFixed");
		output_whenModeChangesToRotate = config.getStringList("output.whenModeChangesToRotate");
		output_whenBlockSelectionMade = config.getStringList("output.whenBlockSelectionMade");
		
		output_whenHasNoPermision = config.getStringList("output.whenHasNoPermision");
		output_whenFamilyDoesntExist = config.getStringList("output.whenFamilyDoesntExist");
		
		output_whenConfigIsReloaded = config.getStringList("output.whenConfigIsReloaded");
		output_whenConfigDefault = config.getStringList("output.whenConfigIsDefault");
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
	
	public void replaceConfig() {
		cfFile.delete();
		try {
			firstRun();
		} catch (Exception e) {
			e.printStackTrace();
		}
		reloadConfig();
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
    }
    
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
    }
    
    public static String[] message(List<String> message, boolean header, String item, String family, String sel){
    	String[] m = new String[message.size()];
    	for(int i=0; i<message.size(); i++){
    		m[i] = message.get(i);
    		m[i] = m[i].replaceAll("%item", item);
    		m[i] = m[i].replaceAll("%family", family);
    		m[i] = m[i].replaceAll("%sel", sel);
    		if(i==0 && header){
    			m[i] = output_header + m[i];
    		}
    	}
    	return m;
    }
    
    @SuppressWarnings("deprecation")
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
    	if(command.getName().equalsIgnoreCase("chisel")){
    		int com;
    		if(args.length < 1){
    			com = 1;
    		}
    		else{
    			if(commands.containsKey(args[0].toLowerCase())){
    				com = commands.get(args[0].toLowerCase());
    			}else{
    				return false;	//Inexistent command
    			}
    		}
    		switch(com){
    			case 1:
    				if(sender instanceof Player){
    					if(sender.hasPermission("chisel.command.get")){
    						if(!pconfig.containsKey(((Player) sender).getUniqueId())){
    							pconfig.put(((Player) sender).getUniqueId(), new PlayerConfig());
    						}
    						if(pconfig.get(((Player) sender).getUniqueId()).setItem(((Player) sender).getItemInHand().getTypeId())){
    							sender.sendMessage(message(output_whenChiselIsAssigned, true, 
    									((Player) sender).getItemInHand().getType().name(), null, null));
    						}else{
    							sender.sendMessage(message(output_whenChiselCantBeAssigned, true,
    									((Player) sender).getItemInHand().getType().name(), null, null));
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
        						ChiselPlugin.pconfig.put(((Player) sender).getUniqueId(), new PlayerConfig());
        					}
        					sel = pconfig.get(((Player) sender).getUniqueId()).getSelection();
    					}else{
    						sel = new byte[families.size()];
    						for(int i=0; i<families.size(); i++){
    							sel[i] = 0;
    						}
    					}
    					
    					if(args.length == 1){
    						sender.sendMessage(message(output_list_header, true, null, null, null));
    						for(int i=0; i<families.size(); i++){
    							sender.sendMessage(message(output_list_toRepeat, false, null, 
            					families.get(i).familyname, families.get(i).lore[sel[i]]));
    						}
    					}else{
    						String arg2 = "";
    						for(int i=1; i<args.length; i++){
    							if(i != 1){
    								arg2 = arg2 + " ";
    							}
    							arg2 += args[i].toLowerCase();
    						}
    						System.out.println(arg2);
    						for(int i=0; i<families.size(); i++){
    							if(families.get(i).familyname.toLowerCase().equals(arg2)){
    								sender.sendMessage(message(output_list_concrete_header, true, 
    										null, families.get(i).familyname, families.get(i).lore[sel[i]]));
    								for(int j=0; j<families.get(i).type.length; j++){
    									if(j == sel[i]){
    										sender.sendMessage(message(output_list_concrete_chosen, false,
    												null, families.get(i).familyname, families.get(i).lore[j]));
    									}else{
    										sender.sendMessage(message(output_list_concrete_toRepeat, false,
    												null, families.get(i).familyname, families.get(i).lore[j]));
    									}
    								}
    								return true;
    							}
    						}
    						sender.sendMessage(message(output_whenFamilyDoesntExist, false,
									null, arg2, null));
    					}

            			return true;
    				}
    				break;
    			case 4:
    				if(sender.hasPermission("chisel.command.reset")){
    					if(sender instanceof Player){
    						if(pconfig.containsKey(((Player) sender).getUniqueId())){
        						ChiselPlugin.pconfig.get(((Player) sender).getUniqueId()).resetItem();
        						sender.sendMessage(message(output_whenChiselIsUnassigned, true, null, null, null));
        					}
    					}
    					return true;
    				}
    				break;
    			case 5:
    				if(sender.hasPermission("chisel.debug")){
    					reloadConfig();
    					sender.sendMessage(message(output_whenConfigIsReloaded, true, null, null, null));
    					return true;
    				}
    				break;
    			case 6:
    				if(sender.hasPermission("chisel.debug")){
    					replaceConfig();
    					sender.sendMessage(message(output_whenConfigDefault, true, null, null, null));
    					return true;
    				}
    				break;
    		}
    	}
    	if(sender instanceof Player){
    		sender.sendMessage(message(output_whenHasNoPermision, true, null, null, null));
    	}
    	return true;
    }
}
