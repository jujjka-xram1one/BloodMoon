package moon.bloodmoon.configuration;

import dev.lone.itemsadder.api.ItemsAdder;
import moon.bloodmoon.BloodMoon;
import moon.bloodmoon.platform.Ability;
import moon.bloodmoon.platform.MoonMob;
import moon.bloodmoon.platform.Weapon;
import moon.bloodmoon.platform.other.BloodDrop;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.MMOItemsAPI;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.comp.mmocore.MMOCoreHook;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class CopyingConfig {
    private final FileConfiguration config = BloodMoon.getInstance().getConfig();

    /**
     * This method reads the configuration for mobs and returns a list of MoonMobs.
     * It checks if the configuration contains "mobs", and then iterates over the list of mob entries.
     * For each entry, it collects data such as health, damage, abilities, and drops, and creates MoonMob objects.
     *
     * @return A list of MoonMobs based on the config.
     */
    public List<MoonMob> copy() {
        try {
            // Check if the configuration contains the "mobs" section
            if (this.config.contains("mobs")) {
                List<MoonMob> moonMobs = new ArrayList<>();
                MemorySection mobSection = (MemorySection) this.config.get("mobs");

                // Iterate over each mob in the config
                for (String set : mobSection.getKeys(false)) {
                    if (set != null && MoonMob.mobs.valueOf(set) != null) {
                        EntityType type = EntityType.valueOf(set);
                        String mob = null;
                        boolean nameVisible = mobSection.getBoolean(set + ".nameVisible");
                        int health = mobSection.getInt(set + ".health");
                        int damage = mobSection.getInt(set + ".damage");
                        double speed = mobSection.getDouble(set + ".speed");
                        double attackSpeed = mobSection.getDouble(set + ".speed_attack");
                        double chance = mobSection.getDouble(set + ".chance");
                        String name = mobSection.getString(set + ".name");
                        String mythic = mobSection.getString(set + ".mythic");
                        String weaponName = mobSection.getString(set + ".weapon.hand");
                        ItemStack stack = null;

                        // If weapon name exists, get the associated item stack
                        if (weaponName != null) stack = Weapon.getObject(weaponName);

                        // Initialize lists for abilities and potion effects
                        List<Ability> abilities = new ArrayList<>();
                        List<PotionEffectType> effectTypes = new ArrayList<>();

                        // Add effects based on the configuration
                        for (String s : mobSection.getStringList(set + ".effects")) {
                            if (PotionEffectType.getByName(s) != null) effectTypes.add(PotionEffectType.getByName(s));
                        }

                        // Add abilities to the mob if they are defined in the config
                        if (mobSection.contains(set + ".weapon.ability")) {
                            MemorySection abilitySection = (MemorySection) mobSection.get(set + ".weapon.ability");
                            for (String abilitySet : abilitySection.getKeys(false)) {
                                if (abilitySet != null && contains(abilitySet)) {
                                    Ability ability = get(abilitySet);
                                    double abilityChance = abilitySection.getDouble(abilitySet + ".chance");
                                    ability.setChance(abilityChance);
                                    abilities.add(ability);
                                }
                            }
                        }

                        // Add drops based on the configuration
                        List<BloodDrop> drops = new ArrayList<>();
                        if (mobSection.contains(set + ".drops")) {
                            MemorySection dropSection = (MemorySection) mobSection.get(set + ".drops");
                            for (String dropSet : dropSection.getKeys(false)) {
                                if (dropSet != null) {
                                    double dropChance = dropSection.getDouble(dropSet + ".chance");
                                    boolean isIA = dropSection.getBoolean(dropSet + ".IA");
                                    boolean isMI = dropSection.getBoolean(dropSet + ".MI");
                                    String stackName = dropSection.getString(dropSet + ".ItemStack");
                                    int amount = dropSection.getInt(dropSet + ".Amount");
                                    ItemStack material = null;

                                    // Determine the material based on the drop settings
                                    if (!isIA && !isMI) {
                                        if (Material.matchMaterial(stackName) != null) {
                                            material = new ItemStack(Material.matchMaterial(stackName), amount);
                                        }
                                    } else {
                                        if (isIA) {
                                            if (BloodMoon.getInstance().isItemsAdderEnabled() && ItemsAdder.getCustomItem(stackName) != null) {
                                                material = ItemsAdder.getCustomItem(stackName);
                                                material.setAmount(amount);
                                            }
                                        }
                                        if (isMI) {
                                            if (MMOItems.plugin.getMMOItem(Type.get(stackName), stackName) != null) {
                                                material = MMOItems.plugin.getMMOItem(Type.get(stackName), stackName).getType().getItem();
                                                material.setAmount(amount);
                                            }
                                        }
                                    }
                                    // Add the drop to the list if the material was found
                                    if (material != null) drops.add(new BloodDrop(dropChance, material));
                                }
                            }
                        }

                        // Create and add the MoonMob to the list
                        MoonMob moonMob = new MoonMob(name, nameVisible, health, damage, speed, attackSpeed, chance, new Weapon(stack), abilities, drops, mob, effectTypes, mythic);
                        moonMob.setType(type);
                        moonMobs.add(moonMob);
                    }
                }
                return moonMobs;
            }
            return null;
        } catch (Throwable ex) {
            // Throw any exception encountered during the process
            throw ex;
        }
    }

    /**
     * Checks if the given ability set exists.
     * @param set The ability set to check.
     * @return True if the set exists in the Ability enum, false otherwise.
     */
    private boolean contains(String set) {
        for (Ability ability : Ability.values()) {
            if (ability.name().equalsIgnoreCase(set)) return true;
        }
        return false;
    }

    /**
     * Retrieves an Ability object based on the given string name.
     * @param set The name of the ability.
     * @return The Ability object corresponding to the name.
     */
    private Ability get(String set) {
        for (Ability ability : Ability.values()) {
            if (ability.name().equalsIgnoreCase(set.toUpperCase())) return ability;
        }
        return null;
    }
}
