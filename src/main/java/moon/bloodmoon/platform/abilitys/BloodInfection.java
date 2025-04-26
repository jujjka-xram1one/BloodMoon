package moon.bloodmoon.platform.abilitys;

import moon.bloodmoon.BloodMoon;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class BloodInfection {

    public void infection(final Player target){
        new BukkitRunnable(){
            int tick = 0;
            int maxTicks = 60;
            double damage = 1.0;
            @Override
            public void run() {
                if(target.isDead() || tick >= maxTicks){
                    this.cancel();
                    return;
                }
                if(target.isOnline()){
                    tick++;

                    target.damage(damage);
                    damage += 0.5;

                    int slownessLevel = Math.min(tick / 10, 4);
                    int weaknessLevel = Math.min(tick / 10, 4);

                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, slownessLevel));
                    target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, weaknessLevel));

                    target.getWorld().spawnParticle(Particle.REDSTONE, target.getLocation(), 5, 0.5, 0.5, 0.5, 0.1);

                    target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT, 1.0f, 1.0f);
                }
            }
        }.runTaskTimer(BloodMoon.getInstance(), 0, 20);  // Runs every second (20 ticks)
    }
}
