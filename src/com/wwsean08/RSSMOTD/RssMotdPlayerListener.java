package com.wwsean08.RSSMOTD;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class RssMotdPlayerListener implements Listener{
	FileConfiguration config;
	public RssMotdPlayerListener(FileConfiguration file){
		config = file;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		ArrayList<String> titles = RssMotdParserRunnable.titles;
		if(!player.hasPermission("news.ignore")){
			if(config.getBoolean("infoOnJoin")){
				player.sendMessage((ChatColor.translateAlternateColorCodes('&', (config.getString("info")))));
			}
                        if(config.getBoolean("newsOnJoin")){
			for(String s : titles){
				player.sendMessage((ChatColor.translateAlternateColorCodes('&', (config.getString("prefix"))))+ " " + s);
			}
                    }
		}
	}
}