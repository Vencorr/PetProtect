package me.vencorr.petprotect;

import me.vencorr.petprotect.util.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.Objects;


public class EventListener implements Listener {

    private Main pp;

    // Send Message
    private void SendMessage(Tameable pet, Player player, boolean isOwner) {
        if (pp.msgtype != 0) {
            String petName;
            String ownerName = null;
            if (pp.customname) {
                petName = pet.getName();
            } else {
                petName = pet.getType().name();
            }
            if (pet.getOwner() != null) {
                if (pet.getOwner().getName() != null) {
                    ownerName = pet.getOwner().getName();
                }
            }
            String msg;
            if (ownerName != null && !isOwner) {
                msg = ChatColor.translateAlternateColorCodes('&', pp.message.replace("{pet}", petName).replace("{owner}", ownerName));
            } else {
                msg = ChatColor.translateAlternateColorCodes('&', pp.altmessage.replace("{pet}", petName));
            }
            if (pp.msgtype == 2) {
                ActionBar.sendActionBar(player, msg);
            } else {
                player.sendMessage(msg);
            }
        }
    }

    // Define Plugin
    EventListener(Main plugin) {
        this.pp = plugin;
    }

    // Damaging
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Tameable && pp.hurt) {
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
                    if ((projsrc != pet.getOwner() || pp.ownerprotect) && !projectile.hasPermission("petprotect.hurt")) {
                        if ((projsrc instanceof Player && !((Player)projsrc).isBanned()) || pet instanceof SkeletonHorse) {
                            event.setCancelled(true);
                            projectile.setVelocity(new Vector(projectile.getVelocity().getX(), 5f, projectile.getVelocity().getZ()));
                            event.setDamage(0);
                            SendMessage(pet,(Player)projsrc, pet.getOwner() == projsrc);
                        }
                    }
                }
                if (player != null && !player.hasPermission("petprotect.hurt")) {
                    OfflinePlayer play = (OfflinePlayer)pet.getOwner();
                    if (
                            pet instanceof SkeletonHorse || ((player != pet.getOwner() || pp.ownerprotect) && !(pet instanceof Wolf && ((Wolf) pet).getTarget() == player) && !play.isBanned())
                            ) {
                        event.setCancelled(true);
                        event.setDamage(0);
                        SendMessage(pet, player, pet.getOwner() == player);
                    }
                }
            }
        }
    }

    // Riding
    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        Entity ride = event.getVehicle();
        if (ride instanceof Tameable && event.getEntered() instanceof Player && pp.ride) {
            Player player = (Player)event.getEntered();
            Tameable pet = (Tameable) ride;
            if (pet.isTamed()) {
                if (!Objects.equals(pet.getOwner(), player) && pet.getOwner() != null && !player.hasPermission("petprotect.ride") && !player.isBanned()) {
                    event.setCancelled(true);
                    SendMessage(pet,player, false);
                }
            }
            if (pet instanceof SkeletonHorse) {
                Bukkit.getConsoleSender().sendMessage("Skeleton Horse (" + pet.getUniqueId().toString() + ") at " + pet.getLocation().getX() + ", " + pet.getLocation().getY() + ", " + pet.getLocation().getZ() + " was mounted by " + player.getName());
            }
        }
    }

    // Inventory Access
    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent event) {
        if ((event.getInventory() instanceof HorseInventory || event.getInventory() instanceof AbstractHorseInventory || event.getInventory() instanceof ChestedHorse) && pp.access) {
            Inventory inv = event.getInventory();
            if (inv.getHolder() instanceof Tameable) {
                Tameable pet = (Tameable) inv.getHolder();
                Player player = (Player) event.getPlayer();
                if (pet instanceof SkeletonHorse) {
                    Bukkit.getConsoleSender().sendMessage("Skeleton Horse (" + pet.getUniqueId().toString() + ") at " + pet.getLocation().getX() + ", " + pet.getLocation().getY() + ", " + pet.getLocation().getZ() + " was accessed by " + player.getName());
                } else if (pet.isTamed() && event.getPlayer() != pet.getOwner() && !event.getPlayer().hasPermission("petprotect.access") && !player.isBanned()) {
                    SendMessage(pet,(Player)event.getPlayer(), false);
                    event.setCancelled(true);
                }
            }
        }
    }

    // Right-Clicks
    @EventHandler
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof Tameable && pp.access) {
            Tameable pet = (Tameable) event.getRightClicked();
            Player player = event.getPlayer();
            if (pet.isTamed() && pet.getOwner() != player && !player.hasPermission("petprotect.access")) {
                SendMessage(pet,player, false);
                event.setCancelled(true);
            }
        }
    }

    // Leash
    @EventHandler
    public void onPlayerLeashEntityEvent(PlayerLeashEntityEvent event) {
        if (event.getEntity() instanceof Tameable && pp.access) {
            Tameable pet = (Tameable) event.getEntity();
            Player player = event.getPlayer();
            if (pet.isTamed() && pet.getOwner() != player && !player.hasPermission("petprotect.access") && !player.isBanned()) {
                SendMessage(pet,player, false);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByBlockEvent (EntityDamageByBlockEvent event) {
        if (pp.petinvulnerable && event.getEntity() instanceof Tameable) {
            Tameable pet = (Tameable) event.getEntity();
            if (pet.isTamed()) event.setCancelled(true);
        }
    }
}
