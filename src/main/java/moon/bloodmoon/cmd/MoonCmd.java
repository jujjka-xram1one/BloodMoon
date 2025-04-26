package moon.bloodmoon.cmd;

import moon.bloodmoon.BloodMoon;
import moon.bloodmoon.MoonUniverse;
import moon.bloodmoon.items.BloodPearl;
import moon.bloodmoon.lang.LangManager;
import moon.bloodmoon.listeners.WorldListener;
import moon.bloodmoon.platform.MoonMob;
import moon.bloodmoon.platform.dungeon.BloodDungeon;
import moon.bloodmoon.platform.hand.BloodAxe;
import moon.bloodmoon.platform.hand.BloodBow;
import moon.bloodmoon.platform.hand.BloodPickaxe;
import moon.bloodmoon.platform.hand.BloodSword;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MoonCmd implements CommandExecutor {
    private LangManager lM = new LangManager();

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        String wn;
        ItemStack is1;
        int i;
        World world = null;
        ItemStack is2, is3, is4;
        if (!(sender instanceof Player))
            return false;
        Player player = (Player)sender;
        if (args.length == 0)
            return false;
        switch (args[0]) {
            case "startTimer":
                if (args.length < 2) {
                    player.sendMessage(this.lM.msg("m.worldNotSelect"));
                    return false;
                }
                wn = args[1];
                if (Bukkit.getWorld(wn) == null) {
                    player.sendMessage(this.lM.msg("m.worldNotFind"));
                    return false;
                }
                world = Bukkit.getWorld(wn);
                if (world.getEnvironment() != World.Environment.NORMAL) {
                    player.sendMessage(this.lM.msg("m.worldNotNormal"));
                    return false;
                }
                if (MoonUniverse.getWorldTasks().containsKey(world) || MoonUniverse.getWorldMoons().contains(world)) {
                    player.sendMessage(this.lM.msg("m.worldHaveMoon"));
                    return false;
                }
                BloodMoon.getInstance().getMoonUniverse().startTask(world);
                player.sendMessage(this.lM.msg("m.worldTimeWait"));
                return true;
            case "cancelTimer":
                if (args.length < 2) {
                    player.sendMessage(this.lM.msg("m.worldNotSelect"));
                    return false;
                }
                wn = args[1];
                if (Bukkit.getWorld(wn) == null) {
                    player.sendMessage(this.lM.msg("m.worldNotFind"));
                    return false;
                }
                world = Bukkit.getWorld(wn);
                if (world.getEnvironment() != World.Environment.NORMAL) {
                    player.sendMessage(this.lM.msg("m.worldNotNormal"));
                    return false;
                }
                if (!MoonUniverse.getWorldTasks().containsKey(world)) {
                    player.sendMessage(this.lM.msg("m.worldNotMoon"));
                    return false;
                }
                BloodMoon.getInstance().getMoonUniverse().deleteTask(world);
                MoonUniverse.getWorldsCash().remove(world);
                player.sendMessage(this.lM.msg("m.worldDeleteTask"));
                return true;
            case "createMoon":
                if (args.length < 2) {
                    player.sendMessage(this.lM.msg("m.worldNotSelect"));
                    return false;
                }
                wn = args[1];
                if (Bukkit.getWorld(wn) == null) {
                    player.sendMessage(this.lM.msg("m.worldNotFind"));
                    return false;
                }
                world = Bukkit.getWorld(wn);
                if (world.getEnvironment() != World.Environment.NORMAL) {
                    player.sendMessage(this.lM.msg("m.worldNotNormal"));
                    return false;
                }
                if (MoonUniverse.getWorldMoons().contains(world)) {
                    player.sendMessage(this.lM.msg("m.worldHaveMoon"));
                    return false;
                }
                if (!MoonUniverse.isNight(world.getTime())) {
                    player.sendMessage(this.lM.msg("m.worldNotNight"));
                    return false;
                }
                BloodMoon.getInstance().getMoonUniverse().startMoon(world);
                return true;
            case "deleteMoon":
                if (args.length < 2) {
                    player.sendMessage(this.lM.msg("m.worldNotSelect"));
                    return false;
                }
                wn = args[1];
                if (Bukkit.getWorld(wn) == null) {
                    player.sendMessage(this.lM.msg("m.worldNotFind"));
                    return false;
                }
                world = Bukkit.getWorld(wn);
                if (world.getEnvironment() != World.Environment.NORMAL) {
                    player.sendMessage(this.lM.msg("m.worldNotNormal"));
                    return false;
                }
                if (!MoonUniverse.getWorldMoons().contains(world)) {
                    player.sendMessage(this.lM.msg("m.worldNotMoon"));
                    return false;
                }
                BloodMoon.getInstance().getMoonUniverse().deleteMoon(world);
                MoonUniverse.getWorldsCash().remove(world);
                return true;
            case "amountMobs":
                player.sendMessage(String.format(this.lM.msg("m.amountMobs"), new Object[]{Integer.valueOf(MoonMob.getMobs().size())}));
                return true;
            case "reload":
                Bukkit.getServer().getPluginManager().disablePlugin((Plugin) BloodMoon.getInstance());
                Bukkit.getServer().getPluginManager().enablePlugin((Plugin) BloodMoon.getInstance());
                player.sendMessage("" + ChatColor.AQUA + "Reload...");
                return true;
            case "deleteMobs":
                WorldListener.setIsDeleteMobs(true);
                for (LivingEntity mob : MoonMob.getMobs()) {
                    mob.getEquipment().clear();
                    mob.damage(mob.getMaxHealth());
                }
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        WorldListener.setIsDeleteMobs(false);
                    }
                }.runTaskLater(BloodMoon.getInstance(),30);
                return true;
            case "info":
                if (args.length < 2) {
                    player.sendMessage(this.lM.msg("m.worldNotSelect"));
                    return false;
                }
                wn = args[1];
                if (Bukkit.getWorld(wn) == null) {
                    player.sendMessage(this.lM.msg("m.worldNotFind"));
                    return false;
                }
                world = Bukkit.getWorld(wn);
                if(!MoonUniverse.getWorldMoons().contains(world)){
                    player.sendMessage(this.lM.msg("m.worldNotMoon"));
                    return true;
                }
                long nowTime = world.getTime();
                long remains = 22500L - nowTime;
                int secondTime = Math.toIntExact(remains / 20);
                player.sendMessage(String.format(this.lM.msg("m.remains"), secondTime));
                return true;
            case "allDungeons":
                i = 0;
                for (BloodDungeon bd : BloodDungeon.getDungeonList()) {
                    if (bd.getLocation() != null) {
                        i++;
                        String format = ChatColor.translateAlternateColorCodes('&', String.format("{}", new Object[] { Integer.valueOf(i), bd.getId(), Integer.valueOf(bd.getLocation().getBlockX()), Integer.valueOf(bd.getLocation().getBlockY()), Integer.valueOf(bd.getLocation().getBlockZ()) }));
                        TextComponent message = new TextComponent(format);
                        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/teleport " + bd.getLocation().getBlockX() + " " + bd.getLocation().getBlockY() + " " + bd.getLocation().getBlockZ()));
                        player.spigot().sendMessage((BaseComponent)message);
                    }
                }
                return true;
            case "help":
                player.sendMessage(this.lM.msg("m.helpMessage"));
                return true;
        }
        return false;
    }
}
