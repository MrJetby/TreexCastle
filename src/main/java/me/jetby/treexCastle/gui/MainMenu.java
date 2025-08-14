package me.jetby.treexCastle.gui;

import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import com.jodexindustries.jguiwrapper.gui.advanced.AdvancedGui;
import me.jetby.treexCastle.Main;
import me.jetby.treexCastle.Shulker;
import me.jetby.treexCastle.configuration.Types;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public class MainMenu extends AdvancedGui {
    public static final NamespacedKey CHANCE = new NamespacedKey("vulcanevent", "chance");
    public MainMenu(Main plugin) {

        super("&0&lTreexCastle | dsc.gg/treexstudio");


        int slot = 0;
        for (String type : plugin.getTypes().getTypes().keySet()) {
            Shulker shulker = plugin.getTypes().getTypes().get(type);
            int finalSlot = slot;
            registerItem(type, builder -> {
                builder.slots(finalSlot);
                builder.defaultItem(ItemWrapper.builder(shulker.getMaterial(), SerializerType.MINI_MESSAGE)
                        .displayName("<!i><#FB430A><bold>⭐</bold> <white>Шалкер: <yellow>" + type)
                        .lore(
                                "<!i><#FB430A><st>=                                   =",
                                "",
                                "<!i><#FB430A><b>▶</b> <white>Нажмите, чтобы настроить предметы",
                                "",
                                "<!i><#FB430A><st>=                                   =")
                        .build());

                builder.defaultClickHandler((event, controller) ->  {
                    event.setCancelled(true);

                    if (event.getWhoClicked() instanceof Player player) {
                        new InvEditor(plugin, player, shulker).open(player);
                    }
                });
            });
            slot++;
        }
    }
}
