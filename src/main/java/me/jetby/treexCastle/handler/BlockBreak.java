package me.jetby.treexCastle.handler;

import lombok.RequiredArgsConstructor;
import me.jetby.treexCastle.Main;
import me.jetby.treexCastle.Shulker;
import me.jetby.treexCastle.ShulkerClones;
import me.jetby.treexCastle.tools.Holo;
import me.jetby.treexCastle.tools.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BlockBreak implements Listener {

    private final Main plugin;


    @EventHandler
    public void onBreak(BlockBreakEvent e) {

        final ShulkerClones[] clone = {null};
        final Shulker[] shulker = {null};

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            for (Main.clone shulkerClone : plugin.getClones().values()) {
                if (shulkerClone.clones().getLocation().equals(e.getBlock().getLocation())) {
                    clone[0] = shulkerClone.clones();
                    shulker[0] = shulkerClone.shulker();
                    break;
                }
            }

            if (clone[0] == null || shulker[0] == null) {
                return;
            }

            e.setCancelled(true);

            clone[0].setDurability(clone[0].getDurability() - 1);

            if (clone[0].getDurability() <= 0) {
                Bukkit.getScheduler().runTask(plugin, () -> e.getBlock().setType(Material.AIR));
                Bukkit.getScheduler().runTask(plugin, () -> shulker[0].dropRandomItems(clone[0].getLocation()));
                shulker[0].delete(clone[0]);
                return;
            }
            shulker[0].updateHologram(clone[0]);
            shulker[0].sendActionbar(clone[0], e.getPlayer());
        });
    }
}
