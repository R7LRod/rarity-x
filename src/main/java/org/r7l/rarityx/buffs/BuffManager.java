package org.r7l.rarityx.buffs;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.r7l.rarityx.rarity.Rarity;

import java.util.UUID;

/**
 * Handles buff application based on rarity
 * 
 * @author R7LRod
 */
public class BuffManager {
    
    /**
     * Apply buffs to an item based on its rarity
     * @param item The item to apply buffs to
     * @param rarity The rarity of the item
     * @return The modified item with buffs
     */
    public static ItemStack applyBuffs(ItemStack item, Rarity rarity) {
        if (item == null || item.getType() == Material.AIR || rarity == null) {
            return item;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        
        Material material = item.getType();
        
        // Apply weapon buffs
        if (isWeapon(material)) {
            applyWeaponBuffs(meta, rarity, material);
        }
        
        // Apply tool buffs
        if (isTool(material)) {
            applyToolBuffs(meta, rarity, material);
        }
        
        // Apply armor buffs
        if (isArmor(material)) {
            applyArmorBuffs(meta, rarity, material);
        }
        
        // Apply enchantment buffs
        applyEnchantmentBuffs(item, rarity);
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Apply weapon-specific buffs
     */
    private static void applyWeaponBuffs(ItemMeta meta, Rarity rarity, Material material) {
        double multiplier = rarity.getMultiplier();
        
        // Attack damage buff
        AttributeModifier attackDamage = new AttributeModifier(
            UUID.randomUUID(),
            "rarityx_attack_damage",
            (multiplier - 1.0) * getBaseAttackDamage(material),
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlot.HAND
        );
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, attackDamage);
        
        // Attack speed buff for higher rarities
        if (rarity.ordinal() >= Rarity.RARE.ordinal()) {
            AttributeModifier attackSpeed = new AttributeModifier(
                UUID.randomUUID(),
                "rarityx_attack_speed",
                (multiplier - 1.0) * 0.5,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlot.HAND
            );
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeed);
        }
    }
    
    /**
     * Apply tool-specific buffs
     */
    private static void applyToolBuffs(ItemMeta meta, Rarity rarity, Material material) {
        double multiplier = rarity.getMultiplier();
        
        // Mining speed boost for higher rarities
        if (rarity.ordinal() >= Rarity.UNCOMMON.ordinal()) {
            // This would need to be handled in event listeners for actual mining speed
            // Here we just add lore to indicate the boost
            java.util.List<String> lore = meta.getLore();
            if (lore == null) {
                lore = new java.util.ArrayList<>();
            }
            
            int speedBoost = (int) ((multiplier - 1.0) * 100);
            lore.add("§6Mining Speed: +" + speedBoost + "%");
            meta.setLore(lore);
        }
    }
    
    /**
     * Apply armor-specific buffs
     */
    private static void applyArmorBuffs(ItemMeta meta, Rarity rarity, Material material) {
        double multiplier = rarity.getMultiplier();
        EquipmentSlot slot = getArmorSlot(material);
        
        if (slot == null) return;
        
        // Armor toughness buff
        if (rarity.ordinal() >= Rarity.RARE.ordinal()) {
            AttributeModifier toughness = new AttributeModifier(
                UUID.randomUUID(),
                "rarityx_armor_toughness",
                (multiplier - 1.0) * 2.0,
                AttributeModifier.Operation.ADD_NUMBER,
                slot
            );
            meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughness);
        }
        
