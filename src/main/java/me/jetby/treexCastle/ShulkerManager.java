package me.jetby.treexCastle;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor
public class ShulkerManager {
    private final Main plugin;
    private final Random RANDOM = new Random();

    public void runTimer() {
        int time = plugin.getCfg().getTime();
        Bukkit.getScheduler( ).runTaskTimerAsynchronously(plugin, () -> {
            int t = time;
            if (t<=0) {
                spawnAllPossible();
            } else {
                t = time;
            }
            t--;


        }, 0L, 20L);

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
            if (shulkerClone.clones().getLocation().equals(location)) {
                return shulkerClone.clones();
            }
        }
        return null;
    }
    public Shulker getShulkerAt(Location location) {
        for (Main.Clone shulkerClone : plugin.getClones().values()) {
            if (shulkerClone.clones().getLocation().equals(location)) {
                return shulkerClone.shulker();
            }
        }
        return null;
    }

}
