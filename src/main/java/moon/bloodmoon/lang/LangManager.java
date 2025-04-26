package moon.bloodmoon.lang;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import moon.bloodmoon.BloodMoon;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class LangManager {
    public BloodMoon getInstance() {
        return this.instance;
    }

    private BloodMoon instance = BloodMoon.getInstance();

    private File folder = this.instance.getServer().getPluginManager().getPlugin(this.instance.getName()).getDataFolder();

    public void setupFiles() {
        File lang = new File(this.folder, "lang");
        File dungeos = new File(this.folder, "dungeos");
        String[] languages = { "ru.yml", "en.yml", "de.yml", "fr.yml", "ko.yml", "zh.yml", "it.yml", "es.yml","ar.yml","jp.yml","pt.yml","hi.yml"};
        for (String langFile : languages) {
            if (this.instance.getResource("lang/" + langFile) != null) {
                this.instance.saveResource("lang/" + langFile, false);
            } else {
                Bukkit.getLogger().warning("Resource lang/" + langFile + " not found in JAR!");
            }
        }
        if (!lang.exists())
            lang.mkdirs();
        if (!dungeos.exists())
            dungeos.mkdirs();
    }

    public String msg(String path) {
        File config = getFather();
        new ColorUtil();
        return ColorUtil.format(YamlConfiguration.loadConfiguration(config).getString(path));
    }

    public List<String> msg_lore(String path) {
        File config = getFather();
        return YamlConfiguration.loadConfiguration(config).getStringList(path);
    }

    private File getFather() {
        List<File> files = Arrays.asList(this.folder.listFiles());
        for (File Ffile : files) {
            if (Ffile.getName().equals("lang"))
                for (File file : Ffile.listFiles()) {
                    if (file.getName().equals(BloodMoon.getInstance().getConfig().getString("lang")))
                        return file;
                }
        }
        return null;
    }
}
