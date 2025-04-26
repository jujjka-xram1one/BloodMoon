package moon.bloodmoon.items;

import moon.bloodmoon.BloodMoon;
import moon.bloodmoon.lang.LangManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BloodPearl {
    public static double getChance() {
        return chance;
    }

    public static boolean isCrash() {
        return crash;
    }

    private static double chance = BloodMoon.getInstance().getConfig().getDouble("items.bloodPearl.chance");

    public static double getChanceDrop() {
        return chanceDrop;
    }
    private static boolean crash = BloodMoon.getInstance().getConfig().getBoolean("items.bloodPearl.crash");
    private static double chanceDrop = BloodMoon.getInstance().getConfig().getDouble("items.bloodPearl.chanceDrop");

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    private ItemStack itemStack = new ItemStack(Material.CHICKEN_SPAWN_EGG);

    public BloodPearl() {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
        meta.setDisplayName((new LangManager()).msg("items.bloodPearl"));
        meta.setLore((new LangManager()).msg_lore("items.bloodPearl.lore"));
        meta.setCustomModelData(Integer.valueOf(686354257));
        this.itemStack.setItemMeta(meta);
    }
}
