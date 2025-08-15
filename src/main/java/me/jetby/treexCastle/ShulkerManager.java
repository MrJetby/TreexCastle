package me.jetby.treexCastle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.jetby.treexCastle.tools.Holo;
import me.jetby.treexCastle.tools.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor
public class ShulkerManager {
    private final Main plugin;
    private final Random RANDOM = new Random();

    @Getter
    private int timeToStart;

    public void runTimer() {
        int time = plugin.getCfg().getTime();
        timeToStart = time;
        Bukkit.getScheduler( ).runTaskTimer(plugin, () -> {
            if (timeToStart<=0) {
                spawnAllPossible();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (String string : plugin.getCfg().getMsg()) {
                        player.sendMessage(string);
                    }
                }

                timeToStart = time;
            }
            timeToStart--;


        }, 0L, 20L);

    }
    public void removeAllClones() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            try {
                for (Main.Clone cloneRecord : new ArrayList<>(plugin.getClones().values())) {
                    if (cloneRecord == null) continue;
                    try {
                        ShulkerClones sc = cloneRecord.shulkerClone();
                        Shulker sh = cloneRecord.shulker();
                        if (sh != null && sc != null) {
                            sh.delete(sc);
                        } else if (sc != null) {
                            if (sc.getLocation() != null && sc.getLocation().getBlock() != null) {
                                sc.getLocation().getBlock().setType(Material.AIR);
                            }
                            Holo.remove(sc.getId());
                            plugin.getLocations().reset(sc.getLocation());
                            plugin.getClones().remove(sc.getId());
                        }
                    } catch (Exception ex) {
                        Logger.error("Ошибка при удалении клона " + (cloneRecord != null ? cloneRecord.id() : "unknown") + ": " + ex.getMessage());
                    }
                }
            } catch (Exception outer) {
                Logger.error("Ошибка при массовом удалении клонов: " + outer.getMessage());
            } finally {
                plugin.getClones().clear();
            }
        });
    }
    public void spawnAllPossible() {
        if (plugin.getTypes().getShulkers().isEmpty()) return;

        for (Location location : plugin.getLocations().getLocations()) {
            if (location == null) continue;

            boolean occupied = plugin.getLocations().isOccupied(location);
            if (occupied) continue;

            String type = getRandomType();
            if (type == null) continue;

            plugin.getLocations().addLocation(location);
            plugin.getTypes().getShulkers().get(type).create(location);
        }
    }
    public String getRandomType() {
        Map<String, Shulker> types = plugin.getTypes().getShulkers();
        if (types.isEmpty()) return null;

        int total = 0;
        for (Shulker sh : types.values()) {
            total += Math.max(0, sh.getSpawnChance());
        }
        if (total <= 0) return null;

        int r = RANDOM.nextInt(total);
        int cum = 0;
        for (Map.Entry<String, Shulker> e : types.entrySet()) {
            cum += Math.max(0, e.getValue().getSpawnChance());
            if (r < cum) return e.getKey();
        }
        return null;
    }

    public ShulkerClones getShulkerCloneAt(Location location) {
        for (Main.Clone shulkerClone : plugin.getClones().values()) {
            if (shulkerClone.shulkerClone().getLocation().equals(location)) {
                return shulkerClone.shulkerClone();
            }
        }
        return null;
    }
    public Shulker getShulkerAt(Location location) {
        for (Main.Clone shulkerClone : plugin.getClones().values()) {
            if (shulkerClone.shulkerClone().getLocation().equals(location)) {
                return shulkerClone.shulker();
            }
        }
        return null;
    }

}
