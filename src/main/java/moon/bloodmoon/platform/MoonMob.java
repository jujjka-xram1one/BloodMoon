package moon.bloodmoon.platform;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.util.MythicUtil;
import moon.bloodmoon.BloodMoon;
import moon.bloodmoon.platform.abilitys.BloodFog;
import moon.bloodmoon.platform.other.BloodDrop;
import moon.bloodmoon.platform.other.MoonKeys;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MoonMob {
    public MoonMob(String DisplayName, boolean nameVisible, int health, int damage, double speed, double attack_speed, double chance, Weapon weapon, List<Ability> abilitys, List<BloodDrop> drop, String mob, List<PotionEffectType> effectTypes, String mythic) {
        this.DisplayName = DisplayName;
        this.nameVisible = nameVisible;
        this.health = health;
        this.damage = damage;
        this.speed = speed;
        this.attack_speed = attack_speed;
        this.chance = chance;
        this.weapon = weapon;
        this.abilitys = abilitys;
        this.drop = drop;
        this.mob = mob;
        this.effectTypes = effectTypes;
        this.mythic = mythic;
    }

    public void setEntity(LivingEntity entity) {
        this.entity = entity;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }


    protected boolean canEqual(Object other) {
        return other instanceof MoonMob;
    }

    public String toString() {
        return "MoonMob(DisplayName=" + getDisplayName() + ", nameVisible=" + isNameVisible() + ", health=" + getHealth() + ", damage=" + getDamage() + ", speed=" + getSpeed() + ", attack_speed=" + getAttack_speed() + ", chance=" + getChance() + ", weapon=" + getWeapon() + ", abilitys=" + getAbilitys() + ", drop=" + getDrop() + ", mob=" + getMob() + ", effectTypes=" + getEffectTypes() + ", mythic=" + getMythic() + ", entity=" + getEntity() + ", uuid=" + getUuid() + ", type=" + getType() + ")";
    }

    public static List<MoonMob> getMoonMobs() {
        return moonMobs;
    }

    public static void setMoonMobs(List<MoonMob> moonMobs) {
        MoonMob.moonMobs = moonMobs;
    }

    private static List<MoonMob> moonMobs = new ArrayList<>();

    public static List<LivingEntity> getMobs() {
        return mobs;
    }

    private static List<LivingEntity> mobs = new ArrayList<>();

    public static List<Location> getLocations() {
        return locations;
    }

    private static List<Location> locations = new ArrayList<>();

    public static List<UUID> getUuids() {
        return uuids;
    }

    private static List<UUID> uuids = new ArrayList<>();

    private final String DisplayName;

    private final boolean nameVisible;

    private final int health;

    private final int damage;

    private final double speed;

    private final double attack_speed;

    private final double chance;

    private final Weapon weapon;

    private final List<Ability> abilitys;

    private final List<BloodDrop> drop;

    private final String mob;

    private final List<PotionEffectType> effectTypes;

    private final String mythic;

    private LivingEntity entity;

    private UUID uuid;

    private EntityType type;

    public String getDisplayName() {
        return this.DisplayName;
    }

    public boolean isNameVisible() {
        return this.nameVisible;
    }

    public int getHealth() {
        return this.health;
    }

    public int getDamage() {
        return this.damage;
    }

    public double getSpeed() {
        return this.speed;
    }

    public double getAttack_speed() {
        return this.attack_speed;
    }

    public double getChance() {
        return this.chance;
    }

    public Weapon getWeapon() {
        return this.weapon;
    }

    public List<Ability> getAbilitys() {
        return this.abilitys;
    }

    public List<BloodDrop> getDrop() {
        return this.drop;
    }

    public String getMob() {
        return this.mob;
    }

    public List<PotionEffectType> getEffectTypes() {
        return this.effectTypes;
    }

    public String getMythic() {
        return this.mythic;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public EntityType getType() {
        return this.type;
    }

    public void spawn(Location location) {
        if (location == null || location.getWorld() == null)
            throw new IllegalArgumentException("Location or World is null");
        getLocations().add(location);
        LivingEntity living = null;
        Entity entity = null;
        if (this.mythic != null && !this.mythic.isEmpty()) {
                  MythicMob mythicMob = MythicMobs.inst().getMobManager().getMythicMob(this.mythic);
               if (mythicMob != null) {
                 ActiveMob activeMob = mythicMob.spawn(BukkitAdapter.adapt(location), 1.0D);
               if (activeMob != null) {
                  entity = activeMob.getEntity().getBukkitEntity();
                 living = (LivingEntity)entity;
               }
            }
         }

        if (entity == null)
            try {
                if (getType() == EntityType.DROWNED) {
                    entity = location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
                } else if (getType() == EntityType.STRAY){
                    entity = location.getWorld().spawnEntity(location, EntityType.SKELETON);
                } else {
                    EntityType type = getType();
                    if (type == null || !EnumSet.<EntityType>allOf(EntityType.class).contains(type))
                        return;
                    entity = location.getWorld().spawnEntity(location, type);
                }
                living = (LivingEntity)entity;
            } catch (Exception e) {
                return;
            }
        if (living != null) {
            living.setCustomName(mName(getDisplayName()));
            living.setCustomNameVisible(isNameVisible());
            living.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(getDamage());
            living.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getHealth());
            living.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getSpeed());
            if (living.getAttribute(Attribute.GENERIC_ATTACK_SPEED) != null)
                living.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(getAttack_speed());
            living.setHealth(getHealth());
            living.getEquipment().setItemInMainHand(getWeapon().getHand());
            for (PotionEffectType ty : this.effectTypes)
                living.addPotionEffect(new PotionEffect(ty, 72000, BloodMoon.getInstance().getConfig().getInt("powerMobEffect")));
        }
        UUID u = UUID.randomUUID();
        while (getUuids().contains(u))
            u = UUID.randomUUID();
        getUuids().add(u);
        if(mythic == null) {
            living.getPersistentDataContainer().set(MoonKeys.keyU, PersistentDataType.STRING, u.toString());
        }
        setUuid(u);
        setEntity(living);
        getMobs().add(living);
        if (getAbilitys().contains(Ability.BLOOD_FOG))
            (new BloodFog()).start(getEntity());
    }

    private String mName(String name) {
        return ChatColor.translateAlternateColorCodes('&', name);
    }

    public MoonMob clone() {
        try {
            return (MoonMob)super.clone();
        } catch (CloneNotSupportedException var2) {
            throw new Error(var2);
        }
    }

    public enum mobs {
        ZOMBIE, SKELETON, SPIDER, CREEPER, PHANTOM, ENDERMAN, EXPLODER, DROWNED, STRAY, HUSK, WITCH, SLIME, CAVE_SPIDER;
    }
}
