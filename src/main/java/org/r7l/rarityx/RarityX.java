package org.r7l.rarityx;

import org.bukkit.plugin.java.JavaPlugin;
import org.r7l.rarityx.commands.RarityXCommand;
import org.r7l.rarityx.config.ConfigManager;
import org.r7l.rarityx.listeners.ItemListener;
import org.r7l.rarityx.listeners.PlayerListener;
import org.r7l.rarityx.rarity.RarityManager;

import java.util.logging.Level;

/**
 * Main plugin class for RarityX
 * 
 * @author R7LRod
 * @version 1.0.0
 */
public final class RarityX extends JavaPlugin {
    
    private static RarityX instance;
    
    private ConfigManager configManager;
    private RarityManager rarityManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        getLogger().log(Level.INFO, "Initializing RarityX v" + getDescription().getVersion());
        
        // Initialize configuration
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        
        // Initialize rarity manager
        rarityManager = new RarityManager(this);
        
        // Register commands
        getCommand("rarityx").setExecutor(new RarityXCommand(this));
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new ItemListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        
        getLogger().log(Level.INFO, "RarityX v" + getDescription().getVersion() + " has been enabled!");
    }
    
    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "RarityX v" + getDescription().getVersion() + " has been disabled!");
    }
    
    /**
     * Get the plugin instance
     * @return Plugin instance
     */
    public static RarityX getInstance() {
        return instance;
    }
    
    /**
     * Get the configuration manager
     * @return ConfigManager instance
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    /**
     * Get the rarity manager
     * @return RarityManager instance
     */
    public RarityManager getRarityManager() {
        return rarityManager;
    }
}