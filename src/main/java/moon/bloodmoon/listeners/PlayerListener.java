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
    @EventHandler
    public void shot(EntityDamageByEntityEvent event) {
        try {
            Entity damager = event.getDamager();
            Entity damaged = event.getEntity();
            if (damager instanceof Arrow && damaged instanceof Player) {
                Arrow arrow = (Arrow)damager;
                Player player = (Player)damaged;
                if (!MoonUniverse.getWorldMoons().contains(player.getWorld()))
                    return;
                if (!arrow.getPersistentDataContainer().has(MoonKeys.keyA, PersistentDataType.INTEGER))
                    return;
                (new BloodArrow()).shot(player);
                return;
            }
            if (!(damaged instanceof LivingEntity))
                return;
            if (damaged instanceof Player)
                return;
            LivingEntity entity = (LivingEntity)damaged;
            if (!BloodRitual.getProtectEntities().containsKey(entity))
                return;
            int all = ((Integer)BloodRitual.getProtectEntities().get(entity)).intValue();
            if (event.getDamage() > all) {
                BloodRitual.getProtectEntities().remove(entity);
                return;
            }
            all -= (int)event.getDamage();
            BloodRitual.getProtectEntities().remove(entity);
            BloodRitual.getProtectEntities().put(entity, Integer.valueOf(all));
            event.setCancelled(true);
        } catch (Throwable $ex) {
            throw $ex;
        }
    }

    @EventHandler
    public void damage(PlayerInteractEvent event) {
        event.getPlayer().setInvulnerable(false);
        if (!BloodDisarm.getDisarmedPlayers().contains(event.getPlayer()))
            return;
        if (event.hasItem())
            event.setCancelled(true);
    }

    @EventHandler
    public void pearl(PlayerInteractEvent event) {
        try {
            World world = event.getPlayer().getWorld();
            if (!event.hasItem())
                return;
            ItemStack item = event.getItem();
            if (!item.hasItemMeta())
                return;
            if (!item.getItemMeta().hasCustomModelData())
                return;
            if (item.getItemMeta().getCustomModelData() != 686354257)
                return;
            event.setCancelled(true);
            Player player = event.getPlayer();
            if(BloodPearl.isCrash()) {
                if (item.getAmount() == 1) {
                    player.getInventory().remove(item);
                } else {
                    item.setAmount(item.getAmount() - 1);
                }
            }
            player.updateInventory();
            if (event.getAction() != Action.RIGHT_CLICK_AIR)
                return;
            if (!MoonUniverse.isNight(world.getTime()))
                return;
            if (MoonUniverse.getWorldMoons().contains(world))
                return;
            if (MoonUniverse.random(BloodPearl.getChance()))
                BloodMoon.getInstance().getMoonUniverse().startMoon(world);
        } catch (Throwable $ex) {
            throw $ex;
        }
    }

    @EventHandler
    public void block(PlayerCommandPreprocessEvent event) {
        if (!MoonUniverse.getWorldMoons().contains(event.getPlayer().getWorld()))
            return;
        if (!BloodMoon.getInstance().isBLOCKED_CMD())
            return;
        if (!event.getPlayer().isOp())
            event.setCancelled(true);
    }
}
