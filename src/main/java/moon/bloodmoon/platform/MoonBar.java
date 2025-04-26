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

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MoonBar))
            return false;
        MoonBar other = (MoonBar)o;
        if (!other.canEqual(this))
            return false;
        if (getTask() != other.getTask())
            return false;
        Object this$world = getWorld(), other$world = other.getWorld();
        if ((this$world == null) ? (other$world != null) : !this$world.equals(other$world))
            return false;
        Object this$bossBar = getBossBar(), other$bossBar = other.getBossBar();
        return !((this$bossBar == null) ? (other$bossBar != null) : !this$bossBar.equals(other$bossBar));
    }

    protected boolean canEqual(Object other) {
        return other instanceof MoonBar;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + getTask();
        Object $world = getWorld();
        result = result * 59 + (($world == null) ? 43 : $world.hashCode());
        Object $bossBar = getBossBar();
        return result * 59 + (($bossBar == null) ? 43 : $bossBar.hashCode());
    }
    public World getWorld() {
        return this.world;
    }

    public BossBar getBossBar() {
        return this.bossBar;
    }

    public int getTask() {
        return this.task;
    }

    public void addPlayer(Player player) {
        this.bossBar.addPlayer(player);
    }

    public void create() {
        this.bossBar = Bukkit.createBossBar("\u2721", BarColor.valueOf(BloodMoon.getInstance().getConfig().getString("barColor")), BarStyle.valueOf(BloodMoon.getInstance().getConfig().getString("barStyle")), new BarFlag[0]);
        this.bossBar.setVisible(true);
        this.bossBar.addFlag(BarFlag.CREATE_FOG);
        cast();
    }

    public void cast() {
        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)BloodMoon.getInstance(), new Runnable() {
            public void run() {
                for (Player player : MoonBar.this.getWorld().getPlayers())
                    MoonBar.this.addPlayer(player);
            }
        },  0L, 120L);
    }
}
