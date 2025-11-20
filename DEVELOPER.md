# RarityX Developer Documentation

## Architecture Overview

RarityX follows a modular architecture with clear separation of concerns:

```
org.r7l.rarityx/
├── RarityX.java              # Main plugin class
├── rarity/
│   ├── Rarity.java           # Enum defining rarity tiers
│   └── RarityManager.java    # Core rarity management logic
├── buffs/
│   └── BuffManager.java      # Handles buff application
├── config/
│   └── ConfigManager.java    # Configuration management
├── commands/
│   └── RarityXCommand.java   # Command handling
└── listeners/
    ├── ItemListener.java     # Item-related events
    └── PlayerListener.java   # Player-related events
```

## Core Classes

### RarityX.java
The main plugin class that:
- Initializes all managers and systems
- Registers commands and event listeners
- Provides access to manager instances
- Handles plugin lifecycle (enable/disable)

### Rarity.java
Enum that defines the five rarity tiers:
- Contains display names, colors, weights, and multipliers
- Provides utility methods for string conversion
- Calculates total weights for probability distribution

### RarityManager.java
Central class for rarity operations:
- Assigns random rarities based on weighted probability
- Stores/retrieves rarity data using Persistent Data Container
- Manages item lore updates
- Handles valuable material bonus calculations

### BuffManager.java
Handles all buff-related functionality:
- Applies attribute modifiers for weapons, tools, and armor
- Manages enchantment bonuses
- Provides special effects for higher rarities
- Categorizes items by type (weapon/tool/armor)

## Event System

### ItemListener.java
Handles item-related events:
- **CraftItemEvent**: Assigns rarities to crafted items
- **InventoryClickEvent**: Prevents stacking items with different rarities
- **PlayerDropItemEvent**: Optionally assigns rarities to dropped items

### PlayerListener.java
Handles player-related events:
- **PlayerJoinEvent**: Assigns rarities to existing items (optional)
- **BlockBreakEvent**: Applies mining bonuses and experience
- **EntityDamageByEntityEvent**: Handles weapon special effects

## Configuration System

### ConfigManager.java
Comprehensive configuration management:
- Loads default values and user overrides
- Provides type-safe getters for all config options
- Handles message formatting with placeholder replacement
- Supports runtime configuration reloading

### Configuration Sections
1. **General Settings**: Basic plugin behavior
2. **Rarity Weights**: Probability distribution
3. **Multipliers**: Buff strength per rarity
4. **Buff Settings**: Feature toggles
5. **Valuable Materials**: Enhanced rarity chances
6. **Messages**: Localization support

## Command System

### RarityXCommand.java
Comprehensive command handler with:
- Sub-command routing (help, reload, set, info, give, remove)
- Permission checking
- Tab completion for all parameters
- Input validation and error handling

### Command Structure
```
/rarityx <subcommand> [arguments]
├── help                      # Show help message
├── reload                    # Reload configuration
├── set <rarity>             # Set item rarity
├── info                     # Show item information
├── give <player> <rarity>   # Give item with rarity
└── remove                   # Remove item rarity
```

## Data Persistence

### Persistent Data Container
RarityX uses Minecraft's built-in NBT system:
- **Namespace**: `rarityx:rarity`
- **Data Type**: STRING
- **Value**: Rarity enum name (e.g., "MYTHIC")

### Benefits
- Survives server restarts
- No external database required
- Compatible with item serialization
- Efficient storage and retrieval

## Probability System

### Weighted Random Selection
The rarity assignment uses weighted probability:

```java
// Base weights
Common: 60.0 (60%)
Uncommon: 25.0 (25%) 
Rare: 10.0 (10%)
Epic: 4.0 (4%)
Mythic: 1.0 (1%)

// For valuable materials (diamond/netherite)
Multipliers are applied to increase chances for better rarities
```

### Algorithm
1. Calculate total weight (including material bonuses)
2. Generate random value between 0 and total weight
3. Iterate through rarities, subtracting weights
4. Return rarity when random value <= current weight

## Buff Application

### Attribute Modifiers
RarityX applies Minecraft attribute modifiers:

```java
// Attack damage example
AttributeModifier attackDamage = new AttributeModifier(
    UUID.randomUUID(),
    "rarityx_attack_damage",
    (multiplier - 1.0) * baseAttackDamage,
    AttributeModifier.Operation.ADD_NUMBER,
    EquipmentSlot.HAND
);
```

