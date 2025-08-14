package me.jetby.treexCastle.tools;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class Logger {

    public void warn(String message) {
        Bukkit.getConsoleSender().sendMessage("§7[§eTreexCastle§7] §e"+ message);
    }
    public void info(String message) {
        Bukkit.getConsoleSender().sendMessage("§7[§aTreexCastle§7] §f"+ message);
    }
    public void success(String message) {
        Bukkit.getConsoleSender().sendMessage("§7[§aTreexCastle§7] §a"+ message);
    }
    public void error(String message) {
        Bukkit.getConsoleSender().sendMessage("§7[§cTreexCastle§7] §c"+ message);
    }
    public void msg(String message) {
        Bukkit.getConsoleSender().sendMessage("§7[§6TreexCastle§7] §f"+ message);
    }
}