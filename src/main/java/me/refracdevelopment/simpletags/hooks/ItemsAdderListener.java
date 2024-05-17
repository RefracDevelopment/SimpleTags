package me.refracdevelopment.simpletags.hooks;

import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import lombok.Getter;
import me.refracdevelopment.simpletags.SimpleTags;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@Getter
public class ItemsAdderListener implements Listener {

    @EventHandler
    public void onItemsAdder(ItemsAdderLoadDataEvent event) {
        SimpleTags.getInstance().getTagManager().loadTags();
    }
}
