package org.r7l.rarityx.rarity;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.r7l.rarityx.RarityX;

import java.util.*;

/**
 * Manages rarity assignment and item manipulation
 * 
 * @author R7LRod
 */
public class RarityManager {
    
    private final RarityX plugin;
    private final NamespacedKey rarityKey;
    private final Random random;
    
    // Materials that should have higher chance for better rarities
    private final Set<Material> valuableMaterials;
    
    public RarityManager(RarityX plugin) {
        this.plugin = plugin;
        this.rarityKey = new NamespacedKey(plugin, "rarity");
        this.random = new Random();
        
        // Initialize valuable materials
        this.valuableMaterials = new HashSet<>();
        initializeValuableMaterials();
    }
    
    /**
     * Initialize materials that should have higher rarity chances
     */
    private void initializeValuableMaterials() {
        // Diamond tools and armor
        valuableMaterials.addAll(Arrays.asList(
            Material.DIAMOND_SWORD, Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE,
            Material.DIAMOND_SHOVEL, Material.DIAMOND_HOE, Material.DIAMOND_HELMET,
            Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS
        ));
        
        // Netherite tools and armor
        valuableMaterials.addAll(Arrays.asList(
            Material.NETHERITE_SWORD, Material.NETHERITE_PICKAXE, Material.NETHERITE_AXE,
            Material.NETHERITE_SHOVEL, Material.NETHERITE_HOE, Material.NETHERITE_HELMET,
            Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS
        ));
        
        // Special items
        valuableMaterials.addAll(Arrays.asList(
            Material.ELYTRA, Material.TRIDENT, Material.CROSSBOW, Material.BOW,
            Material.SHIELD, Material.TOTEM_OF_UNDYING
        ));
    }
    
    /**
     * Assign a random rarity to an item
     * @param item The item to assign rarity to
     * @return The modified item with rarity
     */
    public ItemStack assignRandomRarity(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return item;
        }
        
        Rarity rarity = generateRandomRarity(item.getType());
        return setRarity(item, rarity);
    }
    
    /**
     * Set a specific rarity to an item
     * @param item The item to modify
     * @param rarity The rarity to assign
     * @return The modified item
     */
    public ItemStack setRarity(ItemStack item, Rarity rarity) {
        if (item == null || item.getType() == Material.AIR || rarity == null) {
            return item;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        
        // Store rarity in persistent data
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        dataContainer.set(rarityKey, PersistentDataType.STRING, rarity.name());
        
        // Update lore
        updateItemLore(meta, rarity);
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Get the rarity of an item
     * @param item The item to check
     * @return The rarity of the item, or null if no rarity assigned
     */
    public Rarity getRarity(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }
        
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        String rarityString = dataContainer.get(rarityKey, PersistentDataType.STRING);
        
        if (rarityString == null) {
            return null;
        }
        
        return Rarity.fromString(rarityString);
    }
    
    /**
     * Check if an item has a rarity assigned
     * @param item The item to check
     * @return True if the item has a rarity
     */
    public boolean hasRarity(ItemStack item) {
        return getRarity(item) != null;
    }
    
    /**
     * Remove rarity from an item
     * @param item The item to modify
     * @return The modified item
     */
    public ItemStack removeRarity(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return item;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        
        // Remove rarity from persistent data
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        dataContainer.remove(rarityKey);
        
        // Remove rarity lore
        List<String> lore = meta.getLore();
        if (lore != null) {
            lore.removeIf(line -> line.contains("Rarity:"));
            meta.setLore(lore);
        }
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Generate a random rarity based on material type and weights
     * @param material The material type
     * @return A random rarity
     */
    private Rarity generateRandomRarity(Material material) {
        double totalWeight = Rarity.getTotalWeight();
        
        // Adjust weights for valuable materials
        Map<Rarity, Double> adjustedWeights = new HashMap<>();
        
        for (Rarity rarity : Rarity.values()) {
            double weight = rarity.getWeight();
            
            // Increase chances for better rarities on valuable materials
            if (valuableMaterials.contains(material)) {
                switch (rarity) {
                    case MYTHIC:
                        weight *= 3.0; // 3x chance for mythic
                        break;
                    case EPIC:
                        weight *= 2.5; // 2.5x chance for epic
                        break;
                    case RARE:
                        weight *= 2.0; // 2x chance for rare
                        break;
                    case UNCOMMON:
                        weight *= 1.5; // 1.5x chance for uncommon
                        break;
                    case COMMON:
                        weight *= 0.5; // Half chance for common
                        break;
                }
            }
            
            adjustedWeights.put(rarity, weight);
        }
        
        // Calculate new total weight
        double adjustedTotalWeight = adjustedWeights.values().stream().mapToDouble(Double::doubleValue).sum();
        
        double randomValue = random.nextDouble() * adjustedTotalWeight;
        double currentWeight = 0;
        
        for (Rarity rarity : Rarity.values()) {
            currentWeight += adjustedWeights.get(rarity);
            if (randomValue <= currentWeight) {
                return rarity;
            }
        }
        
        return Rarity.COMMON; // Fallback
    }
    
    /**
     * Update the lore of an item with rarity information
     * @param meta The item meta to update
     * @param rarity The rarity to add
     */
    private void updateItemLore(ItemMeta meta, Rarity rarity) {
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        
        // Remove existing rarity lore
        lore.removeIf(line -> line.contains("Rarity:"));
        
        // Add new rarity lore at the end
        lore.add("");
        lore.add("§7Rarity: " + rarity.getFormattedName());
        
        meta.setLore(lore);
    }
    
    /**
     * Get the NamespacedKey used for rarity data
     * @return The rarity key
     */
    public NamespacedKey getRarityKey() {
        return rarityKey;
    }
}