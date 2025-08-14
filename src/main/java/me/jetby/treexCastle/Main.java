package me.jetby.treexCastle;

import com.jodexindustries.jguiwrapper.common.JGuiInitializer;
import lombok.Getter;
import me.jetby.treexCastle.command.ShulkerCommand;
import me.jetby.treexCastle.configuration.Config;
import me.jetby.treexCastle.configuration.Items;
import me.jetby.treexCastle.configuration.Locations;
import me.jetby.treexCastle.configuration.Types;
import me.jetby.treexCastle.gui.MainMenu;
import me.jetby.treexCastle.handler.ShulkerBlock;
import me.jetby.treexCastle.handler.Wand;
import me.jetby.treexCastle.tools.Version;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class Main extends JavaPlugin {

    private final Config cfg = new Config(this, getFileConfiguration("config.yml"));
    private final Items items = new Items( getFile("items.yml"));
    private final Types types = new Types(this, new File(getDataFolder().getAbsolutePath(), "types"));
    private final ShulkerManager shulkerManager = new ShulkerManager(this);
    private final Locations locations = new Locations(this, getFile("locations.yml"), getFileConfiguration("locations.yml"));
    private final MainMenu mainMenu = new MainMenu(this);
    private final Version version = new Version(this);

    public static final NamespacedKey NAMESPACED_KEY = new NamespacedKey("treexcastle", "wand");

    private final Map<String, Clone> clones = new HashMap<>();

    public record Clone(String id, ShulkerClones clones, Shulker shulker) {}

    @Override
    public void onEnable() {
        JGuiInitializer.init(this);

        cfg.load();
        items.load();
        types.load();
        shulkerManager.runTimer();
        locations.load();


        getCommand("shulker").setExecutor(new ShulkerCommand(this));
        getServer().getPluginManager().registerEvents(new ShulkerBlock(this), this);
        getServer().getPluginManager().registerEvents(new Wand(this), this);
        getServer().getPluginManager().registerEvents(version, this);

        for (String string : version.getAlert()) {
            Bukkit.getConsoleSender().sendMessage(string);
        }

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
