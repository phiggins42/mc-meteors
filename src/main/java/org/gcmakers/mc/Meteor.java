package org.gcmakers.mc;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Meteor {

    private MeteorPlugin _plugin;
    private Player _player;
    public Meteor(MeteorPlugin plugin, Player player) {
        _plugin = plugin;
        _player = player;

        // make it hit somewhere way below the user position:
        Location target = player.getLocation().add(randomAround(72), 0, randomAround(72));
        target.setY(0);
        // somehow pluck these behaviors into options?  where the meteor starts.
        // Location source = player.getLocation().clone().add(randomAround(42), 0, randomAround(42)).add(0, 120, 0);
        // Location source = new Location(player.getWorld(), target.getX(), player.getWorld().getMaxHeight(), target.getZ());
        Location source = new Location(player.getWorld(), target.getX() + randomAround(196), player.getWorld().getMaxHeight(), target.getZ() + randomAround(196));

        // animate a block from source -> target, then explode at the end
        animatePath(source, target, Material.LAVA);
    }

    private void animatePath(Location source, Location target, Material block) {

        // higher interval means faster, I'd like to switch this to a Line/easing/time-based animation
        // sequence to be honest, but this works for now:
        double interval = 2;
        double distance = source.distance(target);
        Vector difference = target.toVector().subtract(source.toVector());
        double points = Math.ceil(distance / interval);
        difference.multiply(1D / points);

        // clone because this `location` is the path along which the thing travels, and mutates for each step
        Location location = source.clone();

        int taskID = new BukkitRunnable() {

            int i = 0;
            @Override
            public void run() {
                if (++i > points) {
                    // stop the animation
                } else {
                    World w = _player.getWorld();

                    // reset the current pointer of location back to air, and step it:
                    location.getBlock().setType(Material.AIR);
                    location.add(difference);

                    Block currentBlock = location.getBlock();
                    if (currentBlock.getType() == Material.AIR) {
                        // passing through an AIR block, is normal.
                        currentBlock.setType(Material.MAGMA_BLOCK);
                        // currentBlock.setType(block);
                        double x = location.getX();
                        double y = location.getY();
                        double z = location.getZ();

                        // just some fancy fancy:
                        w.spawnParticle(Particle.FALLING_DRIPSTONE_LAVA, x, y, z, 1, 0.5, 0.5, 0.5);
                        // w.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, x, y + 5, z, 1, 0.5, 0.5, 0.5);
                        w.spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, x, y, z, 0, 0.5, 1, 0.5, 0);
                        w.spawnParticle(Particle.FALLING_OBSIDIAN_TEAR, x, y, z, 1, 1, 0.5, 1);

                    } else {
                        // when it hits a non-air block, explode and stop
                        i = (int) points + 1;
                        if (location.getBlock().getType() != Material.WATER) {
                            if (location.getBlock().getType().isBurnable()) {
                                location.getBlock().setType(Material.FIRE);
                            } else {
                                w.createExplosion(location, 10);
                                // TODO: parameterize if it leaves a puddle of lava at the end
                                // location.add(0, -4, 0).getBlock().setType(Material.LAVA);
                            }
                        } else {
                            // this makes obsidian in the water:
                            location.getBlock().setType(Material.LAVA);
                        }

                    }

                }
            }
        }.runTaskTimer(_plugin, 0, 0).getTaskId();
    }

    private double randomAround(double n) {
        if (Math.random() > 0.5) {
            return -1 * (n * Math.random());
        } else {
            return (n * Math.random());
        }
    }
}
