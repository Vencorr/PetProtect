package me.vencorr;

import org.bukkit.plugin.java.JavaPlugin;

public final class PetProtect extends JavaPlugin {

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

}
