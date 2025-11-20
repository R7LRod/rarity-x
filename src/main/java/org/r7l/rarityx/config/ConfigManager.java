package org.r7l.rarityx.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.r7l.rarityx.RarityX;

/**
 * Manages plugin configuration
 * 
 * @author R7LRod
 */
public class ConfigManager {
    
    private final RarityX plugin;
    private FileConfiguration config;
    
    public ConfigManager(RarityX plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Load and initialize configuration
     */
    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
        
        // Set default values if they don't exist
        setDefaults();
    }
    
    /**
     * Reload configuration from file
     */
    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }
    
    /**
     * Save configuration to file
     */
    public void saveConfig() {
        plugin.saveConfig();
    }
    
    /**
     * Set default configuration values
     */
    private void setDefaults() {
        // General settings
        config.addDefault("general.assign-rarity-on-craft", true);
        config.addDefault("general.assign-rarity-on-join", false);
        config.addDefault("general.assign-rarity-on-drop", false);
        config.addDefault("general.prevent-different-rarity-stacking", true);
        
        // Rarity weights
        config.addDefault("rarity-weights.common", 60.0);
        config.addDefault("rarity-weights.uncommon", 25.0);
        config.addDefault("rarity-weights.rare", 10.0);
        config.addDefault("rarity-weights.epic", 4.0);
        config.addDefault("rarity-weights.mythic", 1.0);
        
        // Multipliers
        config.addDefault("rarity-multipliers.common", 1.0);
        config.addDefault("rarity-multipliers.uncommon", 1.1);
        config.addDefault("rarity-multipliers.rare", 1.25);
        config.addDefault("rarity-multipliers.epic", 1.5);
        config.addDefault("rarity-multipliers.mythic", 2.0);
        
        // Buff settings
        config.addDefault("buffs.enable-attribute-modifiers", true);
        config.addDefault("buffs.enable-enchantment-bonuses", true);
        config.addDefault("buffs.enable-special-effects", true);
        
        // Valuable materials (higher rarity chance)
        config.addDefault("valuable-materials.enabled", true);
        config.addDefault("valuable-materials.diamond-multiplier", 2.0);
        config.addDefault("valuable-materials.netherite-multiplier", 3.0);
        
        // Messages
        config.addDefault("messages.prefix", "§8[§6RarityX§8]§r ");
        config.addDefault("messages.no-permission", "§cYou don't have permission to use this command!");
        config.addDefault("messages.config-reloaded", "§aConfiguration reloaded successfully!");
        config.addDefault("messages.player-not-found", "§cPlayer not found!");
        config.addDefault("messages.invalid-rarity", "§cInvalid rarity! Valid rarities: Common, Uncommon, Rare, Epic, Mythic");
        config.addDefault("messages.rarity-set", "§aRarity set to §r{rarity}§a for item in hand!");
        config.addDefault("messages.no-item-in-hand", "§cYou must be holding an item!");
        config.addDefault("messages.item-info", "§7Item: §f{item} §7| Rarity: {rarity}");
        
        config.options().copyDefaults(true);
        saveConfig();
    }
    
    // General settings getters
    public boolean isAssignRarityOnCraft() {
        return config.getBoolean("general.assign-rarity-on-craft", true);
    }
    
    public boolean isAssignRarityOnJoin() {
        return config.getBoolean("general.assign-rarity-on-join", false);
    }
    
    public boolean isAssignRarityOnDrop() {
        return config.getBoolean("general.assign-rarity-on-drop", false);
    }
    
    public boolean isPreventDifferentRarityStacking() {
        return config.getBoolean("general.prevent-different-rarity-stacking", true);
    }
    
    // Rarity weights getters
    public double getCommonWeight() {
        return config.getDouble("rarity-weights.common", 60.0);
    }
    
    public double getUncommonWeight() {
        return config.getDouble("rarity-weights.uncommon", 25.0);
    }
    
    public double getRareWeight() {
        return config.getDouble("rarity-weights.rare", 10.0);
    }
    
    public double getEpicWeight() {
        return config.getDouble("rarity-weights.epic", 4.0);
    }
    
    public double getMythicWeight() {
        return config.getDouble("rarity-weights.mythic", 1.0);
    }
    
    // Multipliers getters
    public double getCommonMultiplier() {
        return config.getDouble("rarity-multipliers.common", 1.0);
    }
    
    public double getUncommonMultiplier() {
        return config.getDouble("rarity-multipliers.uncommon", 1.1);
    }
    
    public double getRareMultiplier() {
        return config.getDouble("rarity-multipliers.rare", 1.25);
    }
    
    public double getEpicMultiplier() {
        return config.getDouble("rarity-multipliers.epic", 1.5);
    }
    
    public double getMythicMultiplier() {
        return config.getDouble("rarity-multipliers.mythic", 2.0);
    }
    
    // Buff settings getters
    public boolean isEnableAttributeModifiers() {
        return config.getBoolean("buffs.enable-attribute-modifiers", true);
    }
    
    public boolean isEnableEnchantmentBonuses() {
        return config.getBoolean("buffs.enable-enchantment-bonuses", true);
    }
    
    public boolean isEnableSpecialEffects() {
        return config.getBoolean("buffs.enable-special-effects", true);
    }
    
    // Valuable materials getters
    public boolean isValuableMaterialsEnabled() {
        return config.getBoolean("valuable-materials.enabled", true);
    }
    
    public double getDiamondMultiplier() {
        return config.getDouble("valuable-materials.diamond-multiplier", 2.0);
    }
    
    public double getNetheriteMultiplier() {
        return config.getDouble("valuable-materials.netherite-multiplier", 3.0);
    }
    
    // Messages getters
    public String getPrefix() {
        return config.getString("messages.prefix", "§8[§6RarityX§8]§r ");
    }
    
    public String getMessage(String key) {
        return config.getString("messages." + key, "§cMessage not found: " + key);
    }
    
    public String getFormattedMessage(String key, String... replacements) {
        String message = getPrefix() + getMessage(key);
        
        // Replace placeholders
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace("{" + replacements[i] + "}", replacements[i + 1]);
            }
        }
        
        return message;
    }
}