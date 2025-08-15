package me.jetby.treexCastle.handler;

import lombok.RequiredArgsConstructor;
import me.jetby.treexCastle.Main;
import me.jetby.treexCastle.Shulker;
import me.jetby.treexCastle.ShulkerClones;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
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

        final ShulkerClones[] clone = {plugin.getShulkerManager().getShulkerCloneAt(e.getBlock().getLocation())};
        final Shulker[] shulker = {plugin.getShulkerManager().getShulkerAt(e.getBlock().getLocation())};

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            for (Main.Clone shulkerClone : plugin.getClones().values()) {
                if (shulkerClone.shulkerClone().getLocation().equals(e.getBlock().getLocation())) {
                    clone[0] = shulkerClone.shulkerClone();
                    shulker[0] = shulkerClone.shulker();
                    break;
                }
            }

            if (clone[0] == null || shulker[0] == null) {
                return;
            }

            if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                e.getBlock().setType(shulker[0].getMaterial());
                e.getPlayer().sendMessage("§cЛомать шалкера в креативе нельзя");
            } else {
                e.setCancelled(true);
            }

            clone[0].setDurability(clone[0].getDurability() - 1);

            if (clone[0].getDurability() <= 0) {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    e.getBlock().setType(Material.AIR);
                    shulker[0].dropRandomItems(clone[0].getLocation());
                });
                shulker[0].delete(clone[0]);
                return;
            }
            shulker[0].updateHologram(clone[0]);
            if (shulker[0].isActionbar()) shulker[0].sendActionbar(clone[0], e.getPlayer());

        });
    }
}
