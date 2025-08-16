package me.jetby.treexCastle.tools.locale;

import lombok.RequiredArgsConstructor;
import me.jetby.treexCastle.Main;
import org.bukkit.configuration.file.FileConfiguration;

@RequiredArgsConstructor
public class Lang {

    private final Main plugin;
    private final FileConfiguration configuration;

    private String lang;

    public void load() {
        lang = configuration.getString("lang", "en").toLowerCase();



    }
    public String getLang() {
        return switch (lang.toLowerCase( )) {
            case "ru", "ru_ru" -> "ru";
            case "en", "en_us" -> "en";
            case "uk", "uk_ua" -> "uk";
            case "tr", "tr_tr" -> "tr";
            case "zh", "zh_cn" -> "zh";
            default -> {
                plugin.getLogger( ).warning("Unknown language: '" + lang + "', using default 'en'.");
                yield "en";
            }
        };
    }
}
