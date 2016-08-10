/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.projectzombie.region_rotation.modules;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 * Parent class for modules to store their world and respective WorldGuard
 * region manager.
 *
 * @author Jesse Bannon (jmbannon@uw.edu)
 */
public abstract class RegionWorld
{
    private final String worldName;
    private final boolean isValid;
    
    public RegionWorld(final String worldName)
    {
        this.worldName = worldName;
        this.isValid = this.getWorld() != null && getRegionManager() != null;
    }

    /** @return Whether the object is valid for use or not. */
    public boolean       isValid()          { return this.isValid; }


    public String        getWorldName()     { return this.worldName; }
    public World         getWorld()         { return Bukkit.getWorld(worldName); }
    public LocalWorld    getLocalWorld()    { return com.sk89q.worldedit.bukkit.BukkitUtil.getLocalWorld(this.getWorld()); }
    public RegionManager getRegionManager() { return WGBukkit.getRegionManager(getWorld()); }


}
