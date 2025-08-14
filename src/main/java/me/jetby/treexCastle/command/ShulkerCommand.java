package me.jetby.treexCastle.command;

import lombok.RequiredArgsConstructor;
import me.jetby.treexCastle.Main;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static me.jetby.treexCastle.Main.NAMESPACED_KEY;

@RequiredArgsConstructor
public class ShulkerCommand implements CommandExecutor, TabCompleter {

    private final Main plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {

        if (sender instanceof Player player) {
            if (args[0].equalsIgnoreCase("menu")) {
                plugin.getMainMenu().open(player);
                return true;
            }
            if (args[0].equalsIgnoreCase("create")) {
                plugin.getTypes().getShulkers().get(args[1]).create();
                return true;
            }
            if (args[0].equalsIgnoreCase("wand")) {
                ItemStack item = new ItemStack(Material.STICK);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.getPersistentDataContainer().set(NAMESPACED_KEY, PersistentDataType.STRING, "wand");
                itemMeta.setDisplayName("§dTreexCastle");
                itemMeta.setLore(List.of(
                        "§dПКМ §7- §fДобавить локацию",
                        "§dЛКМ §7- §fУдалить локацию"));
                item.setItemMeta(itemMeta);
                player.getInventory().addItem(item);

                return true;
            }

        }



        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
