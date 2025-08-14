package me.jetby.treexCastle.gui;

import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import com.jodexindustries.jguiwrapper.gui.advanced.AdvancedGui;
import me.jetby.treexCastle.Main;
import me.jetby.treexCastle.configuration.Types;

public class MainMenu extends AdvancedGui {
    public MainMenu(Main plugin) {
        super("&0&lTreexCastle | dsc.gg/treexstudio");


        int slot = 0;
        for (String type : plugin.getTypes().getTypes().keySet()) {
            Types.Data data = plugin.getTypes().getTypes().get(type);
            int finalSlot = slot;
            registerItem(type, builder -> {
                builder.slots(finalSlot);
                builder.defaultItem(ItemWrapper.builder(data.getMaterial(), SerializerType.MINI_MESSAGE)
                        .displayName("<!i><#FB430A><bold>⭐</bold> <white>Шалкер: <yellow>" + type)
                        .lore(
                                "<!i><#FB430A><st>=                                   =",
                                "",
                                "<!i><#FB430A><b>▶</b> <white>Нажмите, чтобы настроить предметы",
                                "",
                                "<!i><#FB430A><st>=                                   =")
                        .build());

                builder.defaultClickHandler((event, controller) ->  {

                });
            });
            slot++;
        }
    }
}
