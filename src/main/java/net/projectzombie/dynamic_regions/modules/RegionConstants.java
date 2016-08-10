package net.projectzombie.dynamic_regions.modules;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Created by jb on 8/9/16.
 */
public class RegionConstants
{
    private static Plugin PLUGIN = null;
    private static Plugin WG_PLUGIN = null;

    public void initConstants(final Plugin plugin)
    {
        PLUGIN = plugin;
        WG_PLUGIN = WGBukkit.getPlugin();
    }

    public Plugin getPlugion()  { return PLUGIN; }
    public Plugin getWGPlugin() { return WG_PLUGIN; }

    private RegionConstants() { /* Do nothing. */ }
}
