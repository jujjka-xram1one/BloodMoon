package moon.bloodmoon.listeners;

import moon.bloodmoon.platform.abilitys.BloodSwamp;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    @EventHandler
    public void move(PlayerMoveEvent moveEvent) {
        Player player = moveEvent.getPlayer();
        if (!BloodSwamp.getPlayers_Swamp().contains(player))
            return;
        moveEvent.setCancelled(true);
    }
}
