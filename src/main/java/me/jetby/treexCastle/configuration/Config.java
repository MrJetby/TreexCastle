package me.jetby.treexCastle.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.jetby.treexCastle.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

@RequiredArgsConstructor
@Getter @Setter
public class Config {


        private final Main plugin;
        private final FileConfiguration configuration;

        private String license;
        private boolean bStats;
        private boolean updateChecker;
        private boolean update;
        private int time;
        private List<String> msg;

        public void load() {
            license = configuration.getString("license.key", "NONE");
            update = configuration.getBoolean("update.enable", false);
            time = configuration.getInt("update.time", 1800);
            msg = configuration.getStringList("update.time");

            bStats = configuration.getBoolean("bStats", true);
            updateChecker = configuration.getBoolean("update-checker", true);
        }
    }
