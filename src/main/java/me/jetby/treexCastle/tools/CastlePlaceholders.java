package me.jetby.treexCastle.tools;

import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.jetby.treexCastle.Main;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class CastlePlaceholders extends PlaceholderExpansion {
    private final Main plugin;
    @Override
    public @NotNull String getIdentifier() {
        return "treexcastle";
    }

    @Override
    public @NotNull String getAuthor() {
        return "TreexStudio";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if (identifier.equalsIgnoreCase("time_to_start")) {
            return String.valueOf(plugin.getShulkerManager().getTimeToStart());
        }
        if (identifier.equalsIgnoreCase("time_to_start_string")) {
            return (plugin.getFormatTime( ).stringFormat(plugin.getShulkerManager().getTimeToStart()));
        }
        if (identifier.equalsIgnoreCase("time_to_start_format")) {
            return (plugin.getFormatTime( ).stringFormat(plugin.getShulkerManager().getTimeToStart()));
        }
        return identifier;
    }

}
