package me.vencorr;

import org.bukkit.Bukkit;
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

import java.util.Objects;

public class EventListener implements Listener {

    PetProtect pp;

    public String GetPet(Entity entity) {
        if (entity instanceof Cat) {
            return pp.config.getString("cat");
        } else if (entity instanceof Wolf) {
            return pp.config.getString("wolf");
        } else if (entity instanceof Horse) {
            return pp.config.getString("horse");
        } else if (entity instanceof Parrot) {
            return pp.config.getString("parrot");
        } else if (entity instanceof Donkey) {
            return pp.config.getString("donkey");
        } else if (entity instanceof Mule) {
            return pp.config.getString("mule");
        } else if (entity instanceof Llama) {
            return pp.config.getString("llama");
        } else {
            return "pet";
        }
    }

    public String GetOwner(Tameable pet) {
        if (pet.getOwner().getName() == null) {
            return "{player}";
        } else {
            return pet.getOwner().getName();
        }
    }

    // Define Plugin
    public EventListener(PetProtect plugin) {
        this.pp = plugin;
    }

    // Handle entity hurt
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Tameable) {
            String message = pp.hurtmessage;
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
                        event.setCancelled(true);
                        event.setDamage(0);
                        if (projsrc instanceof Player) {
                            ((Player) projsrc).sendMessage(message.replace("{player}",GetOwner(pet)).replace("{pet}",GetPet(pet)));
                        }
                    }
                }
                if (player != null && !player.hasPermission("petprotect.hurt")) {
                    if (pet.getOwner() != player) {
                        if (!Objects.equals(pet.getOwner(), player)){
                            event.setCancelled(true);
                            event.setDamage(0);
                            player.sendMessage(message.replace("{player}",GetOwner(pet)).replace("{pet}",GetPet(pet)));
                        }
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
            String message = pp.ridemessage;
            Player player = (Player)event.getEntered();
            Tameable pet = (Tameable) ride;
            if (pet.isTamed()) {
                if (pet.getOwner().getName() == null) {
                    message = message.replace("{player}", "Unknown Player");
                } else {
                    message = message.replace("{player}", pet.getOwner().getName());
                }
                if (!Objects.equals(pet.getOwner(), player) && pet.getOwner() != null && !player.hasPermission("petprotect.ride")) {
                    event.setCancelled(true);
                    player.sendMessage(message.replace("{player}",GetOwner(pet)).replace("{pet}",GetPet(pet)));
                }
            }
        }
    }

    // Handle horse inventory access
    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent event) {
        if (event.getInventory() instanceof HorseInventory || event.getInventory() instanceof AbstractHorseInventory || event.getInventory() instanceof ChestedHorse) {
            String message = pp.accessmessage;
            Inventory inv = event.getInventory();
            if (inv.getHolder() instanceof Tameable) {
                Tameable tamed = (Tameable) inv.getHolder();
                if (tamed.getOwner().getName() == null) {
                    message = message.replace("{player}", "Unknown Player");
                } else {
                    message = message.replace("{player}", tamed.getOwner().getName());
                }
                if (tamed.isTamed() && event.getPlayer() != tamed.getOwner() && !event.getPlayer().hasPermission("petprotect.access")) {
                    event.getPlayer().sendMessage(message.replace("{player}",GetOwner(tamed)).replace("{pet}",GetPet(tamed)));
                    event.setCancelled(true);
                }
            }
        }
    }
}
