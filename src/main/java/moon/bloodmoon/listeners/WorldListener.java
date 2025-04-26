package moon.bloodmoon.listeners;

import java.util.Random;
import moon.bloodmoon.BloodMoon;
import moon.bloodmoon.MoonUniverse;
import moon.bloodmoon.items.BloodPearl;
import moon.bloodmoon.platform.Ability;
import moon.bloodmoon.platform.MoonMob;
import moon.bloodmoon.platform.abilitys.*;
import moon.bloodmoon.platform.other.BloodDrop;
import moon.bloodmoon.platform.other.Exploder;
import moon.bloodmoon.platform.other.MobUtil;
import moon.bloodmoon.platform.other.MoonKeys;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class WorldListener implements Listener {
    public static boolean isDeleteMobs;

    private final MobUtil mobUtil = new MobUtil();

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        World world = entity.getWorld();
        EntityType type = entity.getType();
        if (!MoonUniverse.getWorldMoons().contains(world))
            return;
        if (!BloodMoon.getInstance().getConfig().getBoolean("spawnerSpawn") &&
                fromSpawn(entity.getLocation())) {
            event.setCancelled(true);
            return;
        }
        if (type == EntityType.WOLF && MoonUniverse.randomChance(Exploder.getChance())) {
            new Exploder(event.getLocation());
            return;
        }
        if (type == EntityType.WITCH) {
            event.setCancelled(true);
            return;
        }
        if (MoonMob.getLocations().contains(event.getLocation())) {
            return;
        }

        if (!this.mobUtil.contains(type) || type == EntityType.STRAY || type == EntityType.HUSK) {
            if (type == EntityType.ZOMBIE) {
                MoonMob moonMob = this.mobUtil.getMob(EntityType.ZOMBIE);
                if (moonMob != null && MoonUniverse.randomChance(moonMob.getChance())) {
                    moonMob.spawn(event.getLocation());
                    event.setCancelled(true);
                }
            } else if (type == EntityType.STRAY || type == EntityType.HUSK || type == EntityType.DROWNED) {
                MoonMob moonMob = this.mobUtil.getMob(type);
                if (moonMob != null && MoonUniverse.randomChance(moonMob.getChance())) {
                    moonMob.spawn(event.getLocation());
                    event.setCancelled(true);
                }
            }
            return;
        }
        event.setCancelled(true);
        MoonMob mob = this.mobUtil.getMob(type);
        if (mob != null && MoonUniverse.randomChance(mob.getChance()))
            mob.spawn(event.getLocation());
    }

    @EventHandler
    public void playerDamageEvent(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            return;
        }
        if (!BloodMoon.getInstance().getConfig().isBoolean("pvp")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageTwoEvent(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity))
            return;
        if (!MoonUniverse.getWorldMoons().contains(entity.getWorld()))
            return;
        EntityType type = entity.getType();
        if (!this.mobUtil.contains(type))
            return;
        LivingEntity e = (LivingEntity) entity;
        if ((new Random()).nextDouble() <= BloodMoon.getInstance().getConfig().getDouble("chanceLastHealth")) {
            e.setHealth(e.getMaxHealth());
            event.getEntity().getWorld().strikeLightningEffect(entity.getLocation());
            for (Entity e1 : entity.getNearbyEntities(5.0D, 5.0D, 5.0D)) {
                if (e1 instanceof LivingEntity) {
                    Location playerLocation = e1.getLocation();
                    Vector direction = playerLocation.toVector().subtract(entity.getLocation().toVector()).normalize();
                    direction.multiply(1);
                    direction.setY(direction.getY() + 0.5D);
                    e1.setVelocity(direction);
                }
            }
            return;
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();
        if (!(entity instanceof Player) || damager instanceof Player)
            return;
        if (!MoonUniverse.getWorldMoons().contains(entity.getWorld()))
            return;
        EntityType type = damager.getType();
        if (!this.mobUtil.contains(type))
            return;
        NamespacedKey key = new NamespacedKey((Plugin) BloodMoon.getInstance(), "moonId");
        if (!damager.getPersistentDataContainer().has(key, PersistentDataType.INTEGER))
            return;
        Player player = (Player) entity;
        int moonId = ((Integer) damager.getPersistentDataContainer().get(key, PersistentDataType.INTEGER)).intValue();
        MoonMob mob = this.mobUtil.getMob(type);
        if (moonId == 6)
            mob = this.mobUtil.getMob(EntityType.DROWNED);
        if (mob != null && !mob.getAbilitys().isEmpty())
            for (Ability ability : mob.getAbilitys()) {
                if (MoonUniverse.randomChance(ability.getChance()))
                    activateAbility(mob, ability, (LivingEntity) damager, player);
            }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntityType() != EntityType.CREEPER)
            return;
        LivingEntity entity = (LivingEntity) event.getEntity();
        if (!MoonUniverse.getWorldMoons().contains(entity.getWorld()))
            return;
        MoonMob mob = this.mobUtil.getMob(EntityType.CREEPER);
        if (mob != null && mob.getAbilitys().contains(Ability.BLOOD_EXPLOSION) && MoonUniverse.randomChance(this.mobUtil.getAbility(mob, Ability.BLOOD_EXPLOSION).getChance())) {
            event.setCancelled(true);
            (new BloodExplode()).explode(event.getLocation());
        }
    }

    @EventHandler
    public void slimeBan(SlimeSplitEvent event) {
        World world = event.getEntity().getWorld();
        if (MoonUniverse.getWorldMoons().contains(world)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();
        EntityType type = livingEntity.getType();
        if (this.mobUtil.getMob(type) == null || !livingEntity.getPersistentDataContainer().has(MoonKeys.key, PersistentDataType.INTEGER)) {
            return;
        }

        if (BloodMoon.getInstance().getConfig().isBoolean("remove-loot-when-delete")) {
            if (MoonMob.getMobs().contains(livingEntity) && isDeleteMobs) {
                MoonMob.getMobs().remove(livingEntity);
                event.getDrops().clear();
                return;
            }
        }

        MoonMob mob = this.mobUtil.getMob(type);
        event.getDrops().clear();
        if (MoonUniverse.randomChance(BloodPearl.getChanceDrop())) {
            event.getDrops().add((new BloodPearl()).getItemStack());
        }
        MoonMob.getMobs().remove(event.getEntity());
        if (!mob.getDrop().isEmpty())
            for (BloodDrop drop : mob.getDrop()) {
                if (MoonUniverse.randomChance(drop.getChance())) {
                    event.getDrops().add(drop.getStack());
                }
            }
    }

    private void activateAbility(MoonMob mob, Ability ability, LivingEntity entity, Player player) {
        if (!mob.getAbilitys().isEmpty() && MoonUniverse.randomChance(ability.getChance()))
            switch (ability) {
                case BLOOD_DOME:
                    (new BloodDome()).aur(entity.getLocation(), 6, 1.0, 1, false);
                    break;
                case BLOOD_BOLOTO:
                    (new BloodSwamp()).tune(player);
                    break;
                case BLOOD_CAPTURE:
                    (new BloodCapture()).capture(entity, player);
                    break;
                case GROUP_EXPLOSION:
                    (new BloodExplosion()).explode(entity);
                    break;
                case BLOOD_PULSE:
                    (new BloodPulse()).pulse(entity);
                    break;
                case BLOOD_DISARM:
                    (new BloodDisarm()).disarm(player);
                    break;
                case BLOOD_RITUAL:
                    (new BloodRitual()).ritual(entity);
                    break;
                case BLOOD_HOLE:
                    (new BloodHole()).createHole(entity.getUniqueId(), player.getLocation());
                    break;
                case BLOOD_FIRE:
                    (new BloodFire()).fire(player);
                    break;
                case BLOOD_INPUT:
                    (new BloodFire()).holeInput((LivingEntity) player);
                    break;
                case BLOOD_PUSH:
                    (new BloodPush()).push(entity);
                    break;
                case EXPLODE:
                    (new EXPLODE()).explode(entity);
                    break;
                case BLOOD_INFECTION:
                    new BloodInfection().infection(player);
                    break;
                case BLOOD_SUFFOCATE:
                    new BloodSuffocate().bloodSuffocate(player);
                    break;
                case BLOOD_SPAWN:
                    new BloodSpawn().bloodSpawn(player.getLocation());
                    break;
                case BLOOD_FRENZY:
                    new BloodFrenzy().bloodFrenzy(entity);
                    break;
                case BLOOD_CURSE:
                    new BloodCurse().bloodCurse(player);
                    break;
            }
    }

    private boolean fromSpawn(Location location) {
        int radius = 5;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location checkLocation = location.clone().add(x, y, z);
                    Block checkBlock = checkLocation.getBlock();
                    if (checkBlock.getType() == Material.SPAWNER)
                        return true;
                }
            }
        }
        return false;
    }

    private MoonMob mob(MoonMob mob) {
        return new MoonMob(mob.getDisplayName(), mob.isNameVisible(), mob.getHealth(), mob.getDamage(), mob.getSpeed(), mob.getAttack_speed(), mob.getChance(), mob.getWeapon(), mob.getAbilitys(), mob.getDrop(), mob.getMob(), mob.getEffectTypes(), mob.getMythic());
    }

    public static void setIsDeleteMobs(boolean isDeleteMobs) {
        WorldListener.isDeleteMobs = isDeleteMobs;
    }
}
