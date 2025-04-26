package moon.bloodmoon.listeners;

import moon.bloodmoon.BloodMoon;
import moon.bloodmoon.MoonUniverse;
import moon.bloodmoon.items.BloodPearl;
import moon.bloodmoon.platform.abilitys.BloodArrow;
import moon.bloodmoon.platform.abilitys.BloodDisarm;
import moon.bloodmoon.platform.abilitys.BloodRitual;
import moon.bloodmoon.platform.other.MoonKeys;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class PlayerListener implements Listener {

    // Handles damage dealt by arrows
    @EventHandler
    public void shot(EntityDamageByEntityEvent event) {
        try {
            Entity damager = event.getDamager();
            Entity damaged = event.getEntity();

            // If damage is from an arrow to a player
            if (damager instanceof Arrow && damaged instanceof Player) {
                Arrow arrow = (Arrow) damager;
                Player player = (Player) damaged;

                // Ensure the player is in a valid Moon world
                if (!MoonUniverse.getWorldMoons().contains(player.getWorld()))
                    return;

                // Check if the arrow has special data for blood arrow effect
                if (!arrow.getPersistentDataContainer().has(MoonKeys.keyA, PersistentDataType.INTEGER))
                    return;

                // Trigger BloodArrow ability
                new BloodArrow().shot(player);
                return;
            }

            // If damage is to a non-player living entity and under Blood Ritual protection
            if (damaged instanceof LivingEntity && !(damaged instanceof Player)) {
                LivingEntity entity = (LivingEntity) damaged;
                if (!BloodRitual.getProtectEntities().containsKey(entity))
                    return;

                int all = BloodRitual.getProtectEntities().get(entity);

                // Cancel damage if it is below the protection threshold
                if (event.getDamage() > all) {
                    BloodRitual.getProtectEntities().remove(entity);
                    return;
                }

                // Decrease remaining protection
                all -= (int) event.getDamage();
                BloodRitual.getProtectEntities().put(entity, all);
                event.setCancelled(true);
            }
        } catch (Throwable $ex) {
            throw $ex;
        }
    }

    // Prevents disarmed players from using items
    @EventHandler
    public void damage(PlayerInteractEvent event) {
        event.getPlayer().setInvulnerable(false);
        if (!BloodDisarm.getDisarmedPlayers().contains(event.getPlayer()))
            return;

        if (event.hasItem())
            event.setCancelled(true);
    }

    // Handles interaction with Blood Pearl item
    @EventHandler
    public void pearl(PlayerInteractEvent event) {
        try {
            World world = event.getPlayer().getWorld();

            // Ensure the player has the Blood Pearl item with the correct model data
            if (!event.hasItem()) return;
            ItemStack item = event.getItem();
            if (!item.hasItemMeta() || !item.getItemMeta().hasCustomModelData()) return;
            if (item.getItemMeta().getCustomModelData() != 686354257) return;

            // Prevent item use and handle item removal if needed
            event.setCancelled(true);
            Player player = event.getPlayer();
            if (BloodPearl.isCrash()) {
                if (item.getAmount() == 1) {
                    player.getInventory().remove(item);
                } else {
                    item.setAmount(item.getAmount() - 1);
                }
            }
            player.updateInventory();

            // Start the moon event if conditions are met
            if (event.getAction() != Action.RIGHT_CLICK_AIR) return;
            if (!MoonUniverse.isNight(world.getTime())) return;
            if (MoonUniverse.getWorldMoons().contains(world)) return;
            if (MoonUniverse.randomChance(BloodPearl.getChance())) {
                BloodMoon.getInstance().getMoonUniverse().startMoon(world);
            }
        } catch (Throwable $ex) {
            throw $ex;
        }
    }

    // Blocks certain commands from non-OP players in Moon worlds
    @EventHandler
    public void block(PlayerCommandPreprocessEvent event) {
        if (!MoonUniverse.getWorldMoons().contains(event.getPlayer().getWorld())) return;
        if (!BloodMoon.getInstance().isBlockCmdEnabled()) return;
        if (!event.getPlayer().isOp()) event.setCancelled(true);
    }
}
