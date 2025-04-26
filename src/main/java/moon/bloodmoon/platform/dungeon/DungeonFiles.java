package moon.bloodmoon.platform.dungeon;

import moon.bloodmoon.BloodMoon;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;

public class DungeonFiles {
    private final FileConfiguration config = BloodMoon.getInstance().getConfig();

    public boolean copy() {
        try {
            if (this.config.contains("danges")) {
                MemorySection dangesSection = (MemorySection)this.config.get("danges");
                for (String set : dangesSection.getKeys(false)) {
                    if (set != null) {
                        String id = set;
                        String world = dangesSection.getString(set + ".world");
                        int start = dangesSection.getInt(set + ".radius-spawn.1");
                        int end = dangesSection.getInt(set + ".radius-spawn.2");
                        BloodDungeon.RandomCord cord = new BloodDungeon.RandomCord(start, end);
                        int firstCooldown = dangesSection.getInt(set + ".firstCooldown");
                        String schem = dangesSection.getString(set + ".schemName");
                        String message = dangesSection.getString(set + ".message");
                        boolean explode = dangesSection.getBoolean(set + ".explode");
                        if (Bukkit.getWorld(world) == null) {
                            Bukkit.getLogger().info(String.format(world, new Object[0]));
                            return false;
                        }
                        World w = Bukkit.getWorld(world);
                        BloodDungeon dungeon = new BloodDungeon(id, w, cord, firstCooldown, schem, message,explode);
                        BloodDungeon.getDungeonList().add(dungeon);
                    }
                }
            }
            return true;
        } catch (Throwable $ex) {
            throw $ex;
        }
    }
}
