package moon.bloodmoon;

import moon.bloodmoon.cmd.MoonCmd;
import moon.bloodmoon.cmd.MoonTable;
import moon.bloodmoon.configuration.CashConfig;
import moon.bloodmoon.configuration.CopyingConfig;
import moon.bloodmoon.lang.LangManager;
import moon.bloodmoon.listeners.*;
import moon.bloodmoon.platform.MoonBar;
import moon.bloodmoon.platform.MoonMob;
import moon.bloodmoon.platform.dungeon.DungeonFiles;
import moon.bloodmoon.platform.other.FilterConsole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Logger;
import org.bstats.bukkit.Metrics;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;
import java.util.Objects;

public final class BloodMoon extends JavaPlugin {

    // ───── Static Fields ───────────────────────────────────────────────────────
    private static BloodMoon instance;
    private static FileConfiguration cashConfig;

    // ───── Instance Fields ─────────────────────────────────────────────────────
    private MoonUniverse moonUniverse;
    private boolean itemsAdderEnabled;
    private boolean worldEditEnabled;
    private boolean faweEnabled;
    private boolean blockCmdEnabled;
    private boolean barEnabled;

    // ───── Lifecycle Methods ───────────────────────────────────────────────────
    @Override
    public void onEnable() {
        instance = this;
        ((Logger) LogManager.getRootLogger()).addFilter(new FilterConsole());

        saveDefaultConfig();
        new LangManager().setupFiles();

        checkDependencies();
        saveCashFile();

        cashConfig = new CashConfig().getCashYml();
        moonUniverse = new MoonUniverse();
        MoonMob.setMoonMobs(new CopyingConfig().copy());

        if (worldEditEnabled || faweEnabled) {
            new DungeonFiles().copy();
        }

        registerCommands();
        registerEvents();
        printStartupBanner();

        loadWorldStates();
        resetWorldStateCache();

        new Metrics(this, 24226);
        notifyOperators();
    }

    @Override
    public void onDisable() {
        try {
            persistWorldTasks();
            clearMoonMobs();
            persistMoonStates();
            clearBars();
        } catch (Throwable t) {
            getLogger().severe("Error while disabling plugin:");
            t.printStackTrace();
        }
    }

    // ───── Initialization Helpers ──────────────────────────────────────────────
    private void checkDependencies() {
        PluginManager pm = getServer().getPluginManager();
        itemsAdderEnabled = pm.isPluginEnabled("ItemsAdder");
        worldEditEnabled = pm.isPluginEnabled("WorldEdit");
        faweEnabled = pm.isPluginEnabled("FastAsyncWorldEdit");
    }

    private void saveCashFile() {
        try {
            saveResource("cash.yml", false);
        } catch (Exception ignored) {
        }
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("moon")).setExecutor(new MoonCmd());
        Objects.requireNonNull(getCommand("moon")).setTabCompleter(new MoonTable());
    }

    private void registerEvents() {
        Plugin plugin = this;
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new WorldListener(), plugin);
        pm.registerEvents(new PlayerListener(), plugin);
        pm.registerEvents(new ParticleListener(), plugin);
        pm.registerEvents(new PlayerMoveListener(), plugin);
    }

    private void printStartupBanner() {
        sendConsole("&8+---------------------------------------------+");
        sendConsole("&8| &4BloodMoon &8- &fMoon Adventures Await You!");
        sendConsole(String.format("&8| &fVersion: &4%s &8| &4Author: &fJujjka", getDescription().getVersion()));
        sendConsole("&8+---------------------------------------------+");
        sendConsole("&eBloodMoon &7has &astarted successfully!");
    }

    private void sendConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private void loadWorldStates() {
        MemorySection activeWorlds = (MemorySection) cashConfig.get("worldNow");
        if (activeWorlds != null) {
            for (String name : activeWorlds.getKeys(false)) {
                World world = Bukkit.getWorld(name);
                if (world != null) {
                    moonUniverse.startMoon(world);
                }
            }
        }

        MemorySection taskWorlds = (MemorySection) cashConfig.get("worldTask");
        if (taskWorlds != null) {
            for (String name : taskWorlds.getKeys(false)) {
                World world = Bukkit.getWorld(name);
                if (world != null) {
                    moonUniverse.startTask(world);
                }
            }
        }

        barEnabled = getConfig().getBoolean("bar");
        blockCmdEnabled = getConfig().getBoolean("blockCmd");

        if (MoonUniverse.getWorldMoons().isEmpty() && MoonUniverse.getWorldTasks().isEmpty()) {
            for (World world : Bukkit.getWorlds()) {
                if (world.getEnvironment() == World.Environment.NORMAL && world.getWorldType() == WorldType.NORMAL) {
                    // Optional default logic
                }
            }
        }
    }

    private void resetWorldStateCache() {
        cashConfig.set("worldNow", null);
        cashConfig.set("worldTask", null);
    }

    private void notifyOperators() {
        if (!getConfig().getBoolean("send-enable-plugin-message")) return;

        String message = ChatColor.translateAlternateColorCodes('&',
                getConfig().getString("enable-plugin-message"));

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp()) {
                player.sendMessage(message);
            }
        }
    }

    // ───── Shutdown Helpers ────────────────────────────────────────────────────
    private void persistWorldTasks() {
        for (World world : MoonUniverse.getWorldTasks().keySet()) {
            cashConfig.set("worldTask." + world.getName(), true);
        }
        CashConfig.saveConfigFile(cashConfig);
    }

    private void persistMoonStates() {
        for (World world : MoonUniverse.getWorldMoons()) {
            cashConfig.set("worldNow." + world.getName(), true);
        }
        CashConfig.saveConfigFile(cashConfig);
    }

    private void clearMoonMobs() {
        for (LivingEntity mob : MoonMob.getMobs()) {
            if (!mob.isDead()) {
                mob.setHealth(0);
                mob.remove();
            }
        }
    }

    private void clearBars() {
        for (MoonBar bar : MoonUniverse.getWorldBars().values()) {
            bar.getBossBar().removeAll();
            Bukkit.getScheduler().cancelTask(bar.getTask());
        }
    }

    // ───── Getters ─────────────────────────────────────────────────────────────
    public static BloodMoon getInstance() {
        return instance;
    }

    public static FileConfiguration getCashConfig() {
        return cashConfig;
    }

    public MoonUniverse getMoonUniverse() {
        return moonUniverse;
    }

    public boolean isItemsAdderEnabled() {
        return itemsAdderEnabled;
    }

    public boolean isWorldEditEnabled() {
        return worldEditEnabled;
    }

    public boolean isFaweEnabled() {
        return faweEnabled;
    }

    public boolean isBlockCmdEnabled() {
        return blockCmdEnabled;
    }

    public boolean isBarEnabled() {
        return barEnabled;
    }
}
