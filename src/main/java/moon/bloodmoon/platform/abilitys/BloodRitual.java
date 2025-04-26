package moon.bloodmoon.platform.abilitys;

import java.util.*;

import moon.bloodmoon.BloodMoon;
import moon.bloodmoon.platform.MoonMob;
import moon.bloodmoon.platform.other.MobUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BloodRitual {
    // Список защищаемых сущностей с оставшимся временем действия
    private static final Map<LivingEntity, Integer> protectEntities = new HashMap<>();
    private final BloodMoon instance = BloodMoon.getInstance();

    /**
     * Активирует ритуал для указанной сущности.
     *
     * @param entity Сущность, для которой выполняется ритуал.
     */
    public void ritual(LivingEntity entity) {
        if (protectEntities.containsKey(entity)) {
            return; // Сущность уже участвует в ритуале
        }

        protectEntities.put(entity, 40);

        new BukkitRunnable() {
            int tick = 0;

            @Override
            public void run() {
                if (tick >= 10 || entity.isDead()) {
                    cancel();
                    protectEntities.remove(entity);
                    if (!entity.isDead()) {
                        minion(entity); // Призыв миньонов, если сущность жива
                    }
                    return;
                }
                tick++;
            }
        }.runTaskTimer(instance, 0L, 10L);
    }

    /**
     * Призывает миньонов в окружении сущности.
     *
     * @param entity Сущность, вокруг которой появляются миньоны.
     */
    private void minion(LivingEntity entity) {
        if (!protectEntities.containsKey(entity)) {
            return;
        }

        List<MoonMob> availableMobs = new ArrayList<>(MoonMob.getMoonMobs());
        availableMobs.removeIf(mob -> mob.equals(new MobUtil().getMob(EntityType.PHANTOM)));

        if (availableMobs.isEmpty()) {
            Bukkit.getLogger().warning("No valid MoonMobs available for summoning.");
            return;
        }

        Location baseLocation = entity.getLocation();
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            MoonMob mob = availableMobs.get(random.nextInt(availableMobs.size()));
            Location spawnLocation = baseLocation.clone().add(
                    i == 0 ? -2.0 : (i == 1 ? 0.0 : -2.0),
                    0.0,
                    i == 0 ? 0.0 : (i == 1 ? 2.0 : -2.0)
            );
            mob.spawn(spawnLocation);
            parts(spawnLocation);
        }
    }

    /**
     * Создаёт визуальный эффект для призыва.
     *
     * @param location Местоположение, где создаётся эффект.
     */
    private void parts(Location location) {
        World world = location.getWorld();
        if (world == null) {
            return;
        }

        double[][] offsets = {
                {0.0, 0.0, 0.0}, {0.2, 0.0, 0.0}, {-0.2, 0.0, 0.0},
                {0.0, 0.2, 0.0}, {0.0, -0.2, 0.0}, {0.0, 0.0, 0.2}, {0.0, 0.0, -0.2}
        };

        for (double[] offset : offsets) {
            world.spawnParticle(Particle.EXPLOSION_NORMAL,
                    location.clone().add(offset[0], offset[1], offset[2]), 5);
        }
    }

    /**
     * Возвращает защищаемые сущности.
     *
     * @return Карта защищаемых сущностей.
     */
    public static Map<LivingEntity, Integer> getProtectEntities() {
        return protectEntities;
    }
}
