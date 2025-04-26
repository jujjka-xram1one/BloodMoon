package moon.bloodmoon.platform.other;

import moon.bloodmoon.BloodMoon;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public class MoonKeys {
    public static NamespacedKey key = new NamespacedKey((Plugin)BloodMoon.getInstance(), "moonId");
    public static NamespacedKey keyU = new NamespacedKey((Plugin)BloodMoon.getInstance(), "moonUUID");
    public static NamespacedKey keyA = new NamespacedKey((Plugin)BloodMoon.getInstance(), "moonBarrow");
}
