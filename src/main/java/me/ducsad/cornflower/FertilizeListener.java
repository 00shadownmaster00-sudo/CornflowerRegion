package me.ducsad.cornflower;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FertilizeListener implements Listener {

    private final CornflowerPlugin plugin;
    private final Random random = new Random();

    public FertilizeListener(CornflowerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFertilize(BlockFertilizeEvent event) {

        Location loc = event.getBlock().getLocation();

        ConfigurationSection regionsSection = plugin.getConfig().getConfigurationSection("regions");
        if (regionsSection == null) {
            return;
        }

        for (String regionName : regionsSection.getKeys(false)) {

            ConfigurationSection region = regionsSection.getConfigurationSection(regionName);
            if (region == null)
                continue;

            if (!matchesRegion(loc, region))
                continue;

            List<Material> flowers = getFlowers(region, regionName);
            if (flowers.isEmpty())
                continue;

            applyFlowers(event, flowers);

            // Đã khớp 1 region rồi thì dừng, không cần dò các region khác
            return;
        }
    }

    private boolean matchesRegion(Location loc, ConfigurationSection region) {

        String world = region.getString("world");
        if (world == null || !loc.getWorld().getName().equals(world))
            return false;

        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        if (x < region.getInt("min-x")) return false;
        if (x > region.getInt("max-x")) return false;
        if (z < region.getInt("min-z")) return false;
        if (z > region.getInt("max-z")) return false;
        if (y != region.getInt("y")) return false;

        return true;
    }

    private List<Material> getFlowers(ConfigurationSection region, String regionName) {

        List<Material> flowers = new ArrayList<>();
        List<String> names = new ArrayList<>();

        if (region.isList("flowers")) {
            names = region.getStringList("flowers");
        } else if (region.isString("flower")) {
            names.add(region.getString("flower"));
        }

        for (String name : names) {
            try {
                flowers.add(Material.valueOf(name.trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("[CornflowerRegion] Region '" + regionName + "': tên hoa không hợp lệ '" + name + "'");
            }
        }

        if (flowers.isEmpty()) {
            plugin.getLogger().warning("[CornflowerRegion] Region '" + regionName
                    + "' không có hoa hợp lệ (kiểm tra 'flower' hoặc 'flowers' trong config).");
        }

        return flowers;
    }

    private void applyFlowers(BlockFertilizeEvent event, List<Material> flowers) {

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

                Material chosen = flowers.get(random.nextInt(flowers.size()));

                state.setType(chosen);
                state.update(true, false);
            }
        }
    }
}
