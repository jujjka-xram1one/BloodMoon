package moon.bloodmoon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import moon.bloodmoon.lang.LangManager;
import moon.bloodmoon.platform.MoonBar;
import moon.bloodmoon.platform.MoonMob;
import moon.bloodmoon.platform.dungeon.BloodDungeon;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MoonUniverse {
    private static final List<World> worldMoons = new ArrayList<>();

    public static List<World> getWorldMoons() {
        return worldMoons;
    }

    private static final HashMap<World, Integer> worldTasks = new HashMap<>();

    public static HashMap<World, Integer> getWorldTasks() {
        return worldTasks;
    }

    private static final HashMap<World, Integer> worldMoonTasks = new HashMap<>();

    public static HashMap<World, Integer> getWorldMoonTasks() {
        return worldMoonTasks;
    }

    private static final HashMap<World, MoonBar> worldBars = new HashMap<>();

    public static HashMap<World, MoonBar> getWorldBars() {
        return worldBars;
    }

    private static final List<World> worldsCash = new ArrayList<>();

    public static List<World> getWorldsCash() {
        return worldsCash;
    }

    private final BloodMoon instance = BloodMoon.getInstance();

    private final double chanceMoon = this.instance.getConfig().getDouble("chanceMoon");

    public double getChanceMoon() {
        return this.chanceMoon;
    }

    private final LangManager langManager = new LangManager();

    public static boolean isCurrentNight(long time) {
        return (time >= 13180L && time < 13300L);
    }

    public static boolean isNight(long time) {
        return (time >= 13000L && time < 22500L);
    }

    public static boolean random(double chance) {
        Random r = new Random();
        double randomValue = r.nextDouble();
        return (randomValue < chance);
    }

    public boolean startTask(World world) {
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this.instance, () -> {
            if (isCurrentNight(world.getTime()) && !getWorldMoons().contains(world) && random(getChanceMoon()))
                startMoon(world);
        },0L, 121L);
        getWorldTasks().put(world, Integer.valueOf(taskId));
        if (!getWorldsCash().contains(world))
            getWorldsCash().add(world);
        return true;
    }

    public boolean deleteTask(World world) {
        Bukkit.getScheduler().cancelTask(((Integer)getWorldTasks().get(world)).intValue());
        getWorldTasks().remove(world);
        return true;
    }

    public void startMoon(World world) {
        try {
            if (isNight(world.getTime())) {
                if (getWorldTasks().containsKey(world))
                    deleteTask(world);
                if (!getWorldsCash().contains(world))
                    getWorldsCash().add(world);
                getWorldMoons().add(world);
                if (BloodMoon.getInstance().isBar()) {
                    MoonBar bar = new MoonBar(world);
                    bar.create();
                    getWorldBars().put(world, bar);
                }
                startMessage(world);
                if (BloodMoon.getInstance().isWORLD_EDIT_DEPENDENCY() || BloodMoon.getInstance().isFASTWORLD_EDIT_DEPENDENCY())
                    for (BloodDungeon bd : BloodDungeon.getDungeonList())
                        bd.spawn();
                int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this.instance, () -> {
                    if (!isNight(world.getTime())) {
                        deleteMoon(world);
                        startTask(world);
                    }
                },0L, 120L);
                getWorldMoonTasks().put(world, Integer.valueOf(taskId));

                for (String cmdS : BloodMoon.getInstance().getConfig().getStringList("startCommand")){
                    if(cmdS != null) {
                        Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getConsoleSender(), cmdS);
                    }
                }
            }
        } catch (Throwable $ex) {
            throw $ex;
        }
    }

    public boolean deleteMoon(World world) {
        Bukkit.getScheduler().cancelTask(((Integer)getWorldMoonTasks().get(world)).intValue());
        getWorldMoons().remove(world);
        getWorldMoonTasks().remove(world);
        MoonBar bar = getWorldBars().get(world);
        for (String cmdS : BloodMoon.getInstance().getConfig().getStringList("stopCommand")){
            if(cmdS != null) {
                Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getConsoleSender(), cmdS);
            }
        }
        if (bar != null) {
            Bukkit.getScheduler().cancelTask(bar.getTask());
            bar.getBossBar().removeAll();
            getWorldBars().remove(world);
        }
        for (BloodDungeon bd: BloodDungeon.getDungeonList()){
            bd.delete();
        }

        stopMessage(world);
        if (BloodMoon.getInstance().getConfig().getBoolean("deleteMob"))
            for (LivingEntity e : MoonMob.getMobs()) {
                if (!e.isDead())
                    (new BukkitRunnable() {
                        public void run() {
                            if (e.isDead()) {
                                cancel();
                                return;
                            }
                            e.damage(2.5D);
                            if (Bukkit.getOnlinePlayers().isEmpty() &&
                                    e != null)
                                e.setHealth(0.0D);
                        }
                    }).runTaskTimer((Plugin)this.instance, 0L, 3L);
            }
        return true;
    }

    public void startMessage(World world) {
        try {
            for (Player p : world.getPlayers()) {
                if (!this.langManager.msg("moon.startBarMessage").isEmpty())
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, (BaseComponent)new TextComponent(this.langManager.msg("moon.startBarMessage")));
                (new BukkitRunnable() {
                    public void run() {
                        if (!MoonUniverse.this.langManager.msg("moon.startChatMessage").isEmpty())
                            p.sendMessage(MoonUniverse.this.langManager.msg("moon.startChatMessage"));
                    }
                }).runTaskLater((Plugin)this.instance, 60L);
            }
            if (BloodMoon.getInstance().getConfig().getBoolean("startMoonSound")) {
                world.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, Boolean.valueOf(false));
                Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "playsound minecraft:startmoon block @a 0 0 0 1 1 1");
            }
        } catch (Throwable $ex) {
            throw $ex;
        }
    }

    public void stopMessage(World world) {
        for (Player p : world.getPlayers()) {
            if (!this.langManager.msg("moon.stopBarMessage").isEmpty())
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, (BaseComponent)new TextComponent(this.langManager.msg("moon.stopBarMessage")));
            (new BukkitRunnable() {
                public void run() {
                    if (!MoonUniverse.this.langManager.msg("moon.stopChatMessage").isEmpty())
                        p.sendMessage(MoonUniverse.this.langManager.msg("moon.stopChatMessage"));
                }
            }).runTaskLater((Plugin)this.instance, 60L);
        }
        if (BloodMoon.getInstance().getConfig().getBoolean("stopMoonSound")) {
            world.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, Boolean.valueOf(false));
            Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "playsound minecraft:stopmoon block @a 0 0 0 1 1 1");
        }
    }
}
