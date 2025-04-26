package moon.bloodmoon.configuration;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import moon.bloodmoon.BloodMoon;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CashConfig {
    public BloodMoon getInstance() {
        return this.instance;
    }

    private BloodMoon instance = BloodMoon.getInstance();

    private File folder = this.instance.getServer().getPluginManager().getPlugin(this.instance.getName()).getDataFolder();

    public FileConfiguration getCashYml() {
        return this.cashYml;
    }

    private FileConfiguration cashYml = (FileConfiguration)YamlConfiguration.loadConfiguration(getFather());

    public File getFather() {
        List<File> files = Arrays.asList(this.folder.listFiles());
        for (File Ffile : files) {
            if (Ffile.getName().equals("cash.yml"))
                return Ffile;
        }
        return null;
    }
}
