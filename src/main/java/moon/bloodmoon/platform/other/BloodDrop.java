package moon.bloodmoon.platform.other;

import org.bukkit.inventory.ItemStack;

public class BloodDrop {
    private final double chance;

    private final ItemStack stack;

    public BloodDrop(double chance, ItemStack stack) {
        this.chance = chance;
        this.stack = stack;
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof BloodDrop))
            return false;
        BloodDrop other = (BloodDrop)o;
        if (!other.canEqual(this))
            return false;
        if (Double.compare(getChance(), other.getChance()) != 0)
            return false;
        Object this$stack = getStack(), other$stack = other.getStack();
        return !((this$stack == null) ? (other$stack != null) : !this$stack.equals(other$stack));
    }

    protected boolean canEqual(Object other) {
        return other instanceof BloodDrop;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $chance = Double.doubleToLongBits(getChance());
        result = result * 59 + (int)($chance >>> 32L ^ $chance);
        Object $stack = getStack();
        return result * 59 + (($stack == null) ? 43 : $stack.hashCode());
    }

    public String toString() {
        return "BloodDrop(chance=" + getChance() + ", stack=" + getStack() + ")";
    }

    public double getChance() {
        return this.chance;
    }

    public ItemStack getStack() {
        return this.stack;
    }
}
