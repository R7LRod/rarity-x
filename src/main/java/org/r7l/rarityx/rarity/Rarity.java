package org.r7l.rarityx.rarity;

import org.bukkit.ChatColor;

/**
 * Enum representing different rarity levels
 * 
 * @author R7LRod
 */
public enum Rarity {
    COMMON("Common", ChatColor.WHITE, 60.0, 1.0),
    UNCOMMON("Uncommon", ChatColor.GREEN, 25.0, 1.1),
    RARE("Rare", ChatColor.BLUE, 10.0, 1.25),
    EPIC("Epic", ChatColor.DARK_PURPLE, 4.0, 1.5),
    MYTHIC("Mythic", ChatColor.GOLD, 1.0, 2.0);
    
    private final String displayName;
    private final ChatColor color;
    private final double weight; // Probability weight for random assignment
    private final double multiplier; // Buff multiplier
    
    /**
     * Constructor for Rarity enum
     * 
     * @param displayName The display name of the rarity
     * @param color The color associated with the rarity
     * @param weight The probability weight for random assignment
     * @param multiplier The buff multiplier applied to items
     */
    Rarity(String displayName, ChatColor color, double weight, double multiplier) {
        this.displayName = displayName;
        this.color = color;
        this.weight = weight;
        this.multiplier = multiplier;
    }
    
    /**
     * Get the display name of the rarity
     * @return Display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Get the color of the rarity
     * @return ChatColor
     */
    public ChatColor getColor() {
        return color;
    }
    
    /**
     * Get the probability weight of the rarity
     * @return Weight value
     */
    public double getWeight() {
        return weight;
    }
    
    /**
     * Get the multiplier for this rarity
     * @return Multiplier value
     */
    public double getMultiplier() {
        return multiplier;
    }
    
    /**
     * Get the formatted display text with color
     * @return Colored rarity text
     */
    public String getFormattedName() {
        return color + displayName + ChatColor.RESET;
    }
    
    /**
     * Get rarity from string name (case-insensitive)
     * @param name The rarity name
     * @return Rarity enum or null if not found
     */
    public static Rarity fromString(String name) {
        for (Rarity rarity : values()) {
            if (rarity.name().equalsIgnoreCase(name) || rarity.displayName.equalsIgnoreCase(name)) {
                return rarity;
            }
        }
        return null;
    }
    
    /**
     * Get total weight of all rarities
     * @return Total weight
     */
    public static double getTotalWeight() {
        double total = 0;
        for (Rarity rarity : values()) {
            total += rarity.weight;
        }
        return total;
    }
}