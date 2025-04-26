package moon.bloodmoon.platform;

import java.util.ArrayList;
import java.util.List;
import moon.bloodmoon.BloodMoon;
import moon.bloodmoon.platform.hand.BloodAxe;
import moon.bloodmoon.platform.hand.BloodBow;
import moon.bloodmoon.platform.hand.BloodPickaxe;
import moon.bloodmoon.platform.hand.BloodSword;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Weapon {
    private final ItemStack hand;

    public Weapon(ItemStack hand) {
        this.hand = hand;
    }

    public ItemStack getHand() {
        return this.hand;
    }

    public static int getDamage(String name) {
        return BloodMoon.getInstance().getConfig().getInt("weapon." + name.toLowerCase() + ".damage");
    }

    public static List<Enchantment> getEnch(String name) {
        List<Enchantment> enchantments = new ArrayList<>();
        for (String enchantmentName : BloodMoon.getInstance().getConfig().getStringList("weapon." + name.toLowerCase() + ".effects")) {
            Enchantment enchantment = Enchantment.getByName(enchantmentName);
            if (enchantment != null) {
                enchantments.add(enchantment);
            }
        }
        return enchantments;
    }

    public static ItemStack getObject(String name) {
        switch (name.toLowerCase()) {
            case "blood_bow":
                return new BloodBow().getItemStack();
            case "blood_sword":
                return new BloodSword().getItemStack();
            case "blood_pickaxe":
                return new BloodPickaxe().getItemStack();
            case "blood_axe":
                return new BloodAxe().getItemStack();
            default:
                return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Weapon)) return false;
        Weapon weapon = (Weapon) o;
        return hand.equals(weapon.hand);
    }

    @Override
    public int hashCode() {
        return hand.hashCode();
    }

    @Override
    public String toString() {
        return "Weapon(hand=" + hand + ")";
    }

    public enum Hand {
        BLOOD_BOW, BLOOD_SWORD, BLOOD_PICKAXE, BLOOD_AXE;
    }
}
