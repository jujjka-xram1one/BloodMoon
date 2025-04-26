package moon.bloodmoon.platform.other;

import org.bukkit.inventory.ItemStack;

public class BloodDrop {
    private final double chance;
    private final ItemStack stack;

    public BloodDrop(double chance, ItemStack stack) {
        this.chance = chance;
        this.stack = stack;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof BloodDrop)) return false;
        BloodDrop other = (BloodDrop) o;
        return Double.compare(getChance(), other.getChance()) == 0 && getStack().equals(other.getStack());
    }

    @Override
    public int hashCode() {
        int result = 1;
        long chanceBits = Double.doubleToLongBits(getChance());
        result = 59 * result + (int) (chanceBits ^ (chanceBits >>> 32));
        result = 59 * result + getStack().hashCode();
        return result;
    }

    @Override
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
