package moon.bloodmoon.items;

import moon.bloodmoon.BloodMoon;
import moon.bloodmoon.lang.LangManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BloodPearl {
    // Configuration values for the BloodPearl item
    private static double chance = BloodMoon.getInstance().getConfig().getDouble("items.bloodPearl.chance");
    private static boolean crash = BloodMoon.getInstance().getConfig().getBoolean("items.bloodPearl.crash");
    private static double chanceDrop = BloodMoon.getInstance().getConfig().getDouble("items.bloodPearl.chanceDrop");

    private ItemStack itemStack = new ItemStack(Material.CHICKEN_SPAWN_EGG);

    // Constructor to set up the BloodPearl item
    public BloodPearl() {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);  // Hide item attributes
        meta.setDisplayName((new LangManager()).msg("items.bloodPearl"));  // Set display name
        meta.setLore((new LangManager()).msg_lore("items.bloodPearl.lore"));  // Set lore
        meta.setCustomModelData(686354257);  // Set custom model data
        this.itemStack.setItemMeta(meta);
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }
    public static double getChance() {
        return chance;
    }

    public static boolean isCrash() {
        return crash;
    }

    public static double getChanceDrop() {
        return chanceDrop;
    }
}
