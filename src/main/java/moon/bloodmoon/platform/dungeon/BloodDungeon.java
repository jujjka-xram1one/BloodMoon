package moon.bloodmoon.platform.dungeon;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import moon.bloodmoon.BloodMoon;

public class BloodDungeon {

    // Static list of all dungeons
    private static List<BloodDungeon> dungeonList = new ArrayList<>();

    // Dungeon properties
    private final String id;
    private final org.bukkit.World world;
    private final RandomCord randomCord;
    private final int firstCooldown;
    private final String schem;
    private final String message;
    private final boolean explode;
    private Location location;

    public BloodDungeon(String id, org.bukkit.World world, RandomCord randomCord, int firstCooldown, String schem, String message, boolean explode) {
        this.id = id;
        this.world = world;
        this.randomCord = randomCord;
        this.firstCooldown = firstCooldown;
        this.schem = schem;
        this.message = message;
        this.explode = explode;
    }

    public static List<BloodDungeon> getDungeonList() {
        return dungeonList;
    }

    public static void setDungeonList(List<BloodDungeon> dungeonList) {
        BloodDungeon.dungeonList = dungeonList;
    }

    public String getId() {
        return id;
    }

    public org.bukkit.World getWorld() {
        return world;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void delete() {
        if (explode) {
            world.createExplosion(location, 40);
        }
    }

    public void spawn() {
        new BukkitRunnable() {
            public void run() {
                File schematicFile = fileSchematic(schem);
                if (schematicFile == null) {
                    Bukkit.getLogger().warning("Schematic " + schem + " not found.");
                    return;
                }

                // Generate random coordinates
                Random random = new Random();
                int x = random.nextInt(randomCord.getEnd() - randomCord.getStart()) + randomCord.getStart();
                int z = random.nextInt(randomCord.getEnd() - randomCord.getStart()) + randomCord.getStart();
                int y = world.getHighestBlockYAt(x, z);

                // Set location and paste schematic
                location = new Location(world, x, y, z);
                loadSchematic(schem, location);

                // Notify players
                if (message != null) {
                    for (Player player : world.getPlayers()) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                message.replace("%name", id).replace("%x", "" + x).replace("%y", "" + y).replace("%z", "" + z)));
                    }
                }

                // Log to console
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&',
                        message.replace("%name", id).replace("%x", "" + x).replace("%y", "" + y).replace("%z", "" + z)));
            }
        }.runTaskLater(BloodMoon.getInstance(), 1200L * firstCooldown);
    }

    public void loadSchematic(String fileName, Location location) {
        File schem = fileSchematic(fileName);
        if (schem == null) {
            throw new IllegalArgumentException("Schematic file not found: " + fileName);
        }

        Clipboard clipboard;
        ClipboardFormat format = ClipboardFormats.findByFile(schem);
        if (format == null) {
            throw new IllegalArgumentException("Unsupported schematic format: " + fileName);
        }

        try (ClipboardReader reader = format.getReader(new FileInputStream(schem))) {
            clipboard = reader.read();
        } catch (IOException e) {
            throw new RuntimeException("Error reading schematic file: " + fileName, e);
        }

        com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(location.getWorld());
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(weWorld)) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ()))
                    .ignoreAirBlocks(false)
                    .build();
            Operations.complete(operation);
        } catch (WorldEditException e) {
            throw new RuntimeException("Error pasting schematic: " + fileName, e);
        }
    }

    private File fileSchematic(String id) {
        File schematicsFolder = getSchematicsFolder();
        if (schematicsFolder != null) {
            for (File file : schematicsFolder.listFiles()) {
                if (file.getName().equals(id + ".schem")) {
                    return file;
                }
            }
        }
        return null;
    }

    private File getSchematicsFolder() {
        File dataFolder = BloodMoon.getInstance().getDataFolder();
        for (File file : dataFolder.listFiles()) {
            if (file.getName().equalsIgnoreCase("dungeons")) {
                return file;
            }
        }
        return null;
    }

    public static class RandomCord {
        private final int start;
        private final int end;

        public RandomCord(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }
    }
}
