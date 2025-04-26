package moon.bloodmoon.platform.abilitys;

import java.util.List;
import java.util.stream.Collectors;

import moon.bloodmoon.BloodMoon;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class BloodDome {
    private BloodMoon instance = BloodMoon.getInstance();

    public void aur(final Location location, double maxRadius, double expandSpeed, int durationTicks, boolean isDefensive) {
        final int[] ticksRemaining = { durationTicks };
        new BukkitRunnable() {
            double currentRadius = 1.0D; 

            public void run() {
                if (currentRadius >= maxRadius || ticksRemaining[0]-- <= 0) { // Уменьшаем ticksRemaining[0]
                    cancel();
                    return;
                }

                currentRadius += expandSpeed;
                share(location, currentRadius, isDefensive);
            }
        }.runTaskTimer(instance, 0L, 1L);
    }

    private void share(Location center, double radius, boolean isDefensive) {
        double phi;
        for (phi = 0.0D; phi <= Math.PI; phi += Math.PI / 20) {
            double theta;
            for (theta = 0.0D; theta <= 2 * Math.PI; theta += Math.PI / 20) {
                double x = radius * Math.sin(phi) * Math.cos(theta);
                double y = radius * Math.cos(phi);
                double z = radius * Math.sin(phi) * Math.sin(theta);
                Location particleLocation = center.clone().add(x, y, z);

                float colorIntensity = (float) (Math.random() * 0.5 + 0.5);
                Particle.DustOptions options = new Particle.DustOptions(Color.fromRGB(128, 0, 0), colorIntensity);

                center.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 0, 0.1, 0.1, 0.1, 0.1, options);
            }
        }

        for (Player player : getPlayers(center, radius)) {
            if (isDefensive) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 1));
            } else {
                if (!BloodEffect.getPlayers_Bleeding().contains(player)) {
                    new BloodEffect().giveBleeding(player, 3);
                }
                player.damage(1.0D);
            }
        }
    }

    private List<Player> getPlayers(Location location, double radius) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getLocation().distanceSquared(location) <= radius * radius).collect(Collectors.toList());
    }
}
