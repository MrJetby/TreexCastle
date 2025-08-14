package me.jetby.treexCastle.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.jetby.treexCastle.Main;
import me.jetby.treexCastle.Shulker;
import me.jetby.treexCastle.tools.LocationHandler;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
public class Locations {


    private final Main plugin;
    private final File file;
    private final FileConfiguration configuration;

    @Getter
    private final List<Location> locations = new ArrayList<>();
    @Getter
    private final Map<Location, Boolean> tempLocations = new HashMap<>();

    public void load() {
        if (!configuration.getStringList("locations").isEmpty()) {
            for (String locStr : configuration.getStringList("locations")) {
                locations.add(LocationHandler.deserialize(locStr, plugin));
            }
        }
    }

    public void save() {
        try {
            List<String> locStr = new ArrayList<>();
            for (Location loc : locations) {
                locStr.add(LocationHandler.serialize(loc));
            }
            configuration.set("locations", locStr);
            configuration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Location getRandomLocation() {
        if (locations.isEmpty()) return null;

        for (Location location : locations) {
            if (tempLocations.getOrDefault(location, false)) continue;
            tempLocations.put(location, true);
            return location;
        }

        return null;

    }
}
