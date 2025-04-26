package moon.bloodmoon.platform.abilitys;

import moon.bloodmoon.BloodMoon;
import moon.bloodmoon.platform.other.Exploder;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BloodExplosion {
    private final BloodMoon instance = BloodMoon.getInstance();

    public void explode(final LivingEntity entity) {
        if (entity == null || entity.getLocation() == null) return;

        final World world = entity.getWorld();
        final Location entityLocation = entity.getLocation();
        final double explosionRadius = 10.0D;
        final float explosionPower = 15.0F;
        final int requiredPlayers = Exploder.getNeed_Players(); // Необходимое количество игроков

        new BukkitRunnable() {
            @Override
            public void run() {
                long nearbyPlayers = world.getPlayers().stream()
                        .filter(player -> player.getLocation().distance(entityLocation) <= explosionRadius)
                        .count();

                if (nearbyPlayers >= requiredPlayers) {
                    cancel();
                    createExplosionEffect(entity, explosionPower);
                }
            }
        }.runTaskTimer(instance, 0L, 20L);
    }

    private void createExplosionEffect(final LivingEntity entity, final float explosionPower) {
        final World world = entity.getWorld();
        final Location entityLocation = entity.getLocation();

        new BukkitRunnable() {
            private double currentRadius = 1.0D;
            private final double radiusIncrement = 0.5D;
            private int ticks = 0;

            @Override
            public void run() {
                if (ticks < 10) {
                    ball(entityLocation, currentRadius, Particle.CRIT);
                    currentRadius += radiusIncrement;
                    ticks++;
                } else {
                    cancel();
                    entity.remove();
                    world.createExplosion(entityLocation.getX(), entityLocation.getY(), entityLocation.getZ(), explosionPower, false, true);
                }
            }
        }.runTaskTimer(instance, 0L, 2L); // Ускорен таймер для более плавного эффекта
    }

    private void ball(Location center, double radius, Particle particle) {
        if (center == null || center.getWorld() == null) return;

        for (double phi = 0.0D; phi <= Math.PI; phi += Math.PI / 20) {
            for (double theta = 0.0D; theta <= 2 * Math.PI; theta += Math.PI / 20) {
                double x = radius * Math.sin(phi) * Math.cos(theta);
                double y = radius * Math.cos(phi);
                double z = radius * Math.sin(phi) * Math.sin(theta);

                Location particleLocation = center.clone().add(x, y, z);
                center.getWorld().spawnParticle(particle, particleLocation, 1, 0.0D, 0.0D, 0.0D, 0.1D);
            }
        }
    }
}
