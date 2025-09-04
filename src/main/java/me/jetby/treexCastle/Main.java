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
import me.jetby.treexCastle.tools.*;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class Main extends JavaPlugin {

    private final Config cfg = new Config(this, getFileConfiguration("config.yml"));
    private final Items items = new Items( getFile("items.yml"));
    private final Types types = new Types(this, new File(getDataFolder().getAbsolutePath(), "types"));
    private final ShulkerManager shulkerManager = new ShulkerManager(this);
    private final Locations locations = new Locations(this, getFile("locations.yml"), getFileConfiguration("locations.yml"));
    private MainMenu mainMenu;
    private Version version;
    private FormatTime formatTime;
    private CastlePlaceholders castlePlaceholders;

    public static final NamespacedKey NAMESPACED_KEY = new NamespacedKey("treexcastle", "wand");

    private final Map<String, Clone> clones = new HashMap<>();

    public record Clone(String id, ShulkerClones shulkerClone, Shulker shulker) {}

    @Override
    public void onEnable() {
        JGuiInitializer.init(this);


        new Metrics(this, 24879);
        cfg.load();

        formatTime = new FormatTime(this);

        items.load();
        types.load();
        shulkerManager.runTimer();
        locations.load();

        mainMenu = new MainMenu(this);


        getCommand("shulker").setExecutor(new ShulkerCommand(this));
        getServer().getPluginManager().registerEvents(new ShulkerBlock(this), this);
        getServer().getPluginManager().registerEvents(new Wand(this), this);

        if (cfg.isUpdateChecker()) {
            version = new Version(this);
            getServer().getPluginManager().registerEvents(version, this);
            for (String string : version.getAlert()) {
                Bukkit.getConsoleSender().sendMessage(string);
            }
        }

        if (getServer().getPluginManager().getPlugin("DecentHolograms") == null) {
            Logger.error("DecentHolograms не был найден, плагин не может без него работать!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            castlePlaceholders = new CastlePlaceholders(this);
            castlePlaceholders.register();
            Logger.info("Плейсхлодеры успешно зарегистрированы.");
        } else {
            Logger.error("PlaceholderAPI не был найден, поэтому плейсхолдеры будут не доступны!");
        }

    }

    @Override
    public void onDisable() {
        for (Clone cloneRecord : new ArrayList<>(clones.values())) {
            if (cloneRecord == null) continue;
            try {
                ShulkerClones sc = cloneRecord.shulkerClone();
                Shulker sh = cloneRecord.shulker();
                if (sh != null && sc != null) {
                    sh.delete(sc);
                } else if (sc != null) {
                    if (sc.getLocation( ) != null) {
                        sc.getLocation( ).getBlock( );
                        sc.getLocation( ).getBlock( ).setType(org.bukkit.Material.AIR);
                    }
                    Holo.remove(sc.getId());
                    locations.reset(sc.getLocation());
                    clones.remove(sc.getId());
                }
            } catch (Exception ex) {
                Logger.error("Ошибка при удалении клона " + cloneRecord.id() + ": " + ex.getMessage());
            }
        }
        items.save();
        locations.save();
        if (castlePlaceholders != null) {
            castlePlaceholders.unregister();
        }
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
