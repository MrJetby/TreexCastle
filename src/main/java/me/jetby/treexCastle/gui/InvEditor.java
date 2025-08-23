package me.jetby.treexCastle.gui;

import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.gui.advanced.AdvancedGui;
import me.jetby.treexCastle.Main;
import me.jetby.treexCastle.Shulker;
import me.jetby.treexCastle.configuration.Items;
import me.jetby.treexCastle.configuration.Types;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InvEditor extends AdvancedGui {


    public InvEditor(Main plugin, Player player, Shulker shulker) {
        super("Выбор инвентаря");

        String id = shulker.getId();

        Set<String> invs = new HashSet<>();
        List<Items.ItemsData> itemsData = plugin.getItems().getData().get(id);
        if (itemsData != null) {
            for (Items.ItemsData iData : itemsData) {
                invs.add(iData.inv());
            }
        }

        int slot = 0;
        for (String inv : invs) {
            int currentSlot = slot;
            registerItem(inv + "_" + slot, builder -> {
                builder.slots(currentSlot);
                builder.defaultItem(ItemWrapper.builder(Material.CHEST)
                        .lore(List.of(
                                "§6§l§m=                                   =",
                                "",
                                " §6§lЛКМ §7- §fНастроить предметы ",
                                " §6§lПКМ §7- §fНастроить шансы предметов ",
                                "",
                                "§6§l§m=                                   ="
                        ))
                        .displayName("&#FB430A&l⭐ &fИнвентарь: &e" + inv)
                        .build());

                builder.defaultClickHandler((event, controller) -> {
                    event.setCancelled(true);

                    switch (event.getClick()) {
                        case LEFT -> {
                            new ItemEditor(player, inv, id, plugin).open(player);
                        }
                        case RIGHT -> {
                            new ChanceEditor(player, id, inv, plugin).open(player);
                        }
                    }
                });
            });
            slot++;
        }

        registerItem("add_button", builder -> {
            builder.slots(53);
            builder.defaultItem(ItemWrapper.builder(Material.EMERALD)
                    .displayName("&a[+] Добавить новый инвентарь")
                    .build());

            builder.defaultClickHandler((event, controller) -> {
                event.setCancelled(true);
                String newInvName = "inv_" + System.currentTimeMillis();
                ItemStack dirt = new ItemStack(Material.DIRT);
                plugin.getItems().saveItem(id, newInvName, dirt, 0, 100);

                InvEditor newGui = new InvEditor(plugin, player, shulker);
                newGui.open(player);
            });
        });

    }
}
