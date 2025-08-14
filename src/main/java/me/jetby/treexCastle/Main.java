package me.jetby.treexCastle;

import lombok.Getter;
import me.jetby.treexCastle.configuration.Config;
import me.jetby.treexCastle.configuration.Items;
import me.jetby.treexCastle.configuration.Types;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public final class Main extends JavaPlugin {

    private Config cfg;
    private Types types;
    private Items items;

    @Override
    public void onEnable() {
        items = new Items(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public FileConfiguration getFileConfiguration(String fileName) {
        File file = new File(getDataFolder().getAbsolutePath(), fileName);
        if (!file.exists()) {
            saveResource(fileName, false);
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public File getFile(String fileName) {
        File file = new File(getDataFolder().getAbsolutePath(), fileName);
        if (!file.exists()) {
            saveResource(fileName, false);
        }
        return file;
    }

}
