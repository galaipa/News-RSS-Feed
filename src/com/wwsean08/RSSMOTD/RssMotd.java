package com.wwsean08.RSSMOTD;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RssMotd extends JavaPlugin{
	RssMotdPlayerListener pl;
	FileConfiguration config = null;
	Server server = null;
	PluginManager pm = null;
        public static String structure = null;
	private RssMotdParserRunnable runnable;
	@Override
	public void onDisable() {
		//goodbye
	}

	@Override
	public void onEnable() {
		//hello
		server = Bukkit.getServer();
		pm = server.getPluginManager();
		initConfig();
		pl = new RssMotdPlayerListener(config);
		pm.registerEvents(pl, this);
		parseRSS();
		server.getLogger().info("[NEWS] RSS reader running");
                broadcastRSS();
                structure = getConfig().getString("Structure");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(commandLabel.equalsIgnoreCase("news")){
			if(args.length == 0){
				ArrayList<String> list = RssMotdParserRunnable.titles;
				sender.sendMessage((ChatColor.translateAlternateColorCodes('&', (config.getString("info")))));
				for(String s : list){
					sender.sendMessage((ChatColor.translateAlternateColorCodes('&', (config.getString("prefix"))))+ " " + s);
				}
			}else if(args[0].equalsIgnoreCase("update")){
				if(sender instanceof Player){
					Player player = (Player)sender;
					if(player.hasPermission("news.update")){
						runnable.run();
                                                sender.sendMessage("News updated");
					}else{
						player.sendMessage("You do not have permission to do that");
					}
				}else if(sender instanceof ConsoleCommandSender){
					runnable.run();
				}
			}
		}
		return true;
	}

	/**
	 * initializes the config file writing default values if none exist
	 */
	private void initConfig(){
		config = this.getConfig();
		config.options().copyDefaults(true);
		this.saveConfig();
	}

	/**
	 * parses the RSS feed and saves the results to be displayed to the user
	 */

	private void parseRSS(){
		//roughly 1200 ticks per minute
		int RefreshTime = config.getInt("RefreshTime")*1200;
                runnable = new RssMotdParserRunnable(config.getString("Feed"), config.getInt("Posts"));
                Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, runnable , 200, RefreshTime);
	}
	/**
	 * broadcasts the RSS feed every x minutes 
	 */
  	private void broadcastRSS(){
             if(config.getBoolean("Broadcast")){
                int BroadcastTime = config.getInt("BroadcastTime")*1200;
                Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                        public void run() {
                            Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', (config.getString("info"))));
                            ArrayList<String> list = RssMotdParserRunnable.titles;
                                    for(String s : list){
                                            Bukkit.getServer().broadcastMessage((ChatColor.translateAlternateColorCodes('&', (config.getString("prefix")))) + " " + s);
                                    }
                           
                        }
                }, 200, BroadcastTime);
             }
}
   
        


}
