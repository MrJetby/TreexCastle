package me.jetby.treexCastle.configuration;

import lombok.RequiredArgsConstructor;
import me.jetby.treexCastle.Main;
import me.jetby.treexCastle.tools.LocationHandler;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Locations {

    private final Main plugin;
    private final File file;
    private final FileConfiguration configuration;

    private final Set<String> locationKeys = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Set<String> occupied = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private final Random random = new Random();

    public List<Location> getLocations() {
        return locationKeys.stream()
                .map(s -> LocationHandler.deserialize(s, plugin))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void load() {
        locationKeys.clear();

        ConfigurationSection sec = configuration.getConfigurationSection("locations");
        if (sec != null) {
            for (String encoded : sec.getKeys(false)) {
                try {
                    String key = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
                    locationKeys.add(key);
                } catch (IllegalArgumentException ignored) {
                }
            }
            return;
        }

        List<String> legacy = configuration.getStringList("locations");
        if (!legacy.isEmpty()) {
            locationKeys.addAll(legacy);
            save();
        }
    }

    public void save() {
        try {
            configuration.set("locations", null);

            for (String key : locationKeys) {
                String encoded = Base64.getEncoder().encodeToString(key.getBytes(StandardCharsets.UTF_8));
                configuration.set("locations." + encoded, true);
            }

            configuration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Location acquireRandomAvailableLocation() {
        List<String> available = new ArrayList<>();
        for (String key : locationKeys) {
            if (!occupied.contains(key)) available.add(key);
        }

        if (available.isEmpty()) return null;

        String chosenKey = available.get(random.nextInt(available.size()));
        Location chosenLoc = LocationHandler.deserialize(chosenKey, plugin);
        if (chosenLoc != null) {
            occupied.add(chosenKey);
        }
        return chosenLoc;
    }

    public boolean acquire(Location location) {
        if (location == null) return false;
        String key = LocationHandler.serialize(location);

        if (!locationKeys.contains(key)) return false;
        if (occupied.contains(key)) return false;

        occupied.add(key);
        return true;
    }

    public void reset(Location location) {
        if (location == null) return;
        occupied.remove(LocationHandler.serialize(location));
    }

    public boolean addLocation(Location location) {
        if (location == null) return false;
        String key = LocationHandler.serialize(location);
        boolean added = locationKeys.add(key);
        if (added) save();
        return added;
    }

    public boolean removeLocation(Location location) {
        if (location == null) return false;
        String key = LocationHandler.serialize(location);
        boolean removed = locationKeys.remove(key);
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
