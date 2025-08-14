package me.jetby.treexCastle;

import com.jodexindustries.jguiwrapper.common.JGuiInitializer;
import lombok.Getter;
import me.jetby.treexCastle.command.ShulkerCommand;
import me.jetby.treexCastle.configuration.Config;
import me.jetby.treexCastle.configuration.Items;
import me.jetby.treexCastle.configuration.Locations;
import me.jetby.treexCastle.configuration.Types;
import me.jetby.treexCastle.gui.MainMenu;
import me.jetby.treexCastle.handler.BlockBreak;
import me.jetby.treexCastle.handler.Wand;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class Main extends JavaPlugin {

    private Config cfg;
    private Types types;
    private Items items;
    private Locations locations;
    private MainMenu mainMenu;
    private final ShulkerManager shulkerManager = new ShulkerManager(this);

    public static final NamespacedKey NAMESPACED_KEY = new NamespacedKey("treexcastle", "wand");

    private final Map<String, Clone> clones = new HashMap<>();

    public record Clone(String id, ShulkerClones clones, Shulker shulker) {}

    @Override
    public void onEnable() {
        JGuiInitializer.init(this);

        cfg = new Config();

        items = new Items( getFile("items.yml"));
        items.load();

        types = new Types(this, new File(getDataFolder().getAbsolutePath(), "types"));
        types.load();


        locations = new Locations(this, getFile("locations.yml"), getFileConfiguration("locations.yml"));
        locations.load();
        mainMenu = new MainMenu(this);

        getCommand("shulker").setExecutor(new ShulkerCommand(this));
        getServer().getPluginManager().registerEvents(new BlockBreak(this), this);
        getServer().getPluginManager().registerEvents(new Wand(this), this);

    }

    @Override
    public void onDisable() {
        items.save();
        locations.save();
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
