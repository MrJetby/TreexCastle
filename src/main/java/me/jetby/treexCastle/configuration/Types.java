package me.jetby.treexCastle.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.jetby.treexCastle.Main;
import me.jetby.treexCastle.Shulker;
import me.jetby.treexCastle.tools.Logger;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class Types {

    private final Main plugin;
    private final File file;

    @Getter private final Map<String, Shulker> shulkers = new HashMap<>();

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
        String lootAmount = config.getString("lootAmount", "1");
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

        shulkers.put(id, new Shulker(
                plugin,
                id,
                material,
                durability,
                lootAmount,
                spawnChance,
                explosion, explosionDamage,
                hologram, holoX, holoY, holoZ, hologramLines,
                actionbar, actionbarText,
                items));
    }



}
