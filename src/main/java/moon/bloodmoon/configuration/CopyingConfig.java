package moon.bloodmoon.configuration;

import dev.lone.itemsadder.api.ItemsAdder;

import java.util.ArrayList;
import java.util.List;

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

public class CopyingConfig {
    private final FileConfiguration config = BloodMoon.getInstance().getConfig();

    public List<MoonMob> copy() {
        try {
            if (this.config.contains("mobs")) {
                List<MoonMob> moonMobs = new ArrayList<>();
                MemorySection mobSection = (MemorySection) this.config.get("mobs");
                for (String set : mobSection.getKeys(false)) {
                    if (set != null && MoonMob.mobs.valueOf(set) != null) {
                        EntityType type = EntityType.valueOf(set);
                        String mob = null;
                        boolean nameVisible = mobSection.getBoolean(set + ".nameVisible");
                        int health = mobSection.getInt(set + ".health");
                        int damage = mobSection.getInt(set + ".damage");
                        double speed = mobSection.getDouble(set + ".speed");
                        double attack_speed = mobSection.getDouble(set + ".speed_attack");
                        double chance = mobSection.getDouble(set + ".chance");
                        String name = mobSection.getString(set + ".name");
                        String mythic = mobSection.getString(set + ".mythic");
                        String weaponName = mobSection.getString(set + ".weapon.hand");
                        ItemStack stack = null;
                        if (weaponName != null) stack = Weapon.getObject(weaponName);
                        List<Ability> abilities = new ArrayList<>();
                        List<PotionEffectType> effectTypes = new ArrayList<>();
                        for (String s : mobSection.getStringList(set + ".effects")) {
                            if (PotionEffectType.getByName(s) != null) effectTypes.add(PotionEffectType.getByName(s));
                        }
                        if (mobSection.contains(set + ".weapon.ability")) {
                            MemorySection abySection = (MemorySection) mobSection.get(set + ".weapon.ability");
                            for (String aby_set : abySection.getKeys(false)) {
                                if (aby_set != null && contains(aby_set)) {
                                    Ability ability = get(aby_set);
                                    double aby_chance = abySection.getDouble(aby_set + ".chance");
                                    ability.setChance(aby_chance);
                                    abilities.add(ability);
                                }
                            }
                        }
                        List<BloodDrop> drop = new ArrayList<>();
                        if (mobSection.contains(set + ".drops")) {
                            MemorySection dropSection = (MemorySection) mobSection.get(set + ".drops");
                            for (String drop_set : dropSection.getKeys(false)) {
                                if (drop_set != null) {
                                    double Dchance = dropSection.getDouble(drop_set + ".chance");
                                    boolean ia = dropSection.getBoolean(drop_set + ".IA");
                                    boolean mi = dropSection.getBoolean(drop_set + ".MI");
                                    String stackName = dropSection.getString(drop_set + ".ItemStack");
                                    int amount = dropSection.getInt(drop_set + ".Amount");
                                    ItemStack material = null;
                                    if (!ia && !mi) {
                                        if (Material.matchMaterial(stackName) != null)
                                            material = new ItemStack(Material.matchMaterial(stackName), amount);
                                    } else {
                                        if (ia) {
                                            if (BloodMoon.getInstance().isITEMS_ADDER_DEPENDENCY() && ItemsAdder.getCustomItem(stackName) != null) {
                                                material = ItemsAdder.getCustomItem(stackName);
                                                material.setAmount(amount);
                                            }
                                        }
                                        if (mi) {
                                            if (MMOItems.plugin.getMMOItem(Type.get(stackName), stackName) != null) {
                                                material = MMOItems.plugin.getMMOItem(Type.get(stackName), stackName).getType().getItem();
                                                material.setAmount(amount);
                                            }
                                        }
                                    }
                                    if (material != null) drop.add(new BloodDrop(Dchance, material));
                                }
                            }
                        }
                        MoonMob moonMob = new MoonMob(name, nameVisible, health, damage, speed, attack_speed, chance, new Weapon(stack), abilities, drop, mob, effectTypes, mythic);
                        moonMob.setType(type);
                        moonMobs.add(moonMob);
                    }
                }
                return moonMobs;
            }
            return null;
        } catch (Throwable $ex) {
            throw $ex;
        }
    }

    private boolean contains(String set) {
        for (Ability a : Ability.values()) {
            if (a.name().equalsIgnoreCase(set)) return true;
        }
        return false;
    }

    private Ability get(String set) {
        for (Ability a : Ability.values()) {
            if (a.name().equalsIgnoreCase(set.toUpperCase())) return a;
        }
        return null;
    }
}
