package moon.bloodmoon.cmd;

import moon.bloodmoon.BloodMoon;
import moon.bloodmoon.MoonUniverse;
import moon.bloodmoon.items.BloodPearl;
import moon.bloodmoon.lang.LangManager;
import moon.bloodmoon.listeners.WorldListener;
import moon.bloodmoon.platform.MoonMob;
import moon.bloodmoon.platform.dungeon.BloodDungeon;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MoonCmd implements CommandExecutor {

    private final LangManager lang = new LangManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (args.length == 0) return false;

        String worldName = args.length > 1 ? args[1] : null;
        World world = (worldName != null) ? Bukkit.getWorld(worldName) : null;

        switch (args[0].toLowerCase()) {
            case "starttimer" -> {
                if (!validateWorld(player, world)) return true;
                if (MoonUniverse.getWorldTasks().containsKey(world) || MoonUniverse.getWorldMoons().contains(world)) {
                    player.sendMessage(lang.msg("m.worldHaveMoon"));
                    return true;
                }
                BloodMoon.getInstance().getMoonUniverse().startTask(world);
                player.sendMessage(lang.msg("m.worldTimeWait"));
                return true;
            }
            case "canceltimer" -> {
                if (!validateWorld(player, world)) return true;
                if (!MoonUniverse.getWorldTasks().containsKey(world)) {
                    player.sendMessage(lang.msg("m.worldNotMoon"));
                    return true;
                }
                BloodMoon.getInstance().getMoonUniverse().deleteTask(world);
                MoonUniverse.getWorldsCash().remove(world);
                player.sendMessage(lang.msg("m.worldDeleteTask"));
                return true;
            }
            case "createmoon" -> {
                if (!validateWorld(player, world)) return true;
                if (MoonUniverse.getWorldMoons().contains(world)) {
                    player.sendMessage(lang.msg("m.worldHaveMoon"));
                    return true;
                }
                if (!MoonUniverse.isNight(world.getTime())) {
                    player.sendMessage(lang.msg("m.worldNotNight"));
                    return true;
                }
                BloodMoon.getInstance().getMoonUniverse().startMoon(world);
                return true;
            }
            case "deletemoon" -> {
                if (!validateWorld(player, world)) return true;
                if (!MoonUniverse.getWorldMoons().contains(world)) {
                    player.sendMessage(lang.msg("m.worldNotMoon"));
                    return true;
                }
                BloodMoon.getInstance().getMoonUniverse().deleteMoon(world);
                MoonUniverse.getWorldsCash().remove(world);
                return true;
            }
            case "amountmobs" -> {
                player.sendMessage(String.format(lang.msg("m.amountMobs"), MoonMob.getMobs().size()));
                return true;
            }
            case "reload" -> {
                Plugin plugin = BloodMoon.getInstance();
                Bukkit.getPluginManager().disablePlugin(plugin);
                Bukkit.getPluginManager().enablePlugin(plugin);
                player.sendMessage(ChatColor.AQUA + "Reload...");
                return true;
            }
            case "deletemobs" -> {
                WorldListener.setIsDeleteMobs(true);
                for (LivingEntity mob : MoonMob.getMobs()) {
                    if (mob != null) {
                        mob.getEquipment().clear();
                        mob.damage(mob.getMaxHealth());
                    }
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        WorldListener.setIsDeleteMobs(false);
                    }
                }.runTaskLater(BloodMoon.getInstance(), 30L);
                return true;
            }
            case "info" -> {
                if (!validateWorld(player, world)) return true;
                if (!MoonUniverse.getWorldMoons().contains(world)) {
                    player.sendMessage(lang.msg("m.worldNotMoon"));
                    return true;
                }
                long remainingTicks = 22500L - world.getTime();
                int remainingSeconds = (int) (remainingTicks / 20);
                player.sendMessage(String.format(lang.msg("m.remains"), remainingSeconds));
                return true;
            }
            case "alldungeons" -> {
                int i = 1;
                for (BloodDungeon dungeon : BloodDungeon.getDungeonList()) {
                    if (dungeon.getLocation() == null) continue;
                    Location loc = dungeon.getLocation();
                    String msg = ChatColor.translateAlternateColorCodes('&',
                            String.format("#%d: &e%s &7-> &a[%d, %d, %d]",
                                    i++, dungeon.getId(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
                    TextComponent component = new TextComponent(msg);
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                            "/teleport " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ()));
                    player.spigot().sendMessage(component);
                }
                return true;
            }
            case "help" -> {
                player.sendMessage(lang.msg("m.helpMessage"));
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    private boolean validateWorld(Player player, World world) {
        if (world == null) {
            player.sendMessage(lang.msg("m.worldNotFind"));
            return false;
        }
        if (world.getEnvironment() != World.Environment.NORMAL) {
            player.sendMessage(lang.msg("m.worldNotNormal"));
            return false;
        }
        return true;
    }
}

