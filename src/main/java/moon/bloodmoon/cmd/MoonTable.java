package moon.bloodmoon.cmd;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class MoonTable implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.isOp()) return null;

        List<String> suggestions = new ArrayList<>();

        switch (args.length) {
            case 1:
                addBasicCommands(suggestions);
                break;
            case 2:
                addWorldNames(suggestions, args[0]);
                break;
            default:
                return null;
        }

        return suggestions;
    }

    // Method to add basic commands to the suggestions list
    private void addBasicCommands(List<String> list) {
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
    }

    // Method to add available world names if the first command requires it
    private void addWorldNames(List<String> list, String command) {
        if ("startTimer".equalsIgnoreCase(command) ||
                "cancelTimer".equalsIgnoreCase(command) ||
                "createMoon".equalsIgnoreCase(command) ||
                "deleteMoon".equalsIgnoreCase(command) ||
                "allDungeons".equalsIgnoreCase(command) ||
                "info".equalsIgnoreCase(command)) {

            for (World world : Bukkit.getWorlds()) {
                if (world.getEnvironment() == World.Environment.NORMAL) {
                    list.add(world.getName());
                }
            }
        }
    }
}
