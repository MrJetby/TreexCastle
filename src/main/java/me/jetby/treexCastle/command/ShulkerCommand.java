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

import java.util.ArrayList;
import java.util.List;

import static me.jetby.treexCastle.Main.NAMESPACED_KEY;

@RequiredArgsConstructor
public class ShulkerCommand implements CommandExecutor, TabCompleter {

    private final Main plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {

        if (args.length==0) {
            return true;
        }

        if (sender instanceof Player player) {
            switch (args[0].toLowerCase()) {
                case "menu":
                    plugin.getMainMenu().open(player);

                    break;
                case "wand":
                    ItemStack item = new ItemStack(Material.STICK);
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.getPersistentDataContainer().set(NAMESPACED_KEY, PersistentDataType.STRING, "wand");
                    itemMeta.setDisplayName("§dTreexCastle");
                    itemMeta.setLore(List.of(
                            "§dПКМ §7- §fДобавить локацию",
                            "§dЛКМ §7- §fУдалить локацию"));
                    item.setItemMeta(itemMeta);
                    player.getInventory().addItem(item);
                    break;
                case "reload":
                    long result = reload();
                    sender.sendMessage("§aУспешная перезагрузка, всего заняло "+result+" мс");
                    break;
                case "start":
                    plugin.getShulkerManager().spawnAllPossible();
                    sender.sendMessage("§aВы успешно заспавнили все шалкера");
                    break;
                case "stop":
                    plugin.getShulkerManager().removeAllClones();
                    sender.sendMessage("§aВы успешно очистили все шалкера");
                    break;
                default:

            }
        } else {
            if (args[0].equalsIgnoreCase("reload")) {
                long result = reload();
                sender.sendMessage("§aУспешная перезагрузка, всего заняло "+result+" мс");
                return true;
            }
            if (args[0].equalsIgnoreCase("start")) {
                plugin.getShulkerManager().spawnAllPossible();
                sender.sendMessage("§aВы успешно заспавнили все шалкера");
                return true;
            }
            if (args[0].equalsIgnoreCase("stop")) {
                plugin.getShulkerManager().removeAllClones();
                sender.sendMessage("§aВы успешно очистили все шалкера");
                return true;
            }
            sender.sendMessage("Эту команду можно выполнить только игрок");
        }
        return true;
    }

    private long reload() {
        long start = System.currentTimeMillis();
        try {
            plugin.getCfg().load();
            plugin.getItems().load();
            plugin.getTypes().load();
            plugin.getLocations().load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis()-start;
    }
    List<String> completions = new ArrayList<>();

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String s, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length==1) {
            completions.add("menu");
            completions.add("wand");
            completions.add("reload");
            completions.add("start");
            completions.add("stop");
        }

        String input = args[args.length - 1].toLowerCase();
        completions.removeIf(option -> !option.toLowerCase().startsWith(input));

        return completions;
    }
}
