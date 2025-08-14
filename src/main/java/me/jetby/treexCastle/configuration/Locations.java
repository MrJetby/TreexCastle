package me.jetby.treexCastle.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.jetby.treexCastle.Main;
import me.jetby.treexCastle.Shulker;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class Locations {


    @Getter(AccessLevel.NONE) private final Main plugin;

    private final Map<Location, String> tempLocations = new HashMap<>(); //  location, shulker_name
    private final Map<String, Location> locations = new HashMap<>();

    public void load() {

    }
}
