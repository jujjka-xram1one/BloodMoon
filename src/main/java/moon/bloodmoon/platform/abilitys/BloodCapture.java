package moon.bloodmoon.platform.abilitys;

import java.util.ArrayList;
import java.util.List;
import moon.bloodmoon.BloodMoon;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BloodCapture {
    private static final List<Player> capture = new ArrayList<>();
    private final BloodMoon instance = BloodMoon.getInstance();

    public void capture(final LivingEntity entity, final Player player) {
        if (entity == null || player == null || !player.isOnline() || player.isDead()) return;

        if (!capture.contains(player)) {
            capture.add(player);

            new BukkitRunnable() {
                private int tick = 0;
                private final int maxTicks = 50;

                @Override
                public void run() {
                    if (tick >= maxTicks || entity.isDead() || !player.isOnline() || player.isDead()) {
                        finishCapture(player);
                        cancel();
                        return;
                    }

                    try {
                        Location entityLocation = entity.getLocation();
                        if (entityLocation == null || !Float.isFinite(entityLocation.getPitch())) {
                            finishCapture(player);
                            cancel();
                            return;
                        }

                        player.teleport(entityLocation);
                        player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, player.getLocation(), 10);
                        tick++;
                    } catch (Exception e) {
                        finishCapture(player);
                        cancel();
                        e.printStackTrace();
                    }
                }
            }.runTaskTimer(instance, 0L, 2L);
        }
    }

    private void finishCapture(Player player) {

        if (capture.contains(player)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    capture.remove(player);
                }
            }.runTaskLater(instance, 20L);
        }
    }
}
