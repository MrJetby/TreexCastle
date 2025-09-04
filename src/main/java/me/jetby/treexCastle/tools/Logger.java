package me.jetby.treexCastle.tools;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class Logger {

    public void warn(String message) {
        Bukkit.getConsoleSender().sendMessage("§e[TreexCastle] "+ message);
    }
    public void info(String message) {
        Bukkit.getConsoleSender().sendMessage("§a[TreexCastle] §f"+ message);
    }
    public void success(String message) {
        Bukkit.getConsoleSender().sendMessage("§a[TreexCastle] §a"+ message);
    }
    public void error(String message) {
        Bukkit.getConsoleSender().sendMessage("§c[TreexCastle] "+ message);
    }
    public void msg(String message) {
        Bukkit.getConsoleSender().sendMessage("§6[TreexCastle] §f"+ message);
    }
}