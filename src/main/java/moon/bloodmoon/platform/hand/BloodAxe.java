package moon.bloodmoon.platform.hand;

import moon.bloodmoon.lang.LangManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BloodAxe {
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    private ItemStack itemStack = new ItemStack(Material.STONE_AXE);

    public BloodAxe() {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setCustomModelData(Integer.valueOf(246353));
        meta.setDisplayName((new LangManager()).msg("items.bloodAxe"));
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DESTROYS });
        meta.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
        this.itemStack.setItemMeta(meta);
    }
}
