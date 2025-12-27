package com.homanti.wacorp.commands.cuff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import static com.homanti.wacorp.Plugin.saveCuffedPlayer;

public class cuffCMD implements CommandExecutor, Listener {
    private final JavaPlugin plugin;

    public cuffCMD(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Только игрок может использовать эту команду.");
            return true;

        } else if (args.length <= 0) {
            return false;

        } else {
            Player target = Bukkit.getServer().getPlayer(args[0]);

            if (target != null) {
                Player playerSender = (Player)sender;

                Location targetLocation = target.getLocation();
                Location senderLocation = playerSender.getLocation();
                double distance = senderLocation.distance(targetLocation);

                File dataFolder = this.plugin.getDataFolder();
                File cuffedPlayersFile = new File(dataFolder, "cuffedPlayers.txt");
                boolean isCuffed = false;

                try {
                    BufferedReader reader = new BufferedReader(new FileReader(cuffedPlayersFile));

                    String line;

                    while((line = reader.readLine()) != null) {
                        if (line.equals(target.getName())) {
                            isCuffed = true;
                            break;
                        }
                    }

                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (distance <= 2.0) {
                    if (!isCuffed) {
                        if (!playerSender.equals(target)) {
                            target.setGameMode(GameMode.ADVENTURE);
                            target.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(-1, 1));

                            Bukkit.getServer().broadcastMessage("* " + sender.getName() + " надел наручники на " + target.getName() + " *");
                            saveCuffedPlayer(target, plugin);

                            return true;

                        } else {
                            sender.sendMessage("Вы не можете надеть наручники на себя");
                            return true;
                        }

                    } else {
                        sender.sendMessage("Игрок уже в наручниках.");
                        return true;
                    }

                } else {
                    sender.sendMessage("Игрок слишком далеко.");
                    return true;
                }

            } else {
                sender.sendMessage("Игрок не найден!");
                return true;
            }

        }
    }
}
