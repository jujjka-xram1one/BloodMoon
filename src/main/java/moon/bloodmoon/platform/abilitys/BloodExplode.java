package moon.bloodmoon.platform.abilitys;

import moon.bloodmoon.BloodMoon;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BloodExplode {
    private BloodMoon instance = BloodMoon.getInstance();

    public void explode(final Location l) {
        (new BukkitRunnable() {
            double radiusCur = 1.0D;

            double radiusAdd = 0.5D;

            int tick = 0;

            public void run() {
                this.tick++;
                if (this.tick != 3) {
                    Block block = l.getBlock();
                    this.radiusCur += this.radiusAdd;
                    BloodExplode.this.spawnBall(block.getLocation(), this.radiusCur);
                } else {
                    cancel();
                    (new BukkitRunnable() {
                        public void run() {
                            World world = l.getWorld();
                            world.createExplosion(l.getX(), l.getY(), l.getZ(), 7.0F, false, true);
                        }
                    }).runTaskLater((Plugin)BloodExplode.this.instance, 5L);
                }
            }
        }).runTaskTimer((Plugin)this.instance, 0L, 1L);
    }

    private void spawnBall(Location center, double radius) {
        double phi;
        for (phi = 0.0D; phi <= Math.PI; phi += 0.15707963267948966D) {
            double theta;
            for (theta = 0.0D; theta <= 6.283185307179586D; theta += 0.15707963267948966D) {
                double x = radius * Math.sin(phi) * Math.cos(theta);
                double y = radius * Math.cos(phi);
                double z = radius * Math.sin(phi) * Math.sin(theta);
                Location particleLocation = center.clone().add(x, y, z);
                double xd = 0.1D;
                double yd = 0.1D;
                double zd = 0.1D;
                float speed = 0.1F;
                Particle.DustOptions options = new Particle.DustOptions(Color.MAROON, 1.0F);
                center.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 0, xd, yd, zd, speed, options);
            }
        }
    }
}
