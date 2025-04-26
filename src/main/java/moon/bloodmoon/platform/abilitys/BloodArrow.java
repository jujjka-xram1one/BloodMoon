package moon.bloodmoon.platform.abilitys;

import moon.bloodmoon.BloodMoon;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class BloodArrow {
    private BloodMoon instance = BloodMoon.getInstance();

    public void shot(final Player player) {
        final Location before = player.getLocation();
        final Location[] locations = { before.clone().add(-1.0D, 0.0D, 0.0D), before.clone().add(0.0D, 1.0D, 0.0D), before.clone().add(0.0D, 0.0D, -2.0D), before.clone().add(-1.0D, 0.0D, -1.0D), before.clone().add(-1.0D, 0.0D, 0.0D), before.clone().add(0.0D, 1.0D, 0.0D), before.clone().add(0.0D, 0.0D, -2.0D) };
        (new BukkitRunnable() {
            int tick = 0;

            public void run() {
                if (this.tick < locations.length) {
                    player.teleport(locations[this.tick]);
                    BloodArrow.this.giveDamage(player);
                    player.sendTitle("系", "", 4, 4, 4);
                    this.tick++;
                } else {
                    player.teleport(before);
                    cancel();
                }
            }
        }).runTaskTimer((Plugin)this.instance, 0L, 5L);
    }

    public void arrow_part(final Arrow arrow) {
        (new BukkitRunnable() {
            World world = arrow.getWorld();

            public void run() {
                if (arrow.isOnGround() || arrow.isDead()) {
                    cancel();
                } else {
                    this.world.playEffect(arrow.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK, 3);
                }
            }
        }).runTaskTimer((Plugin)this.instance, 0L, 1L);
    }

    private void giveDamage(Player player) {
        player.damage(2.5D);
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20, 10, false, false, false));
    }
}
