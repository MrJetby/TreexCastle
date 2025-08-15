package me.jetby.treexCastle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.jetby.treexCastle.configuration.Items;
import me.jetby.treexCastle.tools.Holo;
import me.jetby.treexCastle.tools.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;


@Getter @Setter
@RequiredArgsConstructor
public class Shulker {
    final Main plugin;

    final String id;
    final Material material;
    final int durability;
    final String lootAmount;
    final int spawnChance;
    final boolean explosion;
    final int explosionDamage;
    final boolean hologram;
    final double holoX;
    final double holoY;
    final double holoZ;
    final List<String> hologramLines;
    final boolean actionbar;
    final String actionbarText;
    final List<Items.ItemsData> items;

    private final Random RANDOM = new Random();

    public void create() {
        Location location = plugin.getLocations().acquireRandomAvailableLocation();
        if (location == null) {
            Logger.warn("Локация не была найдена");
            return;
        }
        createAt(location);
    }

    public void create(Location location) {
        if (!plugin.getLocations().acquire(location)) {
            Logger.warn("Локация уже занята, пропуск спауна");
            return;
        }
        createAt(location);
    }

    private void createAt(Location location) {
        ShulkerClones clone = new ShulkerClones();
        clone.setId(UUID.randomUUID().toString());
        clone.setDurability(durability);
        clone.setMaxDurability(durability);

        clone.setLocation(location);

        plugin.getClones().put(clone.getId(), new Main.Clone(clone.getId(), clone, this));

        List<String> lines = new ArrayList<>(hologramLines);
        lines.replaceAll(s -> s.replace("{blocks_left}", String.valueOf(clone.getDurability())));

        Location holoLocation = clone.getLocation().clone().add(holoX, holoY, holoZ);
        Holo.create(lines, holoLocation, clone.getId());

        if (explosion) location.getWorld().createExplosion(location, explosionDamage, false, false);
        clone.getLocation().getBlock().setType(material);

    }
    public void delete(ShulkerClones clone) {
        clone.getLocation().getBlock().setType(Material.AIR);
        Holo.remove(clone.getId());
        plugin.getLocations().reset(clone.getLocation());
        plugin.getClones().remove(clone.getId());
    }


    public void dropRandomItems(Location location) {
        if (lootAmount == null) {
            Logger.warn("lootAmount is null, skipping item drop");
            return;
        }

        if (items == null || items.isEmpty()) return;

        int minLoot, maxLoot;
        try {
            if (lootAmount.contains("-")) {
                String[] parts = lootAmount.split("-");
                minLoot = Integer.parseInt(parts[0].trim());
                maxLoot = Integer.parseInt(parts[1].trim());
            } else {
                minLoot = maxLoot = Integer.parseInt(lootAmount.trim());
            }
        } catch (NumberFormatException e) {
            Logger.error("Invalid lootAmount format: " + lootAmount);
            return;
        }

        int lootToDrop = minLoot + RANDOM.nextInt((maxLoot - minLoot) + 1);

        List<ItemStack> itemsToDrop = new ArrayList<>();
        for (Items.ItemsData item : items) {
            if (RANDOM.nextInt(100) < item.chance()) {
                itemsToDrop.add(item.itemStack());
            }
        }

        if (itemsToDrop.isEmpty()) {
            Logger.warn("No items passed chance check, adding all possible items");
            for (Items.ItemsData item : items) {
                itemsToDrop.add(item.itemStack());
            }
        }

        Collections.shuffle(itemsToDrop);
        for (int i = 0; i < Math.min(lootToDrop, itemsToDrop.size()); i++) {
            location.getWorld().dropItemNaturally(location, itemsToDrop.get(i));
        }
    }
    public void updateHologram(ShulkerClones clone) {
        List<String> lines = new ArrayList<>(hologramLines);
        lines.replaceAll(s -> s.replace("{blocks_left}", String.valueOf(clone.getDurability())));

        Location holoLocation = clone.getLocation().clone().add(
                holoX,
                holoY,
                holoZ
        );

        Holo.update(lines, holoLocation, clone.getId());
    }

    public void sendActionbar(ShulkerClones clone, Player player) {
        player.sendActionBar(actionbarText
                .replace("{blocks_left}", String.valueOf(clone.getDurability())));
    }
}
