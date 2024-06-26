package me.refracdevelopment.simpletags.manager;

import me.refracdevelopment.simpletags.utilities.menu.PlayerMenuUtility;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class MenuManager {

    private final HashMap<UUID, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

    public PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;

        if (!(playerMenuUtilityMap.containsKey(p.getUniqueId()))) {
            playerMenuUtility = new PlayerMenuUtility(p);

            playerMenuUtilityMap.put(p.getUniqueId(), playerMenuUtility);

            return playerMenuUtility;
        }

        return playerMenuUtilityMap.get(p.getUniqueId());
    }

    public void remove(Player p) {
        playerMenuUtilityMap.remove(p.getUniqueId());
    }
}