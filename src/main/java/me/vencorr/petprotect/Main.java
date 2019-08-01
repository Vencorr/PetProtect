package me.vencorr.petprotect;

import me.vencorr.petprotect.util.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin {

    FileConfiguration config = this.getConfig();
    // Plugin Configs
    boolean actionbar;
    boolean hurt;
    boolean ride;
    boolean access;
    String message;
    String altmessage;
    boolean customname;

    @Override
    public void onEnable()
    {
        ActionBar.plugin = this;
        ActionBar.nmsver = Bukkit.getServer().getClass().getPackage().getName();
        ActionBar.nmsver = ActionBar.nmsver.substring(ActionBar.nmsver.lastIndexOf(".") + 1);
        this.saveDefaultConfig();
        config.options().copyDefaults(true);
        saveConfig();

        if (!config.getBoolean("enabled")) {
            getServer().getConsoleSender().sendMessage("Main disabled in config. Set 'enabled' to true to use Main.");
            getServer().getPluginManager().disablePlugin(this);
        }
        // Configurations
        actionbar = config.getBoolean("actionbar");
        hurt = config.getBoolean("hurt");
        ride = config.getBoolean("ride");
        access = config.getBoolean("access");
        message = config.getString("message");
        altmessage = config.getString("altmessage");
        customname = config.getBoolean("customname");


        getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }
}