### Supported Attributes
- `GENERIC_ATTACK_DAMAGE`: Weapon damage
- `GENERIC_ATTACK_SPEED`: Weapon speed
- `GENERIC_ARMOR_TOUGHNESS`: Armor effectiveness
- `GENERIC_MAX_HEALTH`: Player health
- `GENERIC_MOVEMENT_SPEED`: Movement speed

### Enchantment System
Automatic enchantment application based on rarity:
- Uses `ItemStack.addUnsafeEnchantment()` for flexibility
- Respects enchantment compatibility with item types
- Applies level restrictions per rarity tier

## Extension Points

### Custom Rarity Implementation
To add new rarities:

1. Extend the `Rarity` enum:
```java
LEGENDARY("Legendary", ChatColor.AQUA, 0.5, 2.5);
```

2. Update `BuffManager` for new buff logic
3. Adjust configuration with new rarity settings

### Custom Buff Types
To add new buff types:

1. Extend `BuffManager.applyBuffs()`:
```java
if (isCustomItemType(material)) {
    applyCustomBuffs(meta, rarity, material);
}
```

2. Implement custom logic in new method
3. Add configuration options if needed

### Event Integration
To integrate with other plugins:

1. Listen for RarityX events (if implemented)
2. Use the API to check/modify item rarities
3. Respect existing rarity data

## API Usage Examples

### Basic Rarity Operations
```java
// Get plugin instance
RarityX plugin = (RarityX) Bukkit.getPluginManager().getPlugin("RarityX");
RarityManager rarityManager = plugin.getRarityManager();

// Check if item has rarity
ItemStack item = player.getInventory().getItemInMainHand();
if (rarityManager.hasRarity(item)) {
    Rarity rarity = rarityManager.getRarity(item);
    player.sendMessage("Item rarity: " + rarity.getFormattedName());
}

// Assign random rarity
ItemStack rarityItem = rarityManager.assignRandomRarity(item);
rarityItem = BuffManager.applyBuffs(rarityItem, rarityManager.getRarity(rarityItem));

// Set specific rarity
ItemStack mythicItem = rarityManager.setRarity(item, Rarity.MYTHIC);
mythicItem = BuffManager.applyBuffs(mythicItem, Rarity.MYTHIC);
```

### Configuration Access
```java
ConfigManager config = plugin.getConfigManager();

// Get rarity weights
double mythicWeight = config.getMythicWeight();

// Check if feature is enabled
if (config.isEnableAttributeModifiers()) {
    // Apply attribute modifiers
}

// Get formatted message
String message = config.getFormattedMessage("rarity-set", 
    "rarity", Rarity.EPIC.getFormattedName());
```

## Performance Considerations

### Optimization Strategies
1. **Lazy Loading**: Rarities are only calculated when needed
2. **Caching**: Rarity checks use efficient NBT lookups
3. **Event Filtering**: Listeners check item validity early
4. **Batch Operations**: Multiple changes use single ItemMeta update

### Memory Usage
- Minimal memory footprint
- No persistent collections or caches
- NBT data stored directly on items
- Configuration loaded once at startup

### Threading
- All operations are thread-safe
- Uses Bukkit's main thread for all modifications
- No async operations or external threads

## Testing Recommendations

### Unit Testing
Focus on testing:
- Rarity probability calculations
- Configuration loading/validation
- Buff application logic
- Command parameter parsing

### Integration Testing
Test with:
- Multiple Minecraft versions
- Different server implementations (Spigot, Paper)
- Various item types and configurations
- Permission system integration

### Performance Testing
Monitor:
- Event processing times
- Memory usage during normal operation
- Item creation/modification performance
- Configuration reload times

## Troubleshooting Guide

### Common Development Issues

1. **NBT Data Not Persisting**
   - Ensure ItemMeta is properly set back to ItemStack
   - Check that NamespacedKey is correctly formatted
   - Verify PersistentDataContainer usage

2. **Attribute Modifiers Not Working**
   - Confirm correct AttributeModifier parameters
   - Check EquipmentSlot matches item type
   - Ensure positive modifier values for buffs

3. **Event Conflicts**
   - Use appropriate EventPriority
   - Check event cancellation status
   - Verify listener registration

4. **Configuration Issues**
   - Validate YAML syntax
   - Check default value fallbacks
   - Test configuration reload functionality

This documentation provides a comprehensive technical overview for developers working with or extending RarityX.