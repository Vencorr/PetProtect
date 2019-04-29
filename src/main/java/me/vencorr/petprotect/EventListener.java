package me.vencorr.petprotect;

import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;

import java.util.Objects;

import static org.bukkit.Bukkit.getServer;

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
                    if (projsrc != pet.getOwner() && !projectile.isOp()) {
                        event.setCancelled(true);
                        event.setDamage(0);
                    }
                }
                if (player != null && !player.isOp()) {
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
}
