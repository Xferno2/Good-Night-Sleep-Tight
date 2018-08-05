package com.GNST.Plugin;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.GNST.Plugin.listeners.SleepListener;

public class Main extends JavaPlugin {
	
	// On plugin enable.
	@Override
	public void onEnable() {
		
		// Save a copy of the default config.yml if one is not there.
		this.saveDefaultConfig();
		
		
		// Create a plugin manager.
		PluginManager pm = getServer().getPluginManager();
		
		// Create a new instance of SleepListener class and register the Listener Events in that class.
		SleepListener sleep = new SleepListener(this);
		pm.registerEvents(sleep,this);
	}
	
	
}
