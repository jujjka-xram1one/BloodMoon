package moon.bloodmoon.listeners;

import java.util.Random;
import moon.bloodmoon.BloodMoon;
import moon.bloodmoon.MoonUniverse;
import moon.bloodmoon.platform.Ability;
import moon.bloodmoon.platform.MoonMob;
import moon.bloodmoon.platform.abilitys.BloodArrow;
import moon.bloodmoon.platform.other.MobUtil;
import moon.bloodmoon.platform.other.MoonKeys;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameRule;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.persistence.PersistentDataType;

public class ParticleListener implements Listener {
    private MobUtil mobUtil = new MobUtil();

    @EventHandler
    public void beat_arrow(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        if (!MoonUniverse.getWorldMoons().contains(event.getLocation().getWorld()))
            return;
        if (projectile instanceof Arrow) {
            Arrow arrow = (Arrow)projectile;
            LivingEntity s = (LivingEntity)arrow.getShooter();
            MoonMob moonMob = this.mobUtil.getMob(s.getType());
            if (moonMob == null)
                return;
            if (!moonMob.getAbilitys().contains(Ability.BLOOD_BEATING_ARROW))
                return;
            double chance = this.mobUtil.getAbility(moonMob, Ability.BLOOD_BEATING_ARROW).getChance();
            if (!MoonUniverse.random(chance))
                return;
            arrow.getPersistentDataContainer().set(MoonKeys.keyA, PersistentDataType.INTEGER, Integer.valueOf(2));
            (new BloodArrow()).arrow_part(arrow);
        }
    }

    @EventHandler
    public void particle(EntityDamageEvent event) {
        World world = event.getEntity().getWorld();
        if (!MoonUniverse.getWorldMoons().contains(world))
            return;
        Particle.DustOptions options = new Particle.DustOptions(getColor(BloodMoon.getInstance().getConfig().getString("colorDamageParticle")), 1.0F);
        world.spawnParticle(Particle.valueOf(BloodMoon.getInstance().getConfig().getString("particle")), event.getEntity().getLocation().clone().add(0.0D, 1.0D, 0.0D), BloodMoon.getInstance().getConfig().getInt("amountDamageParticle"), 0.1D, 0.1D, 0.1D, 0.1D, options);
        if (BloodMoon.getInstance().getConfig().getBoolean("damageSound") && (
                new Random()).nextDouble() <= 0.2D) {
            world.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, Boolean.valueOf(false));
            Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "playsound minecraft:damagemoon block @a 0 0 0 1 1 1");
        }
    }

    public Color getColor(String colorName) {
        if (colorName != null) {
            switch (colorName.toUpperCase()) {
                case "WHITE":
                    return Color.WHITE;
                case "SILVER":
                    return Color.SILVER;
                case "GRAY":
                    return Color.GRAY;
                case "BLACK":
                    return Color.BLACK;
                case "RED":
                    return Color.RED;
                case "MAROON":
                    return Color.MAROON;
                case "YELLOW":
                    return Color.YELLOW;
                case "OLIVE":
                    return Color.OLIVE;
                case "LIME":
                    return Color.GREEN;
                case "GREEN":
                    return Color.GREEN;
                case "AQUA":
                    return Color.AQUA;
                case "TEAL":
                    return Color.TEAL;
                case "BLUE":
                    return Color.BLUE;
                case "NAVY":
                    return Color.NAVY;
                case "FUCHSIA":
                    return Color.FUCHSIA;
                case "PURPLE":
                    return Color.PURPLE;
                case "ORANGE":
                    return Color.ORANGE;
            }
            return null;
        }
        return null;
    }
}
