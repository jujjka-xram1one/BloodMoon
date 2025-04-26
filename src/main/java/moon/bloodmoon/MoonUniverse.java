package moon.bloodmoon;

import moon.bloodmoon.lang.LangManager;
import moon.bloodmoon.platform.MoonBar;
import moon.bloodmoon.platform.MoonMob;
import moon.bloodmoon.platform.dungeon.BloodDungeon;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class MoonUniverse {

    private static final List<World> worldMoons = new ArrayList<>();
    private static final Map<World, Integer> worldTasks = new HashMap<>();
    private static final Map<World, Integer> worldMoonTasks = new HashMap<>();
    private static final Map<World, MoonBar> worldBars = new HashMap<>();
    private static final List<World> worldsCash = new ArrayList<>();

    private final BloodMoon plugin = BloodMoon.getInstance();
    private final LangManager langManager = new LangManager();
    private final double chanceMoon = plugin.getConfig().getDouble("chanceMoon");

    public static List<World> getWorldMoons() {
        return worldMoons;
    }

    public static Map<World, Integer> getWorldTasks() {
        return worldTasks;
    }

    public static Map<World, Integer> getWorldMoonTasks() {
        return worldMoonTasks;
    }

    public static Map<World, MoonBar> getWorldBars() {
        return worldBars;
    }

    public static List<World> getWorldsCash() {
        return worldsCash;
    }

    public double getChanceMoon() {
        return chanceMoon;
    }

    public static boolean isCurrentNight(long time) {
        return time >= 13180L && time < 13300L;
    }

    public static boolean isNight(long time) {
        return time >= 13000L && time < 22500L;
    }

    public static boolean randomChance(double chance) {
        return new Random().nextDouble() < chance;
    }

    public boolean startTask(World world) {
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (isCurrentNight(world.getTime()) && !worldMoons.contains(world) && randomChance(chanceMoon)) {
                startMoon(world);
            }
        }, 0L, 121L);

        worldTasks.put(world, taskId);
        worldsCash.add(world);
        return true;
    }

    public boolean deleteTask(World world) {
        Integer taskId = worldTasks.remove(world);
        if (taskId != null) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
        return true;
    }

    public void startMoon(World world) {
        if (!isNight(world.getTime())) return;

        deleteTask(world);
        worldMoons.add(world);
        worldsCash.add(world);

        if (plugin.isBarEnabled()) {
            MoonBar bar = new MoonBar(world);
            bar.create();
            worldBars.put(world, bar);
        }

        startMessage(world);

        if (plugin.isWorldEditEnabled() || plugin.isFaweEnabled()) {
            BloodDungeon.getDungeonList().forEach(BloodDungeon::spawn);
        }

        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (!isNight(world.getTime())) {
                deleteMoon(world);
                startTask(world);
            }
        }, 0L, 120L);

        worldMoonTasks.put(world, taskId);

        plugin.getConfig().getStringList("startCommand").forEach(cmd -> {
            if (cmd != null && !cmd.isEmpty()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
        });
    }

    public boolean deleteMoon(World world) {
        Integer taskId = worldMoonTasks.remove(world);
        if (taskId != null) {
            Bukkit.getScheduler().cancelTask(taskId);
        }

        worldMoons.remove(world);

        MoonBar bar = worldBars.remove(world);
        if (bar != null) {
            Bukkit.getScheduler().cancelTask(bar.getTask());
            bar.getBossBar().removeAll();
        }

        BloodDungeon.getDungeonList().forEach(BloodDungeon::delete);
        stopMessage(world);

        if (plugin.getConfig().getBoolean("deleteMob")) {
            for (LivingEntity e : MoonMob.getMobs()) {
                if (!e.isDead()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (e.isDead()) {
                                cancel();
                            } else {
                                e.damage(2.5D);
                                if (Bukkit.getOnlinePlayers().isEmpty()) {
                                    e.setHealth(0.0D);
                                }
                            }
                        }
                    }.runTaskTimer(plugin, 0L, 3L);
                }
            }
        }

        plugin.getConfig().getStringList("stopCommand").forEach(cmd -> {
            if (cmd != null && !cmd.isEmpty()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
        });

        return true;
    }

    private void startMessage(World world) {
        for (Player p : world.getPlayers()) {
            String barMessage = langManager.msg("moon.startBarMessage");
            if (!barMessage.isEmpty()) {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(barMessage));
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    String chatMsg = langManager.msg("moon.startChatMessage");
                    if (!chatMsg.isEmpty()) {
                        p.sendMessage(chatMsg);
                    }
                }
            }.runTaskLater(plugin, 60L);
        }

        if (plugin.getConfig().getBoolean("startMoonSound")) {
            world.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, false);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:startmoon block @a 0 0 0 1 1 1");
        }
    }

    private void stopMessage(World world) {
        for (Player p : world.getPlayers()) {
            String barMessage = langManager.msg("moon.stopBarMessage");
            if (!barMessage.isEmpty()) {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(barMessage));
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    String chatMsg = langManager.msg("moon.stopChatMessage");
                    if (!chatMsg.isEmpty()) {
                        p.sendMessage(chatMsg);
                    }
                }
            }.runTaskLater(plugin, 60L);
        }

        if (plugin.getConfig().getBoolean("stopMoonSound")) {
            world.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, false);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:stopmoon block @a 0 0 0 1 1 1");
        }
    }
}
