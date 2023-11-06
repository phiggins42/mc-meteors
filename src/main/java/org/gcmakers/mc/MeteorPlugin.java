package org.gcmakers.mc;

import org.bukkit.plugin.java.JavaPlugin;
import org.gcmakers.mc.commands.ShowerCommand;

public final class MeteorPlugin extends JavaPlugin {

    private ShowerCommand executor;

    @Override
    public void onEnable() {
        executor = new ShowerCommand(this);
        getServer().getPluginCommand("shower").setExecutor(executor);
    }

    @Override
    public void onDisable() {
        executor.stop();
    }

}
