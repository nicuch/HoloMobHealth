package com.loohp.holomobhealth;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import com.loohp.holomobhealth.Updater.Updater;
import com.loohp.holomobhealth.Utils.EntityTypeUtils;

import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!label.equalsIgnoreCase("holomobhealth") && !label.equalsIgnoreCase("hmh")) {
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.AQUA + "HoloMobHealth written by LOOHP!");
            sender.sendMessage(ChatColor.GOLD + "You are running HoloMobHealth version: " + HoloMobHealth.plugin.getDescription().getVersion());
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("holomobhealth.reload")) {
                HoloMobHealth.plugin.reloadConfig();
                HoloMobHealth.loadConfig();
                EntityTypeUtils.reloadLang();
                sender.sendMessage(HoloMobHealth.ReloadPlugin);
                for (World world : Bukkit.getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        String name = "";
                        if (entity.getCustomName() != null) {
                            if (!entity.getCustomName().equals("")) {
                                name = entity.getCustomName();
                            }
                        }
                        boolean visible = entity.isCustomNameVisible();
                        entity.setCustomName(ChatColor.RED + "" + ChatColor.RED + "" + ChatColor.RED + "" + ChatColor.RED + "");
                        entity.setCustomName(name);
                        entity.setCustomNameVisible(!visible);
                        entity.setCustomNameVisible(visible);
                    }
                }
            } else {
                sender.sendMessage(HoloMobHealth.NoPermission);
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("update")) {
            if (sender.hasPermission("holomobhealth.update")) {
                sender.sendMessage(ChatColor.AQUA + "[HoloMobHealth] HoloMobHealth written by LOOHP!");
                sender.sendMessage(ChatColor.GOLD + "[HoloMobHealth] You are running HoloMobHealth version: " + HoloMobHealth.plugin.getDescription().getVersion());
                new BukkitRunnable() {
                    public void run() {
                        String version = Updater.checkUpdate();
                        if (version.equals("latest")) {
                            sender.sendMessage(ChatColor.GREEN + "[HoloMobHealth] You are running the latest version!");
                        } else {
                            Updater.sendUpdateMessage(version);
                        }
                    }
                }.runTaskAsynchronously(HoloMobHealth.plugin);
            } else {
                sender.sendMessage(HoloMobHealth.NoPermission);
            }
            return true;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Bukkit.spigot().getConfig().getString("messages.unknown-command")));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> tab = new ArrayList<String>();
        if (!label.equalsIgnoreCase("holomobhealth") && !label.equalsIgnoreCase("hmh")) {
            return tab;
        }

        if (args.length <= 1) {
            if (sender.hasPermission("holomobhealth.reload")) {
                tab.add("reload");
            }
            if (sender.hasPermission("holomobhealth.update")) {
                tab.add("update");
            }
            return tab;
        }
        return tab;
    }

}
