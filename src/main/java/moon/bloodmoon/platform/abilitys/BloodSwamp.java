package moon.bloodmoon.platform.abilitys;

import java.util.ArrayList;
import java.util.List;
import moon.bloodmoon.BloodMoon;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class BloodSwamp {
    private static final List<Player> players_Swamp = new ArrayList<>();
    private final BloodMoon instance = BloodMoon.getInstance();

    public static List<Player> getPlayers_Swamp() {
        return players_Swamp;
    }

    public void tune(final Player player) {
        if (player == null || !player.isOnline()) return;

        try {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 3)); // Более длительный эффект
            players_Swamp.add(player);

            new BukkitRunnable() {
                private int tick = 0;
                private final double shift = 0.06D;

                @Override
                public void run() {
                    if (!player.isOnline() || player.isDead() || !players_Swamp.contains(player)) {
                        cancel();
                        players_Swamp.remove(player);
                        return;
                    }

                    if (tick < 20) {
                        Location location = player.getLocation();
                        location.setY(location.getY() - shift);
                        player.teleport(location);
                        tick++;
                    } else {
                        cancel();
                        players_Swamp.remove(player);
                    }
                }
            }.runTaskTimer(instance, 0L, 1L);

        } catch (Exception e) {
            players_Swamp.remove(player); // Убираем игрока в случае ошибки
            e.printStackTrace();
        }
    }
}
