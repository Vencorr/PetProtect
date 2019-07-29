package me.vencorr.petprotect;

import me.vencorr.petprotect.util.ActionBar;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.Objects;

public class EventListener implements Listener {

    Main pp;

    void SendMessage(Tameable pet, Player player) {
        String petName = "pet";
        String ownerName = null;
        petName = pet.getName();
        if (pet.getOwner() != null) {
            if (pet.getOwner().getName() != null) {
                ownerName = pet.getOwner().getName();
            }
        }
        String msg = ChatColor.DARK_RED + "Message Error";
        if (ownerName != null) {
            msg = ChatColor.translateAlternateColorCodes('&', "That is &o" + ownerName + "'s&r " + petName);
        } else if (ownerName == null) {
            msg = "That isn't your " + petName;
        }
        ActionBar.sendActionBar(player, msg);
    }

    // Define Plugin
    EventListener(Main plugin) {
        this.pp = plugin;
    }

    // Handle entity hurt
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
                    if (projsrc != pet.getOwner() && !projectile.hasPermission("petprotect.hurt")) {
                        if (projsrc instanceof Player) {
                            event.setCancelled(true);
                            projectile.setVelocity(new Vector(projectile.getVelocity().getX(), 5f, projectile.getVelocity().getZ()));
                            event.setDamage(0);
                            SendMessage(pet,(Player)projsrc);
                        }
                    }
                }
                if (player != null && !player.hasPermission("petprotect.hurt")) {
                    if (pet.getOwner() != player) {
                        event.setCancelled(true);
                        event.setDamage(0);
                        SendMessage(pet,player);
                    }
                }
            }
        }
    }

    // Handle entity ride
    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        Entity ride = event.getVehicle();
        if (ride instanceof Tameable && event.getEntered() instanceof Player) {
            Player player = (Player)event.getEntered();
            Tameable pet = (Tameable) ride;
            if (pet.isTamed()) {
                if (!Objects.equals(pet.getOwner(), player) && pet.getOwner() != null && !player.hasPermission("petprotect.ride")) {
                    event.setCancelled(true);
                    SendMessage(pet,player);
                }
            }
            if (pet instanceof SkeletonHorse && pp.skeletonLog) {
                try {
                    pp.SkeletonLog(player, pet, pet.getLocation(), "ride");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Handle horse inventory access
    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent event) {
        if (event.getInventory() instanceof HorseInventory || event.getInventory() instanceof AbstractHorseInventory || event.getInventory() instanceof ChestedHorse) {
            Inventory inv = event.getInventory();
            if (inv.getHolder() instanceof Tameable) {
                Tameable tamed = (Tameable) inv.getHolder();
                if (tamed instanceof SkeletonHorse && pp.skeletonLog) {
                    try {
                        pp.SkeletonLog((Player)event.getPlayer(), tamed, tamed.getLocation(), "access");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (tamed.isTamed() && event.getPlayer() != tamed.getOwner() && !event.getPlayer().hasPermission("petprotect.access")) {
                    SendMessage(tamed,(Player)event.getPlayer());
                    event.setCancelled(true);
                }
            }
        }
    }
}