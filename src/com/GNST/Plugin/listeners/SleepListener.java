package com.GNST.Plugin.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import com.GNST.Plugin.Main;

public class SleepListener implements Listener{
	
	// Variables.
	public Main plugin;
	public Player player;
	public World world;
	public Server server;
	
	// Task id to cancel the sleeping task when leaving the bed.
	public int taskId;
	
	public boolean Percentage;
	public int Count;
	
	public List<String> Broadcast;
	public String WakeUp;
	
	public int Sleeping;
	public float PercentageVal;
	
	
	// Constructor.
	public SleepListener(Main _plugin)
	{	
		plugin = _plugin;
		Percentage = plugin.getConfig().getBoolean("Sleep-Options.usePercentage");
		Count = plugin.getConfig().getInt("Sleep-Options.Count-Percentage");
		
		Broadcast = plugin.getConfig().getStringList("Message-Options.Sleep-Broadcast");
		WakeUp = plugin.getConfig().getString("Message-Options.Player-Wakeup-Message");
	}
	
	// Player enter bed event.
	@EventHandler
	public void onSleep(PlayerBedEnterEvent ev) {
		// Set the player and world variables.
		player = ev.getPlayer();
		world = player.getWorld();
		server = player.getServer();
		
		Sleeping++;
		PercentageVal = (Sleeping * 100.00f) / server.getOnlinePlayers().size();	
		
		// Broadcast message displaying for all the players.
		if(!Percentage) {
		for (String s : Broadcast) {
			player.getServer().broadcastMessage(Parse(s,Sleeping));
		}
		} else{
			for (String s : Broadcast) {
				player.getServer().broadcastMessage(Parse(s,Sleeping));
			}
		}
		
		// The sleeping task.
		taskId = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			// The task.
	            public void run() {	
	        	Runnable wakeUpFunc = new Runnable() {
	        			public void run() {
	        				// Set the time to morning, when mobs start burning. Also disable thunder if exist but don't disable rain if ther's no thunder.
	        				world.setTime(23450);
	        				if(world.isThundering()){	
	        					world.setThundering(false);
	        					world.setStorm(false);
	        				} else if (!world.isThundering() && world.hasStorm()){
	        				}
	    	        		// Store the current health, deal 1 damage then restore the current health to wake the player up.
	    	        		double currentDamage = player.getHealth();
	    	        		player.damage(0.01);
	    	        		player.setHealth(currentDamage);
		        			player.sendMessage(Parse(WakeUp));
	        			}
	        		};
	        		
	        		// Message to the player.
	        		if(!Percentage)
	        		{
	        			if(Sleeping >= Count)
	        			{
	        				wakeUpFunc.run();
	        			}
	        		}else {
	        			if(PercentageVal >= Count){
	        				wakeUpFunc.run();
	        			}
	        		}
	        		
	        		// Event canceler just in case.
	        		ev.setCancelled(true);
	            }
	            //Delay of 4 ticks.
			},4 * 20L);
	}
	
	// Player leave bed event.
	@EventHandler
	public void onNoSleep(PlayerBedLeaveEvent ev)
	{
		Sleeping--;
		// Cancel the onSleep task if the player leaves the bed.
		Bukkit.getServer().getScheduler().cancelTask(taskId);
	}
	
	public String Parse(String _str, float Sleep) {
		String str = ChatColor.translateAlternateColorCodes('&', _str);
		return str.replace("{PLAYER}", player.getDisplayName()).replace("{COUNTREQ}", String.valueOf(Sleep)).replace("{COUNTMAX}", String.valueOf(Count) );
	}
	
	public String Parse(String _str, int Sleep) {
		String str = ChatColor.translateAlternateColorCodes('&', _str);
		return str.replace("{PLAYER}", player.getDisplayName()).replace("{COUNTREQ}", String.valueOf(Sleep)).replace("{COUNTMAX}", String.valueOf(Count) );
	}
	
	public String Parse(String _str) {
		String str = ChatColor.translateAlternateColorCodes('&', _str);
		return str.replace("{PLAYER}", player.getDisplayName());
	}
}
