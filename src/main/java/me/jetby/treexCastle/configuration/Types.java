package me.jetby.treexCastle.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.jetby.treexCastle.Main;
import me.jetby.treexCastle.tools.Logger;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class Types {


    private final Main plugin;
    private final FileConfiguration config;
    private final File file = new File(plugin.getDataFolder(), "types");

    @Getter private final Map<String, Data> types = new HashMap<>();

    public void load() {

        if (!file.exists()) {
            if (file.mkdirs( )) {
                File defaultFile = new File(file, "default.yml");
                if (!defaultFile.exists()) {
                    plugin.saveResource("types/default.yml", false);
                }
                FileConfiguration config = YamlConfiguration.loadConfiguration(defaultFile);
                loadType(config, config.getString("id", defaultFile.getName().replace(".yml", "")));
                Logger.info("Файл types/"+config.getString("id")+".yml создан");
            }
            return;
        }

        File[] files = file.listFiles();
        for (File file : files) {
            if (!file.getName().endsWith(".yml")) continue;
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            loadType(config, config.getString("id"));
            Logger.info("Файл types/"+config.getString("id")+".yml загружен");
        }
    }

    public void loadType(FileConfiguration config, String id) {

        Material material = Material.valueOf(config.getString("type", "STONE"));
        int durability = config.getInt("durability", 50);
        String lootAmount = config.getString("1");
        int spawnChance = config.getInt("spawnChance", 50);
        boolean explosion = config.getBoolean("explosion.enable", false);
        int explosionDamage = config.getInt("explosion.damage", 4);
        boolean hologram = config.getBoolean("hologram.enable", false);
        double holoX = config.getDouble("hologram.x", 0.5);
        double holoY = config.getDouble("hologram.y", 2.0);
        double holoZ = config.getDouble("hologram.z", 0.5);
        List<String> hologramLines = new ArrayList<>();
        if (!config.getStringList("holo").isEmpty()) {
            hologramLines = config.getStringList("holo");
        }
        boolean actionbar = config.getBoolean("actionbar.enable", false);
        String actionbarText = config.getString("actionbar.msg");

        List<Items.ItemsData> items = plugin.getItems().getData().get(id);

        types.put(id, new Data(id,
                material,
                durability,
                lootAmount,
                spawnChance,
                explosion, explosionDamage,
                hologram, holoX, holoY, holoZ, hologramLines,
                actionbar, actionbarText,
                items));
    }

    @Getter @Setter
    @RequiredArgsConstructor
    public class Data {
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
    }

}
