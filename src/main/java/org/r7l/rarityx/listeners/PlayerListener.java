package org.r7l.rarityx.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.r7l.rarityx.RarityX;
import org.r7l.rarityx.rarity.Rarity;
import org.r7l.rarityx.rarity.RarityManager;

/**
 * Handles player-related events for rarity system
 * 
 * @author R7LRod
 */
public class PlayerListener implements Listener {
    
    private final RarityX plugin;
    private final RarityManager rarityManager;
    
    public PlayerListener(RarityX plugin) {
        this.plugin = plugin;
        this.rarityManager = plugin.getRarityManager();
    }
    
    /**
     * Handle player joining - could assign rarities to existing items
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // If configured, assign rarities to items without them
        if (plugin.getConfigManager().isAssignRarityOnJoin()) {
            assignRaritiesToInventory(player);
        }
    }
    
    /**
     * Handle block breaking for mining speed buffs
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        
        if (tool == null) return;
        
        Rarity rarity = rarityManager.getRarity(tool);
        if (rarity == null) return;
        
        // Apply mining speed bonus (this is a simplified implementation)
        // In a real implementation, you might want to use a more sophisticated approach
        double multiplier = rarity.getMultiplier();
        
        // Give experience bonus based on rarity
        if (rarity.ordinal() >= Rarity.UNCOMMON.ordinal()) {
            int bonusExp = (int) ((multiplier - 1.0) * 2);
            event.setExpToDrop(event.getExpToDrop() + bonusExp);
        }
    }
    
    /**
     * Handle entity damage for weapon buff effects
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        
        Player attacker = (Player) event.getDamager();
        ItemStack weapon = attacker.getInventory().getItemInMainHand();
        
        if (weapon == null) return;
        
        Rarity rarity = rarityManager.getRarity(weapon);
        if (rarity == null) return;
        
        // Apply damage multiplier (note: attribute modifiers should handle this,
        // but this provides additional effects)
        double multiplier = rarity.getMultiplier();
        
        // Add special effects for higher rarities
        switch (rarity) {
            case EPIC:
                // 10% chance to deal extra damage
                if (Math.random() < 0.1) {
                    double damage = event.getDamage();
                    event.setDamage(damage * 1.2);
                    attacker.sendMessage("§5Epic Strike!");
                }
                break;
            case MYTHIC:
                // 15% chance to deal critical damage
                if (Math.random() < 0.15) {
                    double damage = event.getDamage();
                    event.setDamage(damage * 1.5);
                    attacker.sendMessage("§6Mythic Critical Hit!");
                }
                break;
        }
    }
    
    /**
     * Assign rarities to items in player's inventory that don't have them
     */
    private void assignRaritiesToInventory(Player player) {
        ItemStack[] contents = player.getInventory().getContents();
        boolean modified = false;
        
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            
            if (item != null && !rarityManager.hasRarity(item) && shouldAssignRarity(item)) {
                ItemStack rarityItem = rarityManager.assignRandomRarity(item);
                Rarity rarity = rarityManager.getRarity(rarityItem);
                
                if (rarity != null) {
                    contents[i] = org.r7l.rarityx.buffs.BuffManager.applyBuffs(rarityItem, rarity);
                    modified = true;
                }
            }
        }
        
        if (modified) {
            player.getInventory().setContents(contents);
            player.sendMessage("§aRarities have been assigned to your items!");
        }
    }
    
    /**
     * Check if item should have rarity assigned
     */
    private boolean shouldAssignRarity(ItemStack item) {
        String name = item.getType().name();
        
        // Only assign to tools, weapons, and armor
        return name.contains("SWORD") || name.contains("AXE") || 
               name.contains("PICKAXE") || name.contains("SHOVEL") || 
               name.contains("HOE") || name.contains("HELMET") || 
               name.contains("CHESTPLATE") || name.contains("LEGGINGS") || 
               name.contains("BOOTS") || name.contains("BOW") ||
               item.getType().name().equals("TRIDENT") ||
               item.getType().name().equals("CROSSBOW") ||
               item.getType().name().equals("SHIELD");
    }
}