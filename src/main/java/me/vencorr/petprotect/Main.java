package me.vencorr.petprotect;

import me.vencorr.petprotect.util.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;


public final class Main extends JavaPlugin {

    private FileConfiguration config = this.getConfig();
    // Plugin Configs
    boolean petinvulnerable;
    boolean ownerprotect;
    boolean hurt;
    boolean ride;
    boolean access;
    List<String> exclude;
    int msgtype;
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
            getServer().getConsoleSender().sendMessage("PetProtect disabled in config. Set 'enabled' to true to use PetProtect.");
            getServer().getPluginManager().disablePlugin(this);
        }
        // Configurations
        petinvulnerable = config.getBoolean("pet-invulnerable");
        ownerprotect = config.getBoolean("owner-protect");
        hurt = config.getBoolean("hurt");
        ride = config.getBoolean("ride");
        access = config.getBoolean("access");
        exclude = config.getStringList("exclude");
        msgtype = config.getInt("messagetype");
        message = config.getString("message");
        altmessage = config.getString("altmessage");
        customname = config.getBoolean("customname");
        if (msgtype > 2) {
            getServer().getConsoleSender().sendMessage("[PetProtect] Unable to properly assign '" + msgtype + "' from config file to messagetype. Defaulting to actionbar. Please fix this in the config.");
            msgtype = 2;
        }

        getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }
}
