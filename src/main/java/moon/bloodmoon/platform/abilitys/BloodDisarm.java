package moon.bloodmoon.platform.abilitys;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import moon.bloodmoon.BloodMoon;
import moon.bloodmoon.lang.ColorUtil;
import moon.bloodmoon.lang.LangManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BloodDisarm {
    private static final Set<Player> disarmedPlayers = new HashSet<>();
    private final BloodMoon instance = BloodMoon.getInstance();

    public void disarm(Player player) {
        if (disarmedPlayers.contains(player)) {
            return;
        }

        disarmedPlayers.add(player);

        String disarmMessage = new LangManager().msg("m.disarm");
        player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(ColorUtil.format(disarmMessage))
        );

        new BukkitRunnable() {
            @Override
            public void run() {
                disarmedPlayers.remove(player);
                Bukkit.getLogger().info("Player " + player.getName() + " is no longer disarmed.");
            }
        }.runTaskLater(instance, 200L);
    }

    public static Set<Player> getDisarmedPlayers() {
        return disarmedPlayers;
    }
}