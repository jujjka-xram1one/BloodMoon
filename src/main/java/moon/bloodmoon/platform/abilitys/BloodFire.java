package moon.bloodmoon.platform.abilitys;

import moon.bloodmoon.BloodMoon;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BloodFire  {
    private final BloodMoon instance = BloodMoon.getInstance();

    public void fire(final Player player) {
        new BukkitRunnable() {
            int tick = 0;

            @Override
            public void run() {
                if (tick < 6) {
                    player.damage(1.0D);
                    spawnLavaParticles(player);
                    tick++;
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(instance, 0L, 5L);
    }

    public void holeInput(final LivingEntity entity) {
        for (Entity nearby : entity.getNearbyEntities(20.0D, 20.0D, 20.0D)) {
            if (nearby instanceof LivingEntity) {
                nearby.teleport(entity);
            }
        }

        entity.setInvulnerable(true);

        World world = entity.getWorld();
        world.createExplosion(entity.getLocation(), 20.0F);

        new BukkitRunnable() {
            @Override
            public void run() {
                entity.setInvulnerable(false);
            }
        }.runTaskLater(instance, 20L);
    }

    private void spawnLavaParticles(Player player) {
        Location eyeLocation = player.getEyeLocation();
        Location[] locations = {
                eyeLocation.clone().add(0.0D, -1.0D, 0.0D),
                eyeLocation.clone().add(0.3D, 0.0D, 0.0D),
                eyeLocation.clone().add(0.0D, -2.0D, 0.0D)
        };

        for (Location location : locations) {
            World world = location.getWorld();
            if (world != null) {
                world.spawnParticle(Particle.LAVA, location, 5);
            }
        }
    }
}