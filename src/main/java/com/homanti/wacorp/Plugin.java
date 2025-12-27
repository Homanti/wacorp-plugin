package com.homanti.wacorp;

import com.homanti.wacorp.Listener.ItemRenameListener;
import com.homanti.wacorp.commands.cuff.cuffCMD;
import com.homanti.wacorp.commands.cuff.uncuffCMD;
import com.homanti.wacorp.commands.emergency_services.ambulanceCMD;
import com.homanti.wacorp.commands.emergency_services.fbiCMD;
import com.homanti.wacorp.commands.emergency_services.firefightersCMD;
import com.homanti.wacorp.commands.telegram.telegramCMD;
import com.homanti.wacorp.commands.telegram.telegramanonCMD;
import com.homanti.wacorp.commands.telegram.tellCMD;
import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public final class Plugin extends JavaPlugin {
    public void onEnable() {
        this.getCommand("telegram").setExecutor(new telegramCMD());
        this.getCommand("telegramanon").setExecutor(new telegramanonCMD());
        this.getCommand("tell").setExecutor(new tellCMD());
        this.getCommand("cuff").setExecutor(new cuffCMD(this));
        this.getCommand("uncuff").setExecutor(new uncuffCMD(this));
        this.getCommand("fbi").setExecutor(new fbiCMD());
        this.getCommand("ambulance").setExecutor(new ambulanceCMD());
        this.getCommand("firefighters").setExecutor(new firefightersCMD());
        this.getServer().getPluginManager().registerEvents(new ItemRenameListener(), this);

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        File secretsFile = new File(getDataFolder(), "secrets.yml");

        if (!secretsFile.exists()) {
            try {
                secretsFile.createNewFile();
                FileWriter writer = new FileWriter(secretsFile);
                writer.write("# ВАЖНО: Заполните реальные значения!\n");
                writer.write("discord:\n");
                writer.write("  token: \"CHANGE_ME\"\n");
                writer.close();

                getLogger().severe("secrets.yml created! Please configure it and restart server.");
                getServer().getPluginManager().disablePlugin(this);
                return;
            } catch (IOException e) {
                getLogger().severe("Failed to create secrets.yml: " + e.getMessage());
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }

        FileConfiguration secrets = YamlConfiguration.loadConfiguration(secretsFile);
        String discordToken = secrets.getString("discord.token");

        if (discordToken == null || discordToken.isEmpty() || discordToken.equals("CHANGE_ME")) {
            getLogger().severe("Discord token is not configured in secrets.yml!");
            getLogger().severe("Please edit plugins/WacoRP-Plugin/secrets.yml and restart.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            DiscordBot.initialize(discordToken);
            getLogger().info("Discord bot successfully initialized!");
        } catch (LoginException e) {
            getLogger().severe("Failed to authorize Discord bot: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        } catch (InterruptedException e) {
            getLogger().severe("Discord bot initialization interrupted: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            Thread.currentThread().interrupt();
        }
    }

    public void onDisable() {
    }

    public static boolean isHoldingPhone(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        String itemId = itemInHand.getType().getKey().toString();

        return itemId.equals("wacorp:smartphone");
    }

    public static boolean hasPhone(Player player) {
        ItemStack[] items = player.getInventory().getContents();

        for (ItemStack item : items) {
            if (item != null && !item.getType().isAir()) {
                String itemId = item.getType().getKey().toString();

                if (itemId.equals("wacorp:smartphone")) {
                    return true;
                }
            }
        }

        return false;
    }

    public static List<Player> getPlayersWithPermission(String permission) {
        List<Player> playersWithPermission = new ArrayList();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(permission)) {
                playersWithPermission.add(player);
            }
        }

        return playersWithPermission;
    }

    public static void saveCuffedPlayer(Player player, JavaPlugin plugin) {
        File dataFolder = plugin.getDataFolder();

        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        File cuffedPlayersFile = new File(dataFolder, "cuffedPlayers.txt");

        try {
            FileWriter writer = new FileWriter(cuffedPlayersFile, true);
            writer.write(player.getName() + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}