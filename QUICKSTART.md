# RarityX Quick Start Guide

## What is RarityX?

RarityX is a Minecraft plugin that adds an RPG-style rarity system to items. Every tool, weapon, and armor piece can receive one of five rarity levels, each providing unique buffs and visual enhancements.

## Installation

1. **Download**: Get the latest RarityX.jar file
2. **Install**: Place it in your server's `plugins` folder
3. **Restart**: Restart your Minecraft server
4. **Verify**: Type `/rarityx help` to confirm installation

## Rarity Levels

| Rarity | Color | Chance | Buffs |
|--------|-------|--------|--------|
| **Common** | White | 60% | No buffs |
| **Uncommon** | Green | 25% | 10% improvement |
| **Rare** | Blue | 10% | 25% improvement |
| **Epic** | Purple | 4% | 50% improvement |
| **Mythic** | Gold | 1% | 100% improvement |

## How It Works

### Automatic Rarity Assignment
- **Crafting**: Items get rarities when crafted
- **Valuable Items**: Diamond/Netherite have better rarity chances
- **Random Distribution**: Based on configured probability weights

### Item Buffs
- **Weapons**: Increased damage and attack speed
- **Tools**: Mining bonuses and experience boosts
- **Armor**: Enhanced protection and special abilities
- **Enchantments**: Higher rarities get better enchantments

### Visual Indicators
Items show their rarity in the lore:
```
Diamond Sword

Rarity: Mythic
```

## Basic Commands

```bash
# Show help
/rarityx help

# Check item rarity
/rarityx info

# Set item rarity (admin)
/rarityx set mythic

# Give item with rarity (admin)
/rarityx give PlayerName epic

# Reload config (admin)
/rarityx reload
```

## Configuration Highlights

Edit `plugins/RarityX/config.yml` to customize:

### Rarity Chances
```yaml
rarity-weights:
  common: 60.0
  uncommon: 25.0
  rare: 10.0
  epic: 4.0
  mythic: 1.0
```

### Buff Strength
```yaml
rarity-multipliers:
  common: 1.0
  uncommon: 1.1
  rare: 1.25
  epic: 1.5
  mythic: 2.0
```

### When to Apply Rarities
```yaml
general:
  assign-rarity-on-craft: true   # When crafting items
  assign-rarity-on-join: false   # To existing items on join
  assign-rarity-on-drop: false   # When dropping items
```

## Examples

### Mythic Diamond Sword
- **Base Damage**: 7 → 14 (2x multiplier)
- **Enchantments**: Guaranteed high-level enchantments
- **Special Effect**: 15% chance for critical hits
- **Lore**: Golden "Mythic" text

### Epic Diamond Pickaxe
- **Mining Speed**: 50% faster
- **Experience**: Bonus XP when mining
- **Enchantments**: Level 3 enchantments
- **Special Effect**: 10% chance for extra damage

### Rare Diamond Armor
- **Armor Toughness**: 25% increase
- **Max Health**: +2 hearts when wearing full set
- **Enchantments**: Level 2 Protection

## Tips for Server Admins

1. **Balance**: Adjust rarity weights based on your server's economy
2. **Valuable Materials**: Enable diamond/netherite bonuses for end-game content
3. **Player Experience**: Consider enabling rarity assignment on join for existing players
4. **Performance**: The plugin is lightweight and optimized

## Common Questions

**Q: Do rarities affect vanilla gameplay?**
A: No, the plugin only enhances items without breaking Minecraft mechanics.

**Q: Can players see rarity without commands?**
A: Yes, rarities are visible in item lore with colored text.

**Q: Do rarities work with other plugins?**
A: Yes, RarityX uses Minecraft's built-in NBT system for compatibility.

**Q: Can I disable certain features?**
A: Yes, all features can be toggled in the configuration file.

## Need Help?

- **Commands**: `/rarityx help` in-game
- **Documentation**: See README.md for detailed information
- **Support**: Contact R7LRod or check the GitHub repository

---

Enjoy your enhanced Minecraft experience with RarityX! 🎮✨