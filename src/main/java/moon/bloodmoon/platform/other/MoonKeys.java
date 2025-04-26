package moon.bloodmoon.platform.other;

import moon.bloodmoon.BloodMoon;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public class MoonKeys {
    public static final NamespacedKey key = new NamespacedKey(BloodMoon.getInstance(), "moonId");
    public static final NamespacedKey keyU = new NamespacedKey(BloodMoon.getInstance(), "moonUUID");
    public static final NamespacedKey keyA = new NamespacedKey(BloodMoon.getInstance(), "moonBarrow");
}
