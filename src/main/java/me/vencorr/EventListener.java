package me.vencorr;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtect.*;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.*;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Fire;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Objects;

public class EventListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Tameable) {
            Player player = null;
            Projectile projectile = null;
            if (event.getDamager() instanceof Player) {
                player = (Player) event.getDamager();
            }
            if (event.getDamager() instanceof Projectile) {
                projectile = (Projectile) event.getDamager();
            }

            Tameable pet = (Tameable) event.getEntity();
            if (pet.isTamed() && (player != null || projectile != null)) {
                if (projectile != null) {
                    ProjectileSource projsrc = projectile.getShooter();
                    if (projsrc != pet.getOwner() && !projectile.hasPermission("petprotect.petkill")) {
                        event.setCancelled(true);
                        event.setDamage(0);
                    }
                }
                if (player != null && !player.hasPermission("petprotect.petkill")) {
                    if (pet.getOwner() != player) {
                        if (!Objects.equals(pet.getOwner(), player)){
                            event.setCancelled(true);
                            event.setDamage(0);
                        }
                    }
                }
            }
        }
    }

    // Handle rides.
    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        Entity ride = event.getVehicle();
        if (ride instanceof Tameable && event.getEntered() instanceof Player) {
            Player player = (Player)event.getEntered();
            Tameable pet = (Tameable) ride;
            if (pet.isTamed()) {
                if (!Objects.equals(pet.getOwner(), player) && pet.getOwner() != null) {
                    event.setCancelled(true);
                    pet.removePassenger(event.getEntered());
                    player.leaveVehicle();
                }
            }
        }
    }

    /* @EventHandler
    public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
        CoreProtectAPI CoreProtect = getCoreProtect();
        if (CoreProtect != null){
            Block bloc = event.getDamager();
            if (event.getEntity() instanceof Tameable) {
                Tameable pet = (Tameable) event.getEntity();
                List<String[]> blocheck = CoreProtect.blockLookup(bloc, 5);
                Player play = null;
                if (blocheck != null && pet.isTamed()) {
                    for (String[] result : blocheck) {
                        CoreProtectAPI.ParseResult parseResult = CoreProtect.parseResult(result);
                        play = Bukkit.getPlayerExact(parseResult.getPlayer());
                    }
                    if (bloc != null && !Objects.equals(pet.getOwner(), play)) {
                        bloc.setType(Material.AIR);
                        event.setCancelled(true);
                        event.setDamage(0);
                    }
                }
            }

        }
    }

    private CoreProtectAPI getCoreProtect() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CoreProtect");
        if (plugin == null || !(plugin instanceof CoreProtect)) {
            return null;
        }
        CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getAPI();
        if (CoreProtect.isEnabled() == false) {
            return null;
        }
        if (CoreProtect.APIVersion() < 6) {
            return null;
        }
        return CoreProtect;
    } */
}
