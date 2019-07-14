package me.vencorr;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public final class PetProtect extends JavaPlugin {

    FileConfiguration config = this.getConfig();
    String hurtmessage;
    String ridemessage;
    String accessmessage;

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

        hurtmessage = config.getString("hurt");
        ridemessage = config.getString("ride");
        accessmessage = config.getString("access");
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }

}