        // Additional health for epic and mythic armor
        if (rarity.ordinal() >= Rarity.EPIC.ordinal()) {
            AttributeModifier health = new AttributeModifier(
                UUID.randomUUID(),
                "rarityx_max_health",
                (multiplier - 1.0) * 4.0,
                AttributeModifier.Operation.ADD_NUMBER,
                slot
            );
            meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, health);
        }
        
        // Movement speed for mythic armor
        if (rarity == Rarity.MYTHIC && material.name().contains("BOOTS")) {
            AttributeModifier speed = new AttributeModifier(
                UUID.randomUUID(),
                "rarityx_movement_speed",
                0.02, // 2% speed boost
                AttributeModifier.Operation.ADD_NUMBER,
                slot
            );
            meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, speed);
        }
    }
    
    /**
     * Apply enchantment buffs based on rarity
     */
    private static void applyEnchantmentBuffs(ItemStack item, Rarity rarity) {
        switch (rarity) {
            case UNCOMMON:
                // 25% chance for low-level enchantments
                if (Math.random() < 0.25) {
                    addRandomEnchantment(item, 1);
                }
                break;
            case RARE:
                // 50% chance for level 1-2 enchantments
                if (Math.random() < 0.5) {
                    addRandomEnchantment(item, 2);
                }
                break;
            case EPIC:
                // 75% chance for level 1-3 enchantments
                if (Math.random() < 0.75) {
                    addRandomEnchantment(item, 3);
                }
                break;
            case MYTHIC:
                // Guaranteed high-level enchantments
                addRandomEnchantment(item, 4);
                // Chance for additional enchantment
                if (Math.random() < 0.5) {
                    addRandomEnchantment(item, 3);
                }
                break;
        }
    }
    
    /**
     * Add a random enchantment to an item
     */
    private static void addRandomEnchantment(ItemStack item, int maxLevel) {
        Material material = item.getType();
        Enchantment[] possibleEnchants = getPossibleEnchantments(material);
        
        if (possibleEnchants.length == 0) return;
        
        Enchantment enchant = possibleEnchants[(int) (Math.random() * possibleEnchants.length)];
        int level = Math.min(maxLevel, enchant.getMaxLevel());
        level = Math.max(1, (int) (Math.random() * level) + 1);
        
        item.addUnsafeEnchantment(enchant, level);
    }
    
    /**
     * Get possible enchantments for a material
     */
    private static Enchantment[] getPossibleEnchantments(Material material) {
        if (isWeapon(material)) {
            return new Enchantment[]{
                Enchantment.DAMAGE_ALL, Enchantment.DAMAGE_ARTHROPODS,
                Enchantment.DAMAGE_UNDEAD, Enchantment.KNOCKBACK,
                Enchantment.FIRE_ASPECT, Enchantment.LOOTING,
                Enchantment.SWEEPING_EDGE, Enchantment.MENDING
            };
        } else if (isTool(material)) {
            return new Enchantment[]{
                Enchantment.DIG_SPEED, Enchantment.SILK_TOUCH,
                Enchantment.LOOT_BONUS_BLOCKS, Enchantment.MENDING
            };
        } else if (isArmor(material)) {
            return new Enchantment[]{
                Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_FIRE,
                Enchantment.PROTECTION_PROJECTILE, Enchantment.PROTECTION_EXPLOSIONS,
                Enchantment.THORNS, Enchantment.MENDING
            };
        }
        
        return new Enchantment[0];
    }
    
    /**
     * Check if material is a weapon
     */
    private static boolean isWeapon(Material material) {
        String name = material.name();
        return name.contains("SWORD") || name.contains("AXE") || 
               material == Material.BOW || material == Material.CROSSBOW ||
               material == Material.TRIDENT;
    }
    
    /**
     * Check if material is a tool
     */
    private static boolean isTool(Material material) {
        String name = material.name();
        return name.contains("PICKAXE") || name.contains("SHOVEL") || 
               name.contains("HOE") || (name.contains("AXE") && !name.contains("PICKAXE"));
    }
    
    /**
     * Check if material is armor
     */
    private static boolean isArmor(Material material) {
        String name = material.name();
        return name.contains("HELMET") || name.contains("CHESTPLATE") || 
               name.contains("LEGGINGS") || name.contains("BOOTS");
    }
    
    /**
     * Get base attack damage for weapons
     */
    private static double getBaseAttackDamage(Material material) {
        switch (material) {
            case WOODEN_SWORD:
                return 4.0;
            case STONE_SWORD:
                return 5.0;
            case IRON_SWORD:
                return 6.0;
            case DIAMOND_SWORD:
                return 7.0;
            case NETHERITE_SWORD:
                return 8.0;
            case WOODEN_AXE:
                return 7.0;
            case STONE_AXE:
                return 9.0;
            case IRON_AXE:
                return 9.0;
            case DIAMOND_AXE:
                return 9.0;
            case NETHERITE_AXE:
                return 10.0;
            case TRIDENT:
                return 9.0;
            default:
                return 1.0;
        }
    }
    
    /**
     * Get equipment slot for armor pieces
     */
    private static EquipmentSlot getArmorSlot(Material material) {
        String name = material.name();
        if (name.contains("HELMET")) return EquipmentSlot.HEAD;
        if (name.contains("CHESTPLATE")) return EquipmentSlot.CHEST;
        if (name.contains("LEGGINGS")) return EquipmentSlot.LEGS;
        if (name.contains("BOOTS")) return EquipmentSlot.FEET;
        return null;
    }
}