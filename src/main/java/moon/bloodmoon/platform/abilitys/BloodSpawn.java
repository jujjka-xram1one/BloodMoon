package moon.bloodmoon.platform.abilitys;

import moon.bloodmoon.BloodMoon;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;

public class BloodSpawn {
    public void bloodSpawn(final Location location) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Zombie bloodMinion = location.getWorld().spawn(location, Zombie.class);
                bloodMinion.setCustomName(ChatColor.translateAlternateColorCodes('&',"§cBlood Minion"));
                bloodMinion.setCustomNameVisible(true);
                bloodMinion.setHealth(30);
                bloodMinion.getWorld().playSound(location, Sound.ENTITY_ZOMBIE_AMBIENT, 1.0f, 1.0f);
            }
        }.runTaskLater(BloodMoon.getInstance(), 0);
    }
}
