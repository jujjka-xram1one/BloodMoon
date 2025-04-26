package moon.bloodmoon.platform.abilitys;

import java.util.ArrayList;
import java.util.List;
import moon.bloodmoon.BloodMoon;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BloodEffect {
    public static List<Player> getPlayers_Bleeding() {
        return players_Bleeding;
    }

    private static List<Player> players_Bleeding = new ArrayList<>();

    private BloodMoon instance = BloodMoon.getInstance();

    public void giveBleeding(final Player player, final int time) {
        getPlayers_Bleeding().add(player);
        (new BukkitRunnable() {
            int tick = 0;

            public void run() {
                if (this.tick < time && player.isOnline()) {
                    if (player.getHealth() > 0.0D) {
                        player.damage(2.5D);
                    } else {
                        BloodEffect.getPlayers_Bleeding().remove(player);
                        cancel();
                    }
                } else {
                    BloodEffect.getPlayers_Bleeding().remove(player);
                    cancel();
                }
                this.tick++;
            }
        }).runTaskTimer((Plugin)this.instance, 0L, 20L);
    }
}
