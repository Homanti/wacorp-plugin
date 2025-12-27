package com.homanti.wacorp.commands.telegram;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

import static com.homanti.wacorp.Plugin.*;


public class telegramanonCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может использовать только игрок.");
            return true;
        }

        Player player = (Player) sender;

        if (!isHoldingPhone(player)) {
            player.sendMessage("Вы должны держать телефон в руке");
            return true;
        }

        if (args.length < 2) {
            return false;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("Игрок не найден!");
            return true;
        }

        if (!hasPhone(target)) {
            player.sendMessage("У получателя нету телефона");
            return true;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        player.sendMessage("Вы отправили анонимное сообщение " + sender.getName() + ": " + message);
        target.sendMessage("Сообщение Telegram от Анонимно" + ": " + message);

        return true;
    }
}
