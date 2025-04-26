package moon.bloodmoon.platform.abilitys;

import moon.bloodmoon.BloodMoon;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BloodFog {
    private BloodMoon instance = BloodMoon.getInstance();

    public void start(final LivingEntity entity) {
        try {
            final moon.bloodmoon.platform.other.BloodFog fog = new moon.bloodmoon.platform.other.BloodFog(entity.getWorld(), entity);
            fog.start();
            (new BukkitRunnable() {
                int tick = 0;

                public void run() {
                    if (this.tick >= 60 || entity.isDead()) {
                        fog.delete();
                        cancel();
                        (new BukkitRunnable() {
                            public void run() {
                                if (!entity.isDead())
                                    BloodFog.this.start(entity);
                            }
                        }).runTaskLater((Plugin)BloodFog.this.instance, 1200L);
                    }
                    this.tick++;
                }
            }).runTaskTimer((Plugin)this.instance, 0L, 20L);
        } catch (Throwable $ex) {
            throw $ex;
        }
    }
}
