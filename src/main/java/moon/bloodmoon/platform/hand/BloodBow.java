package moon.bloodmoon.platform.hand;

import java.util.UUID;
import moon.bloodmoon.BloodMoon;
import moon.bloodmoon.lang.LangManager;
import moon.bloodmoon.platform.Weapon;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BloodBow {
    private ItemStack itemStack;
    private int damage = Weapon.getDamage(Weapon.Hand.BLOOD_BOW.name());

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public BloodBow() {
        this.itemStack = new ItemStack(Material.BOW);
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setCustomModelData(254773454);
        meta.setDisplayName((new LangManager()).msg("items.bloodBow"));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS);
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", this.damage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        if (!Weapon.getEnch(Weapon.Hand.BLOOD_BOW.name()).isEmpty())
            for (Enchantment e : Weapon.getEnch(Weapon.Hand.BLOOD_BOW.name()))
                meta.addEnchant(e, BloodMoon.getInstance().getConfig().getInt("powerItemEffect"), true);
        this.itemStack.setItemMeta(meta);
    }
}
