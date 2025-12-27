package com.homanti.wacorp.commands.cuff;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class uncuffCMD implements CommandExecutor {
    private final JavaPlugin plugin;

    public uncuffCMD(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Только игрок может использовать эту команду.");
            return true;
        }

        if (args.length == 0) {
            return false;
        }

        Player playerSender = (Player) sender;
        Player target = Bukkit.getServer().getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("Игрок не найден!");
            return true;
        }

        if (playerSender.equals(target)) {
            sender.sendMessage("Вы не можете снять наручники сами с себя");
            return true;
        }

        if (!isWithinDistance(playerSender, target, 2.0)) {
            sender.sendMessage("Игрок слишком далеко.");
            return true;
        }

        if (!removeCuffedPlayer(target)) {
            sender.sendMessage("Игрок не был в наручниках.");
            return true;
        }

        target.setGameMode(GameMode.SURVIVAL);
        target.removePotionEffect(PotionEffectType.WEAKNESS);
        Bukkit.getServer().broadcastMessage("* " + playerSender.getName() + " снял наручники с " + target.getName() + " *");
        return true;
    }

    private boolean isWithinDistance(Player player1, Player player2, double distance) {
        Location location1 = player1.getLocation();
        Location location2 = player2.getLocation();
        return location1.distance(location2) <= distance;
    }

    private boolean removeCuffedPlayer(Player player) {
        File cuffedPlayersFile = new File(this.plugin.getDataFolder(), "cuffedPlayers.txt");
        File tempFile = new File(this.plugin.getDataFolder(), "tempCuffedPlayers.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(cuffedPlayersFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;
            boolean found = false;
            while ((currentLine = reader.readLine()) != null) {
                if (!currentLine.trim().equals(player.getName())) {
                    writer.write(currentLine + System.getProperty("line.separator"));
                } else {
                    found = true;
                }
            }

            if (!found) {
                tempFile.delete();
                return false;
            }

            return cuffedPlayersFile.delete() && tempFile.renameTo(cuffedPlayersFile);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
