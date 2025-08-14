package me.jetby.treexCastle.handler;

import lombok.RequiredArgsConstructor;
import me.jetby.treexCastle.Main;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;

import static me.jetby.treexCastle.Main.NAMESPACED_KEY;


@RequiredArgsConstructor
public class Wand implements Listener {
    private final Random RANDOM = new Random();

    private final Main plugin;

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        ItemStack itemInHand = player.getItemInHand();

        if (e.getClickedBlock() == null) return;

        Location location = block.getLocation();

        if (itemInHand.hasItemMeta() && itemInHand.getItemMeta().getPersistentDataContainer().has(NAMESPACED_KEY, PersistentDataType.STRING)) {
            e.setCancelled(true);
            if (e.getAction().isLeftClick()) {
                if (plugin.getLocations().getLocations().contains(location)) {
                    plugin.getLocations().getLocations().remove(location);
                    plugin.getLocations().save();
                    player.sendTitle("§dTreexCastle", "§cЛокация убрана");
                } else {
                    player.sendTitle("§dTreexCastle", "§7Локация не найдена");
                }
            } else if (e.getAction().isRightClick()) {
                if (!plugin.getLocations().getLocations().contains(location)) {
                    plugin.getLocations().getLocations().add(location);
                    player.sendTitle("§dTreexCastle", "§aЛокация добавлена");
                    plugin.getLocations().save();

                }
            }
        }

    }
}