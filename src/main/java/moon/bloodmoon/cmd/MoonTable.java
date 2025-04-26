package moon.bloodmoon.cmd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class MoonTable implements TabCompleter {
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (sender.isOp()) {
            List<String> list;
            switch (args.length) {
                case 1:
                    list = new ArrayList();
                    list.add("startTimer");
                    list.add("cancelTimer");
                    list.add("createMoon");
                    list.add("deleteMoon");
                    list.add("amountMobs");
                    list.add("deleteMobs");
                    list.add("reload");
                    list.add("allDungeons");
                    list.add("info");
                    list.add("help");
                    return list;
                case 2:
                    list = new ArrayList<>();
                    if (args[0].equals("startTimer") || args[0].equals("cancelTimer") || args[0].equals("createMoon") || args[0].equals("deleteMoon") || args[0].equals("allDungeons") || args[0].equals("info")) {
                        Iterator<World> iterator = Bukkit.getWorlds().iterator();
                        if (iterator.hasNext()) {
                            World world = iterator.next();
                            if (world.getEnvironment() == World.Environment.NORMAL)
                                list.add(world.getName());
                            return list;
                        }
                    }
                    break;
            }
        }
        return null;
    }
}
