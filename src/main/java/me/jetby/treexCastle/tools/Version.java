package me.jetby.treexCastle.tools;

import lombok.RequiredArgsConstructor;
import me.jetby.treexCastle.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Version implements Listener {
    private final Main plugin;
    private final String VERSION = "https://raw.githubusercontent.com/MrJetby/TreexEnd/refs/heads/master/VERSION";
    private final String UPDATE = "https://raw.githubusercontent.com/MrJetby/TreexEnd/refs/heads/master/UPDATE_LINK";


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission("treexcastle.version")) {
            if (!isLastVersion()) {
                   for (String string : List.of(
                           "",
                           "§7-------- §dTreexCastle §7--------",
                           "§d● §fВнимание, доступно обновление, пожалуйста обновите плагин.",
                           "§d● §7Ваша версия: §c" + getVersion() + " §7а последняя §a" + getLastVersion(),
                           "",
                           "§d● §fСкачать тут: §b" + getUPDATE(),
                           "§7-------------------------",
                           ""
                   )) {
                       player.sendMessage(string);
                   }
            }
        }
    }



    public List<String> getAlert() {
        List<String> oldVersion = new ArrayList<>(List.of(
                "",
                "§7-------- §dTreexCastle §7--------",
                "§d● §fВнимание, доступно обновление, пожалуйста обновите плагин.",
                "§d● §7Ваша версия: §c" + getVersion() + " §7а последняя §a" + getLastVersion(),
                "",
                "§d● §fСкачать тут: §b" + getUPDATE(),
                "§7-------------------------",
                ""
        ));
        List<String> lastVersion = new ArrayList<>(List.of(
                "",
                "§7-------- §dTreexCastle §7--------",
                "§d● §7Версия плагин: §c" + getVersion(),
                "",
                "§d● §aВы используете последнюю версию ✔",
                "",
                "§7-------------------------",
                ""
        ));

        if (!isLastVersion()) {
            return oldVersion;
        }
        return lastVersion;
    }

    private String getRaw(String link) {
        try {
            URL url = new URL(link);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder builder = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }
            in.close();
            return builder.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getLastVersion() {
        String result = getRaw(VERSION);
        assert result != null;
        return result;
    }
    public String getUPDATE() {
        String result = getRaw(UPDATE);
        assert result != null;
        return result;
    }

    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    public boolean isLastVersion() {
        String result = getRaw(VERSION);
        if (result == null) {
            return true;
        }

        return plugin.getDescription().getVersion().equalsIgnoreCase(getLastVersion());
    }

}
