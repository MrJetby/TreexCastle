package me.jetby.treexCastle.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.jetby.treexCastle.Main;
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
    private final Set<String> occupied = new HashSet<>();
    private final Random random = new Random();

    public void load() {
        locations.clear();
        occupied.clear();

        if (!configuration.getStringList("locations").isEmpty()) {
            for (String locStr : configuration.getStringList("locations")) {
                Location loc = LocationHandler.deserialize(locStr, plugin);
                if (loc != null) locations.add(loc);
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

    public Location acquireRandomAvailableLocation() {
        List<Integer> availableIdx = new ArrayList<>();
        for (int i = 0; i < locations.size(); i++) {
            Location loc = locations.get(i);
            String key = LocationHandler.serialize(loc);
            if (!occupied.contains(key)) {
                availableIdx.add(i);
            }
        }

        if (availableIdx.isEmpty()) return null;

        int chosen = availableIdx.get(random.nextInt(availableIdx.size()));
        Location chosenLoc = locations.get(chosen);
        occupied.add(LocationHandler.serialize(chosenLoc));
        return chosenLoc;
    }

    public boolean acquire(Location location) {
        if (location == null) return false;
        String key = LocationHandler.serialize(location);
        boolean exists = false;
        for (Location loc : locations) {
            if (LocationHandler.serialize(loc).equals(key)) {
                exists = true;
                break;
            }
        }
        if (!exists) return false;
        if (occupied.contains(key)) return false;
        occupied.add(key);
        return true;
    }

    public void release(Location location) {
        if (location == null) return;
        occupied.remove(LocationHandler.serialize(location));
    }

    public boolean addLocation(Location location) {
        if (location == null) return false;
        String key = LocationHandler.serialize(location);
        for (Location loc : locations) {
            if (LocationHandler.serialize(loc).equals(key)) {
                return false;
            }
        }
        locations.add(location);
        save();
        return true;
    }

    public boolean removeLocation(Location location) {
        if (location == null) return false;
        String key = LocationHandler.serialize(location);
        Iterator<Location> it = locations.iterator();
        boolean removed = false;
        while (it.hasNext()) {
            Location loc = it.next();
            if (LocationHandler.serialize(loc).equals(key)) {
                it.remove();
                removed = true;
            }
        }
        if (removed) {
            occupied.remove(key);
            save();
        }
        return removed;
    }

    public boolean isOccupied(Location location) {
        if (location == null) return false;
        return occupied.contains(LocationHandler.serialize(location));
    }

}