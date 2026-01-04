package com.homanti.wacorp.commands.telegram;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class tellCMD implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может использовать только игрок.");
            return true;

        } else if (args.length < 2) {
            return false;

        } else {
            Player target = Bukkit.getServer().getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage("Игрок не найден!");
                return true;
            }

            String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

            Location targetLocation = target.getLocation();
            Location senderLocation = ((Player) sender).getLocation();
            double distance = senderLocation.distance(targetLocation);

            if (distance > 4.0) {
                sender.sendMessage("Вы должны быть в радиусе 4 блоков от цели!");
                return true;
            }

            String senderName = sender.getName();
            String targetName = target.getName();

            sender.sendMessage(String.format("Вы прошептали %s: %s", targetName, message));
            target.sendMessage(String.format("%s прошептал вам: %s", senderName, message));

            return true;
        }
    }
}
