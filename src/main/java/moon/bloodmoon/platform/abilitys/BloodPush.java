package moon.bloodmoon.platform.abilitys;

import moon.bloodmoon.BloodMoon;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class BloodPush {
    private final BloodMoon instance = BloodMoon.getInstance();

    /**
     * Отталкивает всех ближайших сущностей от заданной сущности.
     *
     * @param entity Сущность, вокруг которой выполняется отталкивание.
     */
    public void push(LivingEntity entity) {
        try {
            for (Entity nearbyEntity : entity.getNearbyEntities(5.0D, 5.0D, 5.0D)) {
                if (nearbyEntity instanceof LivingEntity) {
                    repelEntityFromLocation(nearbyEntity, entity.getLocation(), 3.0D);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Отталкивает сущность от указанной локации с заданной силой.
     *
     * @param entity  Сущность, которую нужно оттолкнуть.
     * @param location Локация, от которой выполняется отталкивание.
     * @param force    Сила отталкивания.
     */
    private void repelEntityFromLocation(Entity entity, Location location, double force) {
        Vector repelVector = entity.getLocation().toVector().subtract(location.toVector());
        repelVector.normalize().multiply(force);
        entity.setVelocity(repelVector);
    }
}