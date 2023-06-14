package me.refracdevelopment.simpletags.manager;

import me.refracdevelopment.simpletags.utilities.menu.PlayerMenuUtility;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MenuManager {

    private final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

    public PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(p))) {

            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityMap.put(p, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(p);
        }
    }
}