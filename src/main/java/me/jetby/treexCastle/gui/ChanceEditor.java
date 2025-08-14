package me.jetby.treexCastle.gui;

import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.gui.advanced.AdvancedGui;
import me.jetby.treexCastle.Main;
import me.jetby.treexCastle.configuration.Items;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.jetby.treexCastle.gui.MainMenu.CHANCE;

public class ChanceEditor extends AdvancedGui {
    private final Items items;
    private final String type;
    private final String inv;
    private final Map<Integer, ItemStack> originalItems = new HashMap<>();

    public ChanceEditor(Player player, String type, String inv, Main plugin) {
        super("Настроить шансы");
        this.type = type;
        this.inv = inv;
        this.items = plugin.getItems();


        List<Items.ItemsData> itemMap = items.getData().get(type);
        for (Items.ItemsData itemData : itemMap) {
            if (!itemData.inv().equals(inv)) continue;
            if (itemData.itemStack() == null) continue;

            ItemStack item = itemData.itemStack().clone();
            originalItems.put(itemData.slot(), item);

            final int[] chance = {item.getItemMeta().getPersistentDataContainer().getOrDefault(CHANCE, PersistentDataType.INTEGER, 100)};

            registerItem("slot_" + itemData.slot(), builder -> {
                builder.slots(itemData.slot())
                        .defaultItem(ItemWrapper.builder(item.getType())
                                .displayName("&#FB430A&l⭐ &fШанс: &6" + chance[0] + "%")
                                .lore(List.of(
                                        "&#FB430A&l&m=&#FB4C0E&l&m                                   &m=",
                                        "",
                                        " &#FB430A&lЛКМ &7+&f1% ",
                                        " &#FB430A&lПКМ &7-&f1% ",
                                        " &#FB430A&lSHIFT+ЛКМ &7+&f10% ",
                                        " &#FB430A&lSHIFT+ПКМ &7-&f10% ",
                                        "",
                                        "&#FB430A&l&m=&#FB4C0E&l&m                                   &m="
                                ))
                                .build());

                builder.defaultClickHandler((event, controller) -> {
                    event.setCancelled(true);
                    ClickType click = event.getClick();

                    if (click == ClickType.LEFT) chance[0] += 1;
                    else if (click == ClickType.RIGHT) chance[0] -= 1;
                    else if (click == ClickType.SHIFT_LEFT) chance[0] += 10;
                    else if (click == ClickType.SHIFT_RIGHT) chance[0] -= 10;

                    chance[0] = Math.max(0, Math.min(100, chance[0]));

                    ItemMeta meta = item.getItemMeta();
                    meta.getPersistentDataContainer().set(CHANCE, PersistentDataType.INTEGER, chance[0]);
                    item.setItemMeta(meta);

                    controller.updateItems(wrapper -> {
                        wrapper.displayName("&#FB430A&l⭐ &fШанс: &6" + chance[0] + "%");
                    });
                });
            });
        }

        onClose(event -> {
            saveChanges();
            Bukkit.getScheduler().runTaskLater(plugin, ()->{new InvEditor(plugin, player, plugin.getTypes().getShulkers().get(type)).open(player);}, 1L);
        });
    }

    private void saveChanges() {
        for (Map.Entry<Integer, ItemStack> entry : originalItems.entrySet()) {
            int slot = entry.getKey();
            ItemStack item = entry.getValue();
            int chance = item.getItemMeta().getPersistentDataContainer().getOrDefault(CHANCE, PersistentDataType.INTEGER, 100);
            items.saveItem(type, inv, item, slot, chance);
        }
    }
}