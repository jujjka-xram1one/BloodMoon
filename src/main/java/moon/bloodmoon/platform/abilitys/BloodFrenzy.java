package moon.bloodmoon.platform.abilitys;

import moon.bloodmoon.BloodMoon;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class BloodFrenzy {
    public void bloodFrenzy(final LivingEntity entity) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity.getHealth() <= entity.getMaxHealth() * 0.2) {
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 1)); // Увеличение урона
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1)); // Увеличение скорости
                    entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1.0f, 1.0f);
                }
            }
        }.runTaskTimer(BloodMoon.getInstance(), 0, 20);
    }
}
