package net.daechler.webhooksender;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;

public class WebhookSender extends JavaPlugin {

    // A HashMap to store webhooks and their URLs from the configuration.
    private HashMap<String, String> webhooks = new HashMap<>();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();  // Saves a copy of the default config.yml (included in the JAR) if it does not exist.

        for (String key : this.getConfig().getKeys(false)) {
            webhooks.put(key, this.getConfig().getString(key));
        }

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + this.getDescription().getName() + " has been enabled!");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + this.getDescription().getName() + " has been disabled!");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("webhook")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Incorrect usage! Please use /webhook <number> <message>");
                return true;
            }

            String webhookNum = args[0];
            if (!webhooks.containsKey(webhookNum)) {
                sender.sendMessage(ChatColor.RED + "No such webhook number! Check your config.yml");
                return true;
            }

            if (!sender.hasPermission("webhook." + webhookNum)) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this webhook.");
                return true;
            }

            String message = String.join(" ", args).substring(webhookNum.length() + 1);  // The rest of the command excluding the webhook number.

            try {
                WebhookUtils.sendWebhook(webhooks.get(webhookNum), message);
                sender.sendMessage(ChatColor.GREEN + "Webhook " + webhookNum + " has been successfully triggered!");
            } catch (IOException e) {
                sender.sendMessage(ChatColor.RED + "An error occurred when sending the webhook. Check your console for more details.");
                e.printStackTrace();
            }

            return true;
        }

        return false;
    }
}
