package me.jetby.treexCastle;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;

@RequiredArgsConstructor
@Getter @Setter
public class ShulkerClones {
    String id;
    int durability;
    int maxDurability;
    Location location;
}
