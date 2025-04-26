package moon.bloodmoon.platform.other;

import java.util.ArrayList;
import java.util.List;
import moon.bloodmoon.BloodMoon;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BloodFog {
    private final World world;
    private final LivingEntity entity;
    private List<Player> players;
    private List<Player> fogPlayers;
    private int taskId;

    public static int active = BloodMoon.getInstance().getConfig().getInt("FOG.active");
    public static int timeOut = BloodMoon.getInstance().getConfig().getInt("FOG.timeOut");
    private static List<BloodFog> fogList = new ArrayList<>();

    public BloodFog(World world, LivingEntity entity) {
        this.world = world;
        this.entity = entity;
        this.fogPlayers = new ArrayList<>();
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setFogPlayers(List<Player> fogPlayers) {
        this.fogPlayers = fogPlayers;
    }

    public World getWorld() {
        return world;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getTaskId() {
        return taskId;
    }

    public static List<BloodFog> getFogList() {
        return fogList;
    }

    public List<Player> getFogPlayers() {
        return fogPlayers;
    }

    public void start() {
        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) BloodMoon.getInstance(), () -> {
            for (Player player : this.world.getPlayers()) {
                if (this.entity.getLocation().distance(player.getLocation()) <= 6.0D) {
                    this.fogPlayers.add(player);
                    if (haveOnly(player, this)) {
                        if (this.entity.getLocation().distance(player.getLocation()) > 5.0D) {
                            pentacle(player);
                        } else {
                            pentacleSuper(player);
                        }
                    }
                } else {
                    this.fogPlayers.remove(player);
                }
            }
            spawnParticleSphere(this.entity.getLocation());
        }, 0L, 10L);
    }

    public void delete() {
        Bukkit.getScheduler().cancelTask(this.taskId);
    }

    private void spawnParticleSphere(final Location center) {
        new BukkitRunnable() {
            int currentRadius = 1;

            @Override
            public void run() {
                if (currentRadius <= 5) {
                    spawnParticlesInCircle(center, currentRadius);
                    currentRadius++;
                } else {
                    cancel();
                }
            }
        }.runTaskTimer((Plugin) BloodMoon.getInstance(), 0L, 1L);
    }

    private void spawnParticlesInCircle(final Location center, final int radius) {
        new BukkitRunnable() {
            double theta = 0.0D;

            @Override
            public void run() {
                theta += 0.39269908169872414D;
                if (theta > 6.283185307179586D) {
                    cancel();
                    return;
                }
                double x = radius * Math.cos(theta);
                double z = radius * Math.sin(theta);
                Particle.DustOptions options = new Particle.DustOptions(Color.MAROON, 3.0F);
                center.getWorld().spawnParticle(Particle.REDSTONE, center.getX() + x, center.getY(), center.getZ() + z, 1, 0.0D, 0.0D, 0.0D, 1.0D, options);
            }
        }.runTaskTimer((Plugin) BloodMoon.getInstance(), 0L, 1L);
    }

    private void pentacle(Player player) {
        player.damage(1.0D);
    }

    private void pentacleSuper(Player player) {
        player.damage(2.0D);
    }

    private boolean haveOnly(Player player, BloodFog fog) {
        for (BloodFog f : getFogList()) {
            if (f != fog) {
                for (Player p : f.getFogPlayers()) {
                    if (p == player) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
