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

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Weapon))
            return false;
        Weapon other = (Weapon)o;
        if (!other.canEqual(this))
            return false;
        Object this$hand = getHand(), other$hand = other.getHand();
        return !((this$hand == null) ? (other$hand != null) : !this$hand.equals(other$hand));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Weapon;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $hand = getHand();
        return result * 59 + (($hand == null) ? 43 : $hand.hashCode());
    }

    public String toString() {
        return "Weapon(hand=" + getHand() + ")";
    }

    public ItemStack getHand() {
        return this.hand;
    }

    public static int getDamage(String name) {
        return BloodMoon.getInstance().getConfig().getInt("weapon." + name.toLowerCase() + ".damage");
    }

    public static List<Enchantment> getEnch(String name) {
        List<Enchantment> enchantments = new ArrayList<>();
        for (String s : BloodMoon.getInstance().getConfig().getStringList("weapon." + name.toLowerCase() + ".effects")) {
            if (Enchantment.getByName(s) != null)
                enchantments.add(Enchantment.getByName(s));
        }
        return enchantments;
    }

    public enum Hand {
        BLOOD_BOW, BLOOD_SWORD, BLOOD_PICKAXE, BLOOD_AXE;
    }

    public Weapon(ItemStack hand) {
        this.hand = hand;
    }

    public static ItemStack getObject(String name) {
        switch (name.toLowerCase()) {
            case "blood_bow":
                return (new BloodBow()).getItemStack();
            case "blood_sword":
                return (new BloodSword()).getItemStack();
            case "blood_pickaxe":
                return (new BloodPickaxe()).getItemStack();
            case "blood_axe":
                return (new BloodAxe()).getItemStack();
        }
        return null;
    }
}
