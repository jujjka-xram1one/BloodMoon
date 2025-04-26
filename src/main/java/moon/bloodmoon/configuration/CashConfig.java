package moon.bloodmoon.configuration;

import moon.bloodmoon.BloodMoon;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class CashConfig {

    private final File cashFile;
    private final FileConfiguration cashConfig;

    public CashConfig() {
        BloodMoon plugin = BloodMoon.getInstance();
        this.cashFile = new File(plugin.getDataFolder(), "cash.yml");

        if (!cashFile.exists()) {
            plugin.saveResource("cash.yml", false);
        }

        this.cashConfig = YamlConfiguration.loadConfiguration(cashFile);
    }

    public FileConfiguration getCashYml() {
        return cashConfig;
    }

    public File getFather() {
        return cashFile;
    }

    public static void saveConfigFile(FileConfiguration config) {
        try {
            File file = new File(
                    Objects.requireNonNull(BloodMoon.getInstance()).getDataFolder(),
                    "cash.yml"
            );
            config.save(file);
        } catch (IOException e) {
            BloodMoon.getInstance().getLogger().severe("Failed to save cash.yml");
            e.printStackTrace();
        }
    }
}
