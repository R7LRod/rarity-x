# RarityX - Minecraft Plugin Documentation

## Overview

RarityX is a comprehensive Minecraft plugin that adds a dynamic rarity system to items in your server. Items can be assigned different rarity levels (Common, Uncommon, Rare, Epic, Mythic) which provide various buffs and visual enhancements.

## Features

### 🎯 Core Features
- **5 Rarity Tiers**: Common, Uncommon, Rare, Epic, and Mythic
- **Dynamic Item Enhancement**: Items receive buffs based on their rarity
- **Visual Identification**: Colored lore indicates item rarity
- **Configurable System**: Extensive configuration options
- **Automatic Assignment**: Items can receive rarities when crafted, picked up, or on join
- **Special Effects**: Higher rarities provide unique combat effects

### 🔧 Rarity System

#### Rarity Tiers
1. **Common** (White) - 60% chance - 1.0x multiplier
2. **Uncommon** (Green) - 25% chance - 1.1x multiplier
3. **Rare** (Blue) - 10% chance - 1.25x multiplier
4. **Epic** (Dark Purple) - 4% chance - 1.5x multiplier
5. **Mythic** (Gold) - 1% chance - 2.0x multiplier

### 💪 Buff System

#### Weapon Buffs
- **Attack Damage**: Increased based on rarity multiplier
- **Attack Speed**: Enhanced for Rare+ weapons
- **Special Effects**: 
  - Epic weapons: 10% chance for 1.2x damage
  - Mythic weapons: 15% chance for 1.5x critical damage

#### Tool Buffs
- **Mining Speed**: Visual indicator and experience bonuses
- **Durability**: Enhanced through enchantments
- **Experience Bonus**: Additional XP when mining with rare+ tools

#### Armor Buffs
- **Armor Toughness**: Enhanced for Rare+ armor
- **Max Health**: Additional health for Epic+ armor
- **Movement Speed**: Mythic boots provide speed boost

#### Enchantment Bonuses
- **Uncommon**: 25% chance for level 1 enchantments
- **Rare**: 50% chance for level 1-2 enchantments
- **Epic**: 75% chance for level 1-3 enchantments
- **Mythic**: Guaranteed level 1-4 enchantments + chance for additional

## Installation

1. Download the latest RarityX JAR file
2. Place it in your server's `plugins` folder
3. Start/restart your server
4. Configure the plugin using `/rarityx reload` after editing `config.yml`

## Commands

### Player Commands
- `/rarityx help` - Show help message
- `/rarityx info` - Show rarity information of item in hand

### Admin Commands
- `/rarityx reload` - Reload configuration
- `/rarityx set <rarity>` - Set rarity of item in hand
- `/rarityx give <player> <rarity>` - Give player an item with specified rarity
- `/rarityx remove` - Remove rarity from item in hand

### Command Aliases
- `/rx` - Short alias for `/rarityx`
- `/rarity` - Alternative alias

## Permissions

```yaml
rarityx.admin:
  description: Access to all RarityX commands
  default: op
  
rarityx.use:
  description: Use RarityX features
  default: true
  
rarityx.bypass:
  description: Bypass rarity restrictions
  default: false
```

## Configuration

The plugin comes with extensive configuration options in `config.yml`:

### General Settings
```yaml
general:
  assign-rarity-on-craft: true      # Assign rarity when crafting
  assign-rarity-on-join: false     # Assign to existing items on join
  assign-rarity-on-drop: false     # Assign when dropping items
  prevent-different-rarity-stacking: true  # Prevent stacking different rarities
```

### Rarity Weights
```yaml
rarity-weights:
  common: 60.0
  uncommon: 25.0
  rare: 10.0
  epic: 4.0
  mythic: 1.0
```

### Multipliers
```yaml
rarity-multipliers:
  common: 1.0
  uncommon: 1.1
  rare: 1.25
  epic: 1.5
  mythic: 2.0
```

### Buff System
```yaml
buffs:
  enable-attribute-modifiers: true
  enable-enchantment-bonuses: true
  enable-special-effects: true
```

### Valuable Materials
```yaml
valuable-materials:
  enabled: true
  diamond-multiplier: 2.0    # Diamond gear has 2x better rarity chances
  netherite-multiplier: 3.0  # Netherite gear has 3x better rarity chances
```

## API Usage

### For Developers

#### Getting Rarity of an Item
```java
RarityX plugin = (RarityX) Bukkit.getPluginManager().getPlugin("RarityX");
RarityManager rarityManager = plugin.getRarityManager();
Rarity rarity = rarityManager.getRarity(itemStack);
```

#### Setting Rarity
```java
ItemStack item = rarityManager.setRarity(itemStack, Rarity.MYTHIC);
item = BuffManager.applyBuffs(item, Rarity.MYTHIC);
```

#### Checking if Item Has Rarity
```java
boolean hasRarity = rarityManager.hasRarity(itemStack);
```

## Advanced Features

### Valuable Materials System
The plugin recognizes "valuable" materials and gives them higher chances for better rarities:
- Diamond tools/armor: 2x multiplier for better rarities
- Netherite tools/armor: 3x multiplier for better rarities
- Special items (Elytra, Trident, etc.): Enhanced chances

### Attribute Modifier System
Items receive actual attribute modifiers that affect:
- Attack damage and speed
- Armor and armor toughness
- Max health
- Movement speed

### Persistent Data
Rarities are stored using Minecraft's Persistent Data Container system, ensuring:
- Data survives server restarts
- No conflicts with other plugins
- Efficient storage and retrieval

## Troubleshooting

### Common Issues

1. **Items not getting rarities when crafted**
   - Check if `assign-rarity-on-craft` is enabled in config
   - Ensure the item type is supported (tools, weapons, armor)

2. **Buffs not applying**
   - Verify `enable-attribute-modifiers` is true
   - Check if the item has a valid rarity assigned

3. **Permission errors**
   - Ensure players have `rarityx.use` permission
   - Admin commands require `rarityx.admin` permission

4. **Config not loading**
   - Check YAML syntax in config.yml
   - Use `/rarityx reload` after making changes

### Performance Considerations
- The plugin uses efficient persistent data storage
- Event listeners are optimized for minimal performance impact
- Rarity assignment is calculated only when needed

## Technical Details

### Supported Items
- All swords, axes, pickaxes, shovels, hoes
- All armor pieces (helmets, chestplates, leggings, boots)
- Special items: bow, crossbow, trident, shield, elytra, fishing rod

### Data Storage
- Uses Minecraft's NBT Persistent Data Container
- Namespace: `rarityx:rarity`
- Data type: String (rarity name)

### Compatibility
- **Minecraft Version**: 1.20+
- **Server Software**: Spigot, Paper, Purpur
- **Java Version**: 17+
- **Dependencies**: None (PlaceholderAPI support optional)

## Changelog

### Version 1.0.0
- Initial release
- Complete rarity system with 5 tiers
- Comprehensive buff system
- Full configuration options
- Command system with tab completion
- Event listeners for automatic rarity assignment
- Persistent data storage

## Support

For support, bug reports, or feature requests:
- GitHub: [https://github.com/R7LRod/rarity-x](https://github.com/R7LRod/rarity-x)
- Discord: Contact R7LRod

## License

This plugin is released under the MIT License. See LICENSE file for details.

---

**Note**: This plugin is designed for servers looking to add an RPG-like item enhancement system. It provides extensive customization options while maintaining performance and compatibility.