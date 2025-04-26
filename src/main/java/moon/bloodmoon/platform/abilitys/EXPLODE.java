package moon.bloodmoon.platform.abilitys;

import java.util.ArrayList;
import java.util.List;
import moon.bloodmoon.BloodMoon;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class EXPLODE {
    private static final List<Player> playersSwamp = new ArrayList<>();
    private final BloodMoon instance = BloodMoon.getInstance();

    /**
     * Возвращает список игроков, находящихся в зоне "Swamp".
     *
     * @return Список игроков.
     */
    public static List<Player> getPlayersSwamp() {
        return playersSwamp;
    }

    /**
     * Создаёт взрыв через заданное время после вызова.
     *
     * @param entity Сущность, в позиции которой произойдёт взрыв.
     */
    public void explode(final LivingEntity entity) {
        try {
            // Увеличение скорости сущности перед взрывом
            entity.setVelocity(entity.getVelocity().multiply(5));

            final Location explosionLocation = entity.getLocation().clone();

            // Запуск задачи на создание взрыва
            new BukkitRunnable() {
                @Override
                public void run() {
                    explosionLocation.getWorld().createExplosion(explosionLocation, 10.0F);
                }
            }.runTaskLater(instance, 10L); // Задержка 10 тиков (~0.5 секунд)
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}