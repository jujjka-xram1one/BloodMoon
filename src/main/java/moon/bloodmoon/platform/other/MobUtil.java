package moon.bloodmoon.platform.other;

import moon.bloodmoon.platform.Ability;
import moon.bloodmoon.platform.MoonMob;
import org.bukkit.entity.EntityType;

public class MobUtil {
    public boolean contains(EntityType type) {
        for (MoonMob moonMob : MoonMob.getMoonMobs()) {
            if (moonMob.getType() == type)
                return true;
        }
        return false;
    }

    public MoonMob getMob(EntityType type) {
        for (MoonMob moonMob : MoonMob.getMoonMobs()) {
            if (moonMob.getType() == type)
                return moonMob;
        }
        return null;
    }

    public Ability getAbility(MoonMob mob, Ability ability) {
        for (Ability ab : mob.getAbilitys()) {
            if (ab.name().equals(ability.name()))
                return ab;
        }
        return null;
    }

    public int getId(EntityType type) {
        switch (type) {
            case ZOMBIE:
                return 1;
            case SKELETON:
                return 2;
            case SPIDER:
                return 3;
            case CREEPER:
                return 4;
            case STRAY:
                return 5;
            case DROWNED:
                return 6;
            case PHANTOM:
                return 7;
            case HUSK:
                return 8;
        }
        return 0;
    }
}
