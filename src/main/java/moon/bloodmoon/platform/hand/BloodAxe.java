package moon.bloodmoon.platform.hand;

import moon.bloodmoon.lang.LangManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BloodAxe {
    private ItemStack itemStack = new ItemStack(Material.STONE_AXE);

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public BloodAxe() {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setCustomModelData(246353);
        meta.setDisplayName((new LangManager()).msg("items.bloodAxe"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS);
        meta.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
        this.itemStack.setItemMeta(meta);
    }
}
