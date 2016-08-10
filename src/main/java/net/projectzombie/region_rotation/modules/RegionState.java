/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.projectzombie.region_rotation.modules;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionType;

/**
 *
 * @author jbannon
 */
public abstract class RegionState extends RegionWorld
{
    private final String regionName;
    private final RegionType regionType;

    public RegionState(final String regionName,
                       final String worldName)
    {
        super(worldName);
        this.regionName = regionName;
        this.regionType = this.getProtectedRegion().getType();
    }
    
    public String getRegionName()      { return this.regionName; }
    public ProtectedRegion getProtectedRegion()
    {
        return super.getRegionManager().getRegion(regionName);
    }

    public boolean isCuboidRegion()
    {
        return regionType.equals(RegionType.CUBOID);
    }

    public boolean isPolygonRegion()
    {
        return regionType.equals(RegionType.POLYGON);
    }

    public CuboidRegion getCuboidRegion()
    {
        if (regionType.equals(RegionType.CUBOID))
        {
            final ProtectedRegion region = this.getProtectedRegion();
            final Vector maxPoint = region.getMaximumPoint().floor();
            final Vector minPoint = region.getMinimumPoint().floor();
            return new CuboidRegion(super.getLocalWorld(), minPoint, maxPoint);
        }
        else
        {
            return null;
        }
    }

    public Polygonal2DRegion getPolygonRegion()
    {
        if (regionType.equals(RegionType.POLYGON))
        {
            final ProtectedRegion region = this.getProtectedRegion();
            final int maxY = region.getMaximumPoint().getBlockY();
            final int minY = region.getMinimumPoint().getBlockY();
            return new Polygonal2DRegion(super.getLocalWorld(), region.getPoints(), minY, maxY);
        }
        else
        {
            return null;
        }
    }

}
