package me.vencorr;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public final class PetProtect extends JavaPlugin {

    FileConfiguration config = this.getConfig();
    // Plugin Configs
    public boolean skeletonProtect;
    public boolean skeletonLog;

    @Override
    public void onEnable()
    {
        this.saveDefaultConfig();
        config.options().copyDefaults(true);
        saveConfig();

        if (!config.getBoolean("enabled")) {
            getServer().getConsoleSender().sendMessage("PetProtect disabled in config. Set 'enabled' to true to use PetProtect.");
            getServer().getPluginManager().disablePlugin(this);
        }
        skeletonProtect = config.getBoolean("protect-skeleton-horses");
        skeletonLog = config.getBoolean("log-skeleton-horses");
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }

    public void SkeletonLog(Player ply, Entity ent, Location loc, String action) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        BufferedWriter output = new BufferedWriter(new FileWriter(getDataFolder().getAbsolutePath() + "/skeleton-horses.log", true));
        output.append(dtf.format(now)).append(": Entity ").append(String.valueOf(ent.getEntityId())).append(" located at ").append(loc.toString()).append(" was interacted (").append(action).append(") with by ").append(ply.getName());
        output.newLine();
        output.close();
    }

}
