package me.ducsad.cornflower;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;

public class FertilizeListener implements Listener {

    private final CornflowerPlugin plugin;

    public FertilizeListener(CornflowerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFertilize(BlockFertilizeEvent event) {

        Location loc = event.getBlock().getLocation();

        if (!loc.getWorld().getName().equals(plugin.getConfig().getString("world")))
            return;

        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        if (x < plugin.getConfig().getInt("min-x"))
            return;

        if (x > plugin.getConfig().getInt("max-x"))
            return;

        if (z < plugin.getConfig().getInt("min-z"))
            return;

        if (z > plugin.getConfig().getInt("max-z"))
            return;

        if (y != plugin.getConfig().getInt("y"))
            return;

        Material flower = Material.valueOf(plugin.getConfig().getString("flower"));

        for (BlockState state : event.getBlocks()) {

            Material type = state.getType();

            if (type == Material.DANDELION
                    || type == Material.POPPY
                    || type == Material.BLUE_ORCHID
                    || type == Material.ALLIUM
                    || type == Material.AZURE_BLUET
                    || type == Material.RED_TULIP
                    || type == Material.ORANGE_TULIP
                    || type == Material.WHITE_TULIP
                    || type == Material.PINK_TULIP
                    || type == Material.OXEYE_DAISY
                    || type == Material.CORNFLOWER) {

                state.setType(flower);
                state.update(true, false);
            }
        }
    }
}
