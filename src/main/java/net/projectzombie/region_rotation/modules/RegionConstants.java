package net.projectzombie.region_rotation.modules;

import com.sk89q.worldguard.bukkit.WGBukkit;
import org.bukkit.plugin.Plugin;

/**
 * Created by jb on 8/9/16.
 */
public class RegionConstants
{
    private static Plugin PLUGIN = null;
    private static Plugin WG_PLUGIN = null;

    static public void init(final Plugin plugin)
    {
        PLUGIN = plugin;
        WG_PLUGIN = WGBukkit.getPlugin();
    }

    static public Plugin getPlugion()  { return PLUGIN; }
    static public Plugin getWGPlugin() { return WG_PLUGIN; }

    private RegionConstants() { /* Do nothing. */ }
}
