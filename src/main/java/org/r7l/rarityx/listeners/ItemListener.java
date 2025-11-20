package org.r7l.rarityx.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.r7l.rarityx.RarityX;
import org.r7l.rarityx.buffs.BuffManager;
import org.r7l.rarityx.rarity.Rarity;
import org.r7l.rarityx.rarity.RarityManager;

/**
 * Handles item-related events for rarity system
 * 
 * @author R7LRod
 */
public class ItemListener implements Listener {
    
    private final RarityX plugin;
    private final RarityManager rarityManager;
    
    public ItemListener(RarityX plugin) {
        this.plugin = plugin;
        this.rarityManager = plugin.getRarityManager();
    }
    
    /**
     * Handle item crafting to assign rarity to crafted items
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCraftItem(CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();
        
        if (result == null || result.getType() == Material.AIR) {
            return;
        }
        
        // Check if crafted item should have rarity
        if (shouldHaveRarity(result.getType())) {
            // Assign random rarity
            ItemStack rarityItem = rarityManager.assignRandomRarity(result.clone());
            
            // Get the rarity to apply buffs
            Rarity rarity = rarityManager.getRarity(rarityItem);
            if (rarity != null) {
                rarityItem = BuffManager.applyBuffs(rarityItem, rarity);
            }
            
            // Set the result
            event.setCurrentItem(rarityItem);
        }
    }
    
    /**
     * Handle inventory clicks to prevent rarity manipulation
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        ItemStack cursor = event.getCursor();
        
        // Prevent stacking items with different rarities
        if (clicked != null && cursor != null && 
            clicked.getType() == cursor.getType() &&
            clicked.getType() != Material.AIR) {
            
            Rarity clickedRarity = rarityManager.getRarity(clicked);
            Rarity cursorRarity = rarityManager.getRarity(cursor);
            
            // If both have rarities but they're different, prevent stacking
            if (clickedRarity != null && cursorRarity != null && clickedRarity != cursorRarity) {
                event.setCancelled(true);
                return;
            }
            
            // If one has rarity and other doesn't, prevent stacking
            if ((clickedRarity != null && cursorRarity == null) || 
                (clickedRarity == null && cursorRarity != null)) {
                event.setCancelled(true);
                return;
            }
        }
    }
    
    /**
     * Handle item drops (optional: could assign rarity to naturally dropped items)
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        
        // If item doesn't have rarity and config allows, assign one
        if (!rarityManager.hasRarity(item) && shouldHaveRarity(item.getType())) {
            if (plugin.getConfigManager().isAssignRarityOnDrop()) {
                ItemStack rarityItem = rarityManager.assignRandomRarity(item);
                Rarity rarity = rarityManager.getRarity(rarityItem);
                
                if (rarity != null) {
                    rarityItem = BuffManager.applyBuffs(rarityItem, rarity);
                    event.getItemDrop().setItemStack(rarityItem);
                }
            }
        }
    }
    
    /**
     * Determine if an item type should have rarity assigned
     */
    private boolean shouldHaveRarity(Material material) {
        if (material == null || material == Material.AIR) {
            return false;
        }
        
        String name = material.name();
        
        // Tools, weapons, and armor
        if (name.contains("SWORD") || name.contains("AXE") || 
            name.contains("PICKAXE") || name.contains("SHOVEL") || 
            name.contains("HOE") || name.contains("HELMET") || 
            name.contains("CHESTPLATE") || name.contains("LEGGINGS") || 
            name.contains("BOOTS")) {
            return true;
        }
        
        // Special items
        switch (material) {
            case BOW:
            case CROSSBOW:
            case TRIDENT:
            case SHIELD:
            case ELYTRA:
            case FISHING_ROD:
            case FLINT_AND_STEEL:
            case SHEARS:
                return true;
            default:
                return false;
        }
    }
}