package moon.bloodmoon.platform.abilitys;

import moon.bloodmoon.BloodMoon;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class BloodPulse {
    private final BloodMoon instance = BloodMoon.getInstance();
    private final Particle.DustOptions pulseEffect = new Particle.DustOptions(Color.MAROON, 3.0F);
    private final double pulseRadius = 5.0D;
    private final double verticalRadius = 3.0D;
    private final Vector pushForce = new Vector(0, 1, 0).multiply(4); // Сила толчка

    public void pulse(LivingEntity sourceEntity) {
        World world = sourceEntity.getWorld();

        world.spawnParticle(Particle.EXPLOSION_LARGE, sourceEntity.getLocation(), 4);
        sourceEntity.getNearbyEntities(pulseRadius, verticalRadius, pulseRadius).forEach(entity -> {
            Vector repelDirection = entity.getLocation().toVector()
                    .subtract(sourceEntity.getLocation().toVector())
                    .normalize()
                    .multiply(pushForce.length());

            entity.setVelocity(repelDirection);
            createPulseEffect(entity);
            
            if (entity instanceof Player) {
                Player player = (Player) entity;
                applyPlayerEffects(player);
            }
        });
    }

    private void createPulseEffect(Entity entity) {
        World world = entity.getLocation().getWorld();

        if (world == null) return;

        new BukkitRunnable() {
            int tick = 0;
            @Override
            public void run() {
                if (tick >= 3) {
                    cancel();
                    return;
                }

                world.spawnParticle(
                        Particle.REDSTONE,
                        entity.getLocation(),
                        3,
                        pulseEffect
                );
                tick++;
            }
        }.runTaskTimer(instance, 0L, 1L);
    }

    private void applyPlayerEffects(Player player) {
        // Эффект кровотечения
        new BloodEffect().giveBleeding(player, 3);

        // Применение эффектов замедления и ослепления
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 3, true, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 3, true, true));
    }
}
