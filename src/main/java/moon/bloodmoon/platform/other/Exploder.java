package moon.bloodmoon.platform.other;

import moon.bloodmoon.BloodMoon;
import moon.bloodmoon.platform.abilitys.BloodExplosion;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wolf;

public class Exploder {
    public static double getChance() {
        return chance;
    }

    private static double chance = BloodMoon.getInstance().getConfig().getDouble("EXPLODER.chance_spawn");

    public static int getNeed_Players() {
        return need_Players;
    }

    private static int need_Players = BloodMoon.getInstance().getConfig().getInt("EXPLODER.needPlayers");

    public Exploder(Location location) {
        Wolf wolf = (Wolf)location.getWorld().spawnEntity(location, EntityType.WOLF);
        wolf.setAngry(true);
        wolf.setCollarColor(DyeColor.BLACK);
        (new BloodExplosion()).explode((LivingEntity)wolf);
    }
}
