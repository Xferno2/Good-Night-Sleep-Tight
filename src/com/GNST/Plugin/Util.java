package com.GNST.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.plugin.java.JavaPlugin;

public class Util extends JavaPlugin{
	private FileConfiguration config = null;
	private File configFile = null;
	
	public void reloadConfig() {
		if(configFile == null) {
			configFile = new File(getDataFolder(), "config.yml");
		}
		config = YamlConfiguration.loadConfiguration(configFile);
		
		Reader defConfigStream = new InputStreamReader(this.getResource("customConfig.yml"));
		if(defConfigStream !=null){
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
	}
	
	public FileConfiguration CustomConfig() {
		if(config == null) {
			reloadConfig();
		}
		return config;
	}
	
	public void saveConfig() {
		if(config == null || configFile == null) {
			return;
		}
		try {
			this.getConfig().save(configFile);
		}catch (IOException ex) {
			getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
		}
	}
}
