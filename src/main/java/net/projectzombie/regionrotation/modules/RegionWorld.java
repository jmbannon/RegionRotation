/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.projectzombie.regionrotation.modules;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.UUID;

/**
 * Parent class for modules to store their world and respective WorldGuard
 * region manager.
 *
 * @author Jesse Bannon (jmbannon@uw.edu)
 */
public abstract class RegionWorld
{
    private final UUID worldUID;
    private final boolean isValid;
    
    protected RegionWorld(final UUID worldUID)
    {
        this.worldUID = worldUID;
        this.isValid = this.getWorld() != null && getRegionManager() != null;
    }

    /** @return Whether the object is valid for use or not. */
    protected boolean isValid()          { return this.isValid; }

    protected UUID          getWorldUID()      { return this.worldUID; }
    protected World         getWorld()         { return Bukkit.getWorld(worldUID); }
    protected LocalWorld    getLocalWorld()    { return com.sk89q.worldedit.bukkit.BukkitUtil.getLocalWorld(this.getWorld()); }
    protected RegionManager getRegionManager() { return WGBukkit.getRegionManager(getWorld()); }


}
