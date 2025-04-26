package moon.bloodmoon.platform.dungeon;

import moon.bloodmoon.BloodMoon;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.logging.Level;

public class DungeonFiles {
    private final FileConfiguration config = BloodMoon.getInstance().getConfig();

    public boolean copy() {
        try {
            // Check if "danges" section exists
            if (this.config.contains("danges")) {
                MemorySection dangesSection = (MemorySection) this.config.get("danges");

                // Iterate through the danges section
                for (String set : dangesSection.getKeys(false)) {
                    if (set == null || set.isEmpty()) continue;

                    String id = set;
                    String worldName = dangesSection.getString(set + ".world");

                    // Skip if world name is missing
                    if (worldName == null || worldName.isEmpty()) {
                        Bukkit.getLogger().warning("World name is missing for dungeon: " + id);
                        continue;
                    }

                    World world = Bukkit.getWorld(worldName);

                    // Skip if world is not found
                    if (world == null) {
                        Bukkit.getLogger().warning("World not found: " + worldName + " for dungeon: " + id);
                        continue;
                    }

                    // Get remaining parameters
                    int start = dangesSection.getInt(set + ".radius-spawn.1");
                    int end = dangesSection.getInt(set + ".radius-spawn.2");
                    BloodDungeon.RandomCord cord = new BloodDungeon.RandomCord(start, end);
                    int firstCooldown = dangesSection.getInt(set + ".firstCooldown");
                    String schem = dangesSection.getString(set + ".schemName");
                    String message = dangesSection.getString(set + ".message");
                    boolean explode = dangesSection.getBoolean(set + ".explode");

                    // Create and add the dungeon
                    BloodDungeon dungeon = new BloodDungeon(id, world, cord, firstCooldown, schem, message, explode);
                    BloodDungeon.getDungeonList().add(dungeon);
                    Bukkit.getLogger().info("Dungeon loaded: " + id);
                }
            }
            return true;
        } catch (Exception e) {
            // Log error if something goes wrong
            Bukkit.getLogger().log(Level.SEVERE, "Error loading dungeons from config", e);
            return false;
        }
    }
}
