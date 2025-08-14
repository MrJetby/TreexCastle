package me.jetby.treexCastle.tools;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Holo {

    public static void create(@NotNull List<String> lines,
                              @NotNull Location location,
                              @NotNull String name){

        Hologram hologram = DHAPI.getHologram(name);
        if(hologram == null){
            DHAPI.createHologram(name, location, lines);
        }
    }
    public static void update(@NotNull List<String> lines,
                              @NotNull Location location,
                              @NotNull String name){

        Hologram hologram = DHAPI.getHologram(name);
        if(hologram != null){
            if(!hologram.getLocation().equals(location)){
                hologram.setLocation(location);
                hologram.realignLines();
            }
            DHAPI.setHologramLines(hologram, lines);
        }
    }


    public static void remove(@NotNull String name){

        Hologram hologram = DHAPI.getHologram(name);
        if(hologram != null)
            DHAPI.removeHologram(name);
    }
}
