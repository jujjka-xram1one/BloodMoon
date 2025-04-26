package moon.bloodmoon.platform.abilitys;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import moon.bloodmoon.BloodMoon;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.*;

public class BloodHole {
    private final Set<UUID> holeEntities = new HashSet<>();
    private final BloodMoon instance = BloodMoon.getInstance();
    private final Particle.DustOptions particleOptions = new Particle.DustOptions(Color.MAROON, 5.0F);

    public Set<UUID> getHoleEntities() {
        return holeEntities;
    }

    public void createHole(final UUID entityUUID, final Location location) {
        if (!holeEntities.contains(entityUUID)) {
            final World world = location.getWorld();
            if (world == null) {
                Bukkit.getLogger().warning("World is null for location: " + location);
                return;
            }

            holeEntities.add(entityUUID);

            new BukkitRunnable() {
                int tick = 0;

                @Override
                public void run() {
                    if (tick >= 10) {
                        cancel();
                        holeEntities.remove(entityUUID);
                        return;
                    }

                    createParticleCircle(location, 0.5D, 10);

                    for (Player player : world.getPlayers()) {
                        if (player.getLocation().distance(location) <= 10.0D) {
                            Vector pullDirection = location.toVector().subtract(player.getLocation().toVector()).normalize();
                            player.setVelocity(pullDirection.multiply(2));
                            player.damage(1.0D);
                            processBlocks(player.getLocation(), location, 1.0D);
                        }
                    }

                    tick++;
                }
            }.runTaskTimer(instance, 0L, 10L);
        }
    }

    private void processBlocks(Location start, Location end, double step) {
        BlockIterator blockIterator = getBlockIterator(start, end, step);
        if (blockIterator == null) {
            return;
        }

        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            if (block != null) {
                Location blockLocation = block.getLocation();
                blockLocation.getWorld().spawnParticle(Particle.REDSTONE, blockLocation, 3, particleOptions);
            }
        }
    }

    private BlockIterator getBlockIterator(Location start, Location end, double step) {
        double distance = start.distance(end);
        if (distance > step) {
            Vector direction = end.toVector().subtract(start.toVector()).normalize();
            return new BlockIterator(start.getWorld(), start.toVector(), direction, 0.0D, (int) (distance / step));
        }
        return null;
    }

    private void createParticleCircle(Location center, double radius, int numParticles) {
        World world = center.getWorld();
        if (world == null) {
            Bukkit.getLogger().warning("World is null for particle circle at: " + center);
            return;
        }

        for (int i = 0; i < numParticles; i++) {
            double angle = 2 * Math.PI * i / numParticles;
            double x = center.getX() + radius * Math.cos(angle);
            double z = center.getZ() + radius * Math.sin(angle);
            Location particleLocation = new Location(world, x, center.getY(), z);
            world.spawnParticle(Particle.REDSTONE, particleLocation, 1, particleOptions);
        }
    }
}
