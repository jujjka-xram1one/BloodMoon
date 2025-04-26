package moon.bloodmoon;

import java.util.Iterator;
import moon.bloodmoon.cmd.MoonCmd;
import moon.bloodmoon.cmd.MoonTable;
import moon.bloodmoon.configuration.CashConfig;
import moon.bloodmoon.configuration.CopyingConfig;
import moon.bloodmoon.lang.LangManager;
import moon.bloodmoon.listeners.ParticleListener;
import moon.bloodmoon.listeners.PlayerListener;
import moon.bloodmoon.listeners.PlayerMoveListener;
import moon.bloodmoon.listeners.WorldListener;
import moon.bloodmoon.platform.MoonBar;
import moon.bloodmoon.platform.MoonMob;
import moon.bloodmoon.platform.dungeon.DungeonFiles;
import moon.bloodmoon.platform.other.FilterConsole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class BloodMoon extends JavaPlugin {
    private static BloodMoon instance;

    private static FileConfiguration cash_configuration;

    private MoonUniverse moonUniverse;

    public static BloodMoon getInstance() {
        return instance;
    }

    public static FileConfiguration getCash_configuration() {
        return cash_configuration;
    }

    public MoonUniverse getMoonUniverse() {
        return this.moonUniverse;
    }

    private boolean ITEMS_ADDER_DEPENDENCY = false;

    public boolean isITEMS_ADDER_DEPENDENCY() {
        return this.ITEMS_ADDER_DEPENDENCY;
    }

    public void setITEMS_ADDER_DEPENDENCY(boolean ITEMS_ADDER_DEPENDENCY) {
        this.ITEMS_ADDER_DEPENDENCY = ITEMS_ADDER_DEPENDENCY;
    }

    private boolean WORLD_EDIT_DEPENDENCY = false;
    private boolean FASTWORLD_EDIT_DEPENDENCY = false;

    public boolean isWORLD_EDIT_DEPENDENCY() {
        return this.WORLD_EDIT_DEPENDENCY;
    }

    public void setWORLD_EDIT_DEPENDENCY(boolean WORLD_EDIT_DEPENDENCY) {
        this.WORLD_EDIT_DEPENDENCY = WORLD_EDIT_DEPENDENCY;
    }

    public boolean isFASTWORLD_EDIT_DEPENDENCY() {
        return FASTWORLD_EDIT_DEPENDENCY;
    }

    public void setFASTWORLD_EDIT_DEPENDENCY(boolean FASTWORLD_EDIT_DEPENDENCY) {
        this.FASTWORLD_EDIT_DEPENDENCY = FASTWORLD_EDIT_DEPENDENCY;
    }

    private boolean BLOCKED_CMD = false;

    public boolean isBLOCKED_CMD() {
        return this.BLOCKED_CMD;
    }

    public void setBLOCKED_CMD(boolean BLOCKED_CMD) {
        this.BLOCKED_CMD = BLOCKED_CMD;
    }

    private boolean bar = true;

    public boolean isBar() {
        return this.bar;
    }

    public void onEnable() {
        ((Logger)LogManager.getRootLogger()).addFilter((Filter)new FilterConsole());
        instance = this;
        saveDefaultConfig();
        (new LangManager()).setupFiles();
        if (getServer().getPluginManager().isPluginEnabled("ItemsAdder"))
            setITEMS_ADDER_DEPENDENCY(true);
        if (getServer().getPluginManager().isPluginEnabled("WorldEdit"))
            setWORLD_EDIT_DEPENDENCY(true);
        if(getServer().getPluginManager().isPluginEnabled("FastAsyncWorldEdit"))
            setFASTWORLD_EDIT_DEPENDENCY(true);
        try {
            saveResource("cash.yml", false);
        } catch (Exception e){

        }
        cash_configuration = (new CashConfig()).getCashYml();
        this.moonUniverse = new MoonUniverse();
        MoonMob.setMoonMobs((new CopyingConfig()).copy());
        if (isWORLD_EDIT_DEPENDENCY() || isFASTWORLD_EDIT_DEPENDENCY())
            (new DungeonFiles()).copy();
        getCommand("moon").setExecutor((CommandExecutor)new MoonCmd());
        getCommand("moon").setTabCompleter((TabCompleter)new MoonTable());
        getServer().getPluginManager().registerEvents((Listener)new WorldListener(), (Plugin)this);
        getServer().getPluginManager().registerEvents((Listener)new PlayerListener(), (Plugin)this);
        getServer().getPluginManager().registerEvents((Listener)new ParticleListener(), (Plugin)this);
        getServer().getPluginManager().registerEvents((Listener)new PlayerMoveListener(), (Plugin)this);
        moon();
        this.bar = getConfig().getBoolean("bar");
        setBLOCKED_CMD(getConfig().getBoolean("blockCmd"));
        MemorySection sectionNow = (MemorySection)getCash_configuration().get("worldNow");
        if (sectionNow != null)
            for (String set : sectionNow.getKeys(false)) {
                if (set != null && Bukkit.getWorld(set) != null)
                    getMoonUniverse().startMoon(Bukkit.getWorld(set));
            }
        MemorySection sectionTask = (MemorySection)getCash_configuration().get("worldTask");
        if (sectionTask != null)
            for (String set : sectionTask.getKeys(false)) {
                if (set != null && Bukkit.getWorld(set) != null)
                    getMoonUniverse().startTask(Bukkit.getWorld(set));
            }
        if (MoonUniverse.getWorldMoons().isEmpty() && MoonUniverse.getWorldTasks().isEmpty())
            for (World world : Bukkit.getWorlds()) {
                if (world.getEnvironment() != World.Environment.NORMAL || world.getWorldType() == WorldType.NORMAL);
            }
        getCash_configuration().set("worldNow", null);
        getCash_configuration().set("worldTask", null);

        new Metrics(this,24226);

        if (getConfig().getBoolean("send-enable-plugin-message"))
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp())
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("enable-plugin-message")));
            }
    }

    public void onDisable() {
        try {
            Iterator<World> worldIterator = MoonUniverse.getWorldTasks().keySet().iterator();
            while (worldIterator.hasNext()) {
                World world = worldIterator.next();
                getCash_configuration().set("worldTask." + world.getName(), Boolean.valueOf(true));
                getCash_configuration().save((new CashConfig()).getFather());
            }
            for (LivingEntity moonMob : MoonMob.getMobs()) {
                if (!moonMob.isDead()) {
                    moonMob.setHealth(0.0D);
                    moonMob.remove();
                }
            }
            for (World world : MoonUniverse.getWorldMoons()) {
                getCash_configuration().set("worldNow." + world.getName(), Boolean.valueOf(true));
                getCash_configuration().save((new CashConfig()).getFather());
            }
            Iterator<MoonBar> barIterator = MoonUniverse.getWorldBars().values().iterator();
            while (barIterator.hasNext()) {
                MoonBar bar = barIterator.next();
                bar.getBossBar().removeAll();
                Bukkit.getScheduler().cancelTask(bar.getTask());
            }
        } catch (Throwable $ex) {
            $ex.fillInStackTrace();
        }
    }

    private void moon() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8+---------------------------------------------+"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8| &4BloodMoon &8- &fMoon Adventures Await You!"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&8| &fVersion: &4%s &8| &4Author: &fJujjka", getDescription().getVersion())));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8+---------------------------------------------+"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eBloodMoon &7has &astarted successfully!"));
    }
}

