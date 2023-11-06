package org.gcmakers.mc.commands;

import org.gcmakers.mc.Meteor;
import org.bukkit.scheduler.BukkitRunnable;
import org.gcmakers.mc.MeteorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

public class ShowerCommand implements CommandExecutor {

    private int _taskID = -1;
    private MeteorPlugin _plugin;

    private Instant startTime = null;
    private double duration = 1000 * 60;
    private Logger logger;

    public ShowerCommand(MeteorPlugin plugin) {
        _plugin = plugin;
        logger = plugin.getLogger();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length > 0) {
            duration = Double.parseDouble(args[0]);
        }

        if (_taskID == -1) {
            startDecay();
        } else {
            endDecay();
        }

        return true;
    }

    private void startDecay() {
        startTime = Instant.now();
        System.out.println("Starting end of world timer");
        _taskID = new BukkitRunnable() {

            @Override
            public void run() {
                Instant currentTime = Instant.now();
                double intensity = (double) (Duration.between(startTime, currentTime).toMillis() / duration) * 100;
                if (intensity > 100) {
                    endDecay();
                }

                if (Math.random() > 0.42) {
                    Object[] players = Bukkit.getServer().getOnlinePlayers().toArray();
                    for (Object player : players) {
                        new Meteor(_plugin, (Player) player);
                    }
                }
            }
        }.runTaskTimer(_plugin, 0, 5).getTaskId();
    }

    private void endDecay() {
        if (_taskID != -1) {
            System.out.println("Ending end of world timer");
            Bukkit.getScheduler().cancelTask(_taskID);
            _taskID = -1;
        }
    }

    public void stop() {
        endDecay();
    }
}
