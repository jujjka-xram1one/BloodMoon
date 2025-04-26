package moon.bloodmoon.platform;

import moon.bloodmoon.BloodMoon;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MoonBar {
    private final World world;
    private BossBar bossBar;
    private int task;

    public MoonBar(World world) {
        this.world = world;
    }

    public void setBossBar(BossBar bossBar) {
        this.bossBar = bossBar;
    }

    public void setTask(int task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoonBar)) return false;
        MoonBar other = (MoonBar) o;
        return getTask() == other.getTask() && getWorld().equals(other.getWorld()) && getBossBar().equals(other.getBossBar());
    }

    @Override
    public int hashCode() {
        int result = getTask();
        result = 59 * result + getWorld().hashCode();
        result = 59 * result + getBossBar().hashCode();
        return result;
    }

    public World getWorld() {
        return world;
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public int getTask() {
        return task;
    }

    public void addPlayer(Player player) {
        bossBar.addPlayer(player);
    }

    public void create() {
        bossBar = Bukkit.createBossBar("✪", BarColor.valueOf(BloodMoon.getInstance().getConfig().getString("barColor")),
                BarStyle.valueOf(BloodMoon.getInstance().getConfig().getString("barStyle")));
        bossBar.setVisible(true);
        bossBar.addFlag(BarFlag.CREATE_FOG);
        cast();
    }

    public void cast() {
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)BloodMoon.getInstance(), () -> {
            for (Player player : world.getPlayers()) {
                addPlayer(player);
            }
        }, 0L, 120L);
    }
}
