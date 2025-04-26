package moon.bloodmoon.platform.abilitys;

import moon.bloodmoon.BloodMoon;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class BloodCurse {
    public void bloodCurse(final Player target) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (target.isDead()) {
                    this.cancel();
                    return;
                }

                target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 10, 1)); // Слабое проклятие
                target.getWorld().spawnParticle(org.bukkit.Particle.REDSTONE, target.getLocation(), 30, 0.5, 0.5, 0.5, 0.1);
                target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1.0f, 1.0f);
            }
        }.runTaskLater(BloodMoon.getInstance(), 0);
    }
}
