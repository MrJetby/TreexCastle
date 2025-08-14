package me.jetby.treexCastle.handler;

import lombok.RequiredArgsConstructor;
import me.jetby.treexCastle.Main;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class Wand implements Listener {

    private final Main plugin;
    private final NamespacedKey NAMESPACED_KEY;


    public Wand(Main plugin) {
        this.plugin = plugin;
        this.NAMESPACED_KEY = new NamespacedKey(plugin, "wand");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        ItemStack itemInHand = player.getItemInHand();

        if (e.getClickedBlock() == null) return; // Проверка на null

        Location location = block.getLocation();

        if (itemInHand.hasItemMeta() && itemInHand.getItemMeta().getPersistentDataContainer().has(NAMESPACED_KEY, PersistentDataType.STRING)) {

        }

    }
}