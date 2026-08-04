package me.ducsad.cornflower;

import org.bukkit.plugin.java.JavaPlugin;

public class CornflowerPlugin extends JavaPlugin {

    private static CornflowerPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new FertilizeListener(this), this);

        getLogger().info("CornflowerRegion Enabled!");
    }

    public static CornflowerPlugin get() {
        return instance;
    }
}
