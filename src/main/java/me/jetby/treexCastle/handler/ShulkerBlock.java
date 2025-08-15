package me.jetby.treexCastle.handler;

import lombok.RequiredArgsConstructor;
import me.jetby.treexCastle.Main;
import me.jetby.treexCastle.Shulker;
import me.jetby.treexCastle.ShulkerClones;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@RequiredArgsConstructor
public class ShulkerBlock implements Listener {

    private final Main plugin;

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock()==null) return;

        final ShulkerClones clone = plugin.getShulkerManager().getShulkerCloneAt(e.getClickedBlock().getLocation());
        final Shulker shulker = plugin.getShulkerManager().getShulkerAt(e.getClickedBlock().getLocation());

        if (clone == null || shulker == null) {
            return;
        }

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            e.setCancelled(true);

        }
    }
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Location blockLoc = e.getBlock().getLocation();
        final ShulkerClones clone = plugin.getShulkerManager().getShulkerCloneAt(blockLoc);
        final Shulker shulker = plugin.getShulkerManager().getShulkerAt(blockLoc);

        if (clone == null || shulker == null) {
            return;
        }

        Player player = e.getPlayer();

        e.setCancelled(true);

        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            e.getBlock().setType(shulker.getMaterial());
        }

        clone.setDurability(clone.getDurability() - 1);

        if (clone.getDurability() <= 0) {
            e.getBlock().setType(Material.AIR);
            shulker.dropRandomItems(clone.getLocation());
            shulker.delete(clone);
            return;
        }

        shulker.updateHologram(clone);
        if (shulker.isActionbar()) shulker.sendActionbar(clone, player);
    }
}
