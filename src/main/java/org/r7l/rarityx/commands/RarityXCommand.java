package org.r7l.rarityx.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.r7l.rarityx.RarityX;
import org.r7l.rarityx.buffs.BuffManager;
import org.r7l.rarityx.config.ConfigManager;
import org.r7l.rarityx.rarity.Rarity;
import org.r7l.rarityx.rarity.RarityManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main command handler for RarityX
 * 
 * @author R7LRod
 */
public class RarityXCommand implements CommandExecutor, TabCompleter {
    
    private final RarityX plugin;
    private final RarityManager rarityManager;
    private final ConfigManager configManager;
    
    public RarityXCommand(RarityX plugin) {
        this.plugin = plugin;
        this.rarityManager = plugin.getRarityManager();
        this.configManager = plugin.getConfigManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "help":
                sendHelp(sender);
                break;
            case "reload":
                handleReload(sender);
                break;
            case "set":
                handleSet(sender, args);
                break;
            case "info":
                handleInfo(sender);
                break;
            case "give":
                handleGive(sender, args);
                break;
            case "remove":
                handleRemove(sender);
                break;
            default:
                sender.sendMessage(configManager.getFormattedMessage("invalid-command"));
                sendHelp(sender);
        }
        
        return true;
    }
    
    /**
     * Send help message to command sender
     */
    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6=== RarityX Commands ===");
        sender.sendMessage("§e/rarityx help §7- Show this help message");
        
        if (sender.hasPermission("rarityx.admin")) {
            sender.sendMessage("§e/rarityx reload §7- Reload configuration");
            sender.sendMessage("§e/rarityx set <rarity> §7- Set rarity of item in hand");
            sender.sendMessage("§e/rarityx give <player> <rarity> §7- Give player item with rarity");
            sender.sendMessage("§e/rarityx remove §7- Remove rarity from item in hand");
        }
        
        sender.sendMessage("§e/rarityx info §7- Show info about item in hand");
        sender.sendMessage("§7Rarities: §fCommon, §aUncommon, §9Rare, §5Epic, §6Mythic");
    }
    
    /**
     * Handle reload command
     */
    private void handleReload(CommandSender sender) {
        if (!sender.hasPermission("rarityx.admin")) {
            sender.sendMessage(configManager.getFormattedMessage("no-permission"));
            return;
        }
        
        try {
            configManager.reloadConfig();
            sender.sendMessage(configManager.getFormattedMessage("config-reloaded"));
        } catch (Exception e) {
            sender.sendMessage(configManager.getPrefix() + "§cError reloading configuration: " + e.getMessage());
        }
    }
    
    /**
     * Handle set rarity command
     */
    private void handleSet(CommandSender sender, String[] args) {
        if (!sender.hasPermission("rarityx.admin")) {
            sender.sendMessage(configManager.getFormattedMessage("no-permission"));
            return;
        }
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getPrefix() + "§cThis command can only be used by players!");
            return;
        }
        
        if (args.length < 2) {
            sender.sendMessage(configManager.getPrefix() + "§cUsage: /rarityx set <rarity>");
            return;
        }
        
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        
        if (item == null || item.getType().isAir()) {
            sender.sendMessage(configManager.getFormattedMessage("no-item-in-hand"));
            return;
        }
        
        Rarity rarity = Rarity.fromString(args[1]);
        if (rarity == null) {
            sender.sendMessage(configManager.getFormattedMessage("invalid-rarity"));
            return;
        }
        
        ItemStack rarityItem = rarityManager.setRarity(item, rarity);
        rarityItem = BuffManager.applyBuffs(rarityItem, rarity);
        
        player.getInventory().setItemInMainHand(rarityItem);
        sender.sendMessage(configManager.getFormattedMessage("rarity-set", 
            "rarity", rarity.getFormattedName()));
    }
    
    /**
     * Handle info command
     */
    private void handleInfo(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getPrefix() + "§cThis command can only be used by players!");
            return;
        }
        
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        
        if (item == null || item.getType().isAir()) {
            sender.sendMessage(configManager.getFormattedMessage("no-item-in-hand"));
            return;
        }
        
        Rarity rarity = rarityManager.getRarity(item);
        String rarityText = rarity != null ? rarity.getFormattedName() : "§7None";
        
        sender.sendMessage(configManager.getFormattedMessage("item-info",
            "item", item.getType().toString(),
            "rarity", rarityText));
    }
    
    /**
     * Handle give command
     */
    private void handleGive(CommandSender sender, String[] args) {
        if (!sender.hasPermission("rarityx.admin")) {
            sender.sendMessage(configManager.getFormattedMessage("no-permission"));
            return;
        }
        
        if (args.length < 3) {
            sender.sendMessage(configManager.getPrefix() + "§cUsage: /rarityx give <player> <rarity>");
            return;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(configManager.getFormattedMessage("player-not-found"));
            return;
        }
        
        Rarity rarity = Rarity.fromString(args[2]);
        if (rarity == null) {
            sender.sendMessage(configManager.getFormattedMessage("invalid-rarity"));
            return;
        }
        
        // Give a diamond sword with the specified rarity as example
        ItemStack item = new ItemStack(org.bukkit.Material.DIAMOND_SWORD);
        item = rarityManager.setRarity(item, rarity);
        item = BuffManager.applyBuffs(item, rarity);
        
        target.getInventory().addItem(item);
        sender.sendMessage(configManager.getPrefix() + "§aGave " + target.getName() + 
            " a " + rarity.getFormattedName() + " §aDiamond Sword!");
        target.sendMessage(configManager.getPrefix() + "§aYou received a " + 
            rarity.getFormattedName() + " §aDiamond Sword!");
    }
    
    /**
     * Handle remove rarity command
     */
    private void handleRemove(CommandSender sender) {
        if (!sender.hasPermission("rarityx.admin")) {
            sender.sendMessage(configManager.getFormattedMessage("no-permission"));
            return;
        }
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(configManager.getPrefix() + "§cThis command can only be used by players!");
            return;
        }
        
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        
        if (item == null || item.getType().isAir()) {
            sender.sendMessage(configManager.getFormattedMessage("no-item-in-hand"));
            return;
        }
        
        if (!rarityManager.hasRarity(item)) {
            sender.sendMessage(configManager.getPrefix() + "§cThis item has no rarity!");
            return;
        }
        
        ItemStack cleanItem = rarityManager.removeRarity(item);
        player.getInventory().setItemInMainHand(cleanItem);
        sender.sendMessage(configManager.getPrefix() + "§aRarity removed from item!");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("help", "info");
            
            if (sender.hasPermission("rarityx.admin")) {
                subCommands = Arrays.asList("help", "reload", "set", "info", "give", "remove");
            }
            
            for (String subCommand : subCommands) {
                if (subCommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subCommand);
                }
            }
        } else if (args.length == 2 && sender.hasPermission("rarityx.admin")) {
            if (args[0].equalsIgnoreCase("set")) {
                // Tab complete rarities for set command
                for (Rarity rarity : Rarity.values()) {
                    if (rarity.name().toLowerCase().startsWith(args[1].toLowerCase()) ||
                        rarity.getDisplayName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(rarity.getDisplayName());
                    }
                }
            } else if (args[0].equalsIgnoreCase("give")) {
                // Tab complete player names for give command
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(player.getName());
                    }
                }
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give") && sender.hasPermission("rarityx.admin")) {
            // Tab complete rarities for give command
            for (Rarity rarity : Rarity.values()) {
                if (rarity.name().toLowerCase().startsWith(args[2].toLowerCase()) ||
                    rarity.getDisplayName().toLowerCase().startsWith(args[2].toLowerCase())) {
                    completions.add(rarity.getDisplayName());
                }
            }
        }
        
        return completions;
    }
}