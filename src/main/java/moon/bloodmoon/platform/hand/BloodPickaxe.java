package moon.bloodmoon.platform.hand;

import moon.bloodmoon.lang.LangManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BloodPickaxe {
    private ItemStack itemStack = new ItemStack(Material.DIAMOND_PICKAXE);

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public BloodPickaxe() {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setCustomModelData(765545343);
        meta.setDisplayName((new LangManager()).msg("items.bloodPick"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS);
        meta.addEnchant(Enchantment.DIG_SPEED, 3, true);
        this.itemStack.setItemMeta(meta);
    }
}
