package com.homanti.wacorp.commands.emergency_services;

import java.util.Arrays;
import java.util.List;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.homanti.wacorp.Plugin.*;

public class firefightersCMD implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof BlockCommandSender)) {
            Player player = (Player) sender;

            if (!isHoldingPhone(player)) {
                player.sendMessage("Вы должны держать телефон в руке");
                return true;

            } else if (args.length < 1) {
                return false;

            } else {
                List<Player> playersWithPermission = getPlayersWithPermission("wacorp.firefighters");

                if (playersWithPermission.isEmpty()) {
                    player.sendMessage("МЧС нет онлайн.");
                    return true;

                } else {
                    String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
                    sender.sendMessage("Вы вызвали МЧС: " + message);

                    for (Player target : playersWithPermission) {
                        if (!target.equals(sender)) {
                            String player_name = player.getName();
                            target.sendMessage("Вызов МЧС от " + player_name + ": " + message);
                        }
                    }
                    return true;
                }
            }
        } else {
            List<Player> playersWithPermission = getPlayersWithPermission("wacorp.firefighters");

            if (playersWithPermission.isEmpty()) {
                return true;

            } else {
                String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

                for (Player target : playersWithPermission) {
                    target.sendMessage(message);
                }

                return false;
            }
        }
    }
}
