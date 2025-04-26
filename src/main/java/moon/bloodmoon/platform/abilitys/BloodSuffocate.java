package moon.bloodmoon.platform.abilitys;

import moon.bloodmoon.BloodMoon;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class BloodSuffocate {
    public void bloodSuffocate(final Player target) {
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (target.isDead() || ticks >= 100) { // 5 секунд
                    this.cancel();
                    return;
                }
                if (target.isOnline()) {
                    // Наносим урон
                    target.damage(0.5);
                    // Снижаем здоровье
                    target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20, 1)); // Отравление
                    target.getWorld().spawnParticle(org.bukkit.Particle.REDSTONE, target.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);
                    target.getWorld().playSound(target.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1.0f, 1.0f);
                }
                ticks++;
            }
        }.runTaskTimer(BloodMoon.getInstance(), 0, 20);
    }
}
