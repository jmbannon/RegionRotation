/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.projectzombie.region_rotation.modules;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockWorldVector;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionType;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.TreeSet;
import java.util.Iterator;

/**
 *
 * @author jbannon
 */
public abstract class RegionState extends RegionWorld
{
    private final String regionName;
    private final RegionType regionType;
    private final boolean isValid;

    public RegionState(final String regionName,
                       final String worldName)
    {
        super(worldName);
        this.isValid = super.isValid() && this.getProtectedRegion() != null;
        this.regionName = regionName;
        this.regionType = this.getProtectedRegion().getType();
    }

    /** {@inheritDoc} */
    @Override public boolean isValid() { return this.isValid; }

    public String getRegionName()     { return this.regionName;      }
    public RegionType getRegionType() { return this.getRegionType(); }
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
        if (this.isCuboidRegion())
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
        if (this.isPolygonRegion()) {
            final ProtectedRegion region = this.getProtectedRegion();
            final int maxY = region.getMaximumPoint().getBlockY();
            final int minY = region.getMinimumPoint().getBlockY();
            return new Polygonal2DRegion(super.getLocalWorld(), region.getPoints(), minY, maxY);
        } else {
            return null;
        }
    }

    public Iterator<Block> getSortedBlockIterator()
    {
        final LocalWorld regionWorld;
        final Region region;
        final TreeSet<Block> arr;
        if (this.isCuboidRegion())
        {
            region = getCuboidRegion();
        }
        else if (this.isPolygonRegion())
        {
            region = getPolygonRegion();
        }
        else
        {
            return null;
        }
        arr = new TreeSet<>();
        regionWorld = super.getLocalWorld();

        for (BlockVector block : region) {
            arr.add(BukkitUtil.toBlock(new BlockWorldVector(regionWorld, block)));
        }

        return arr.iterator();
    }

    public boolean canRotate(final RegionState rhs)
    {
        if (rhs == null || !this.isValid || !rhs.isValid)
        {
            return false;
        }

        if (this.isCuboidRegion() && rhs.isCuboidRegion())
        {
            CuboidRegion lhsR = this.getCuboidRegion();
            CuboidRegion rhsR = this.getCuboidRegion();
            return lhsR.getLength() == rhsR.getLength()
                    && lhsR.getWidth() == rhsR.getWidth()
                    && lhsR.getHeight() == rhsR.getHeight();
        }
        else if (this.isPolygonRegion() && rhs.isPolygonRegion())
        {
            Polygonal2DRegion lhsR = this.getPolygonRegion();
            Polygonal2DRegion rhsR = this.getPolygonRegion();
            return lhsR.getLength() == rhsR.getLength()
                    && lhsR.getWidth() == rhsR.getWidth()
                    && lhsR.getHeight() == rhsR.getHeight()
                    && lhsR.getArea() == rhsR.getArea();
        }
        else
        {
            return false;
        }
    }

    public boolean copyFrom(final RegionState rhs)
    {
        return _copyPaste(this, rhs);
    }

    public boolean pasteTo(final RegionState rhs)
    {
        return _copyPaste(rhs, this);
    }

    static private boolean _copyPaste(final RegionState copyState,
                                      final RegionState pasteState)
    {
        if (!copyState.canRotate(pasteState)) {
            return false;
        }

        Iterator<Block> copyIter = copyState.getSortedBlockIterator();
        Iterator<Block> pasteIter = pasteState.getSortedBlockIterator();
        Block copyBlk, pasteBlk;
        Material copyMat, pasteMat;

        while (copyIter.hasNext() && pasteIter.hasNext())
        {
            copyBlk = copyIter.next();
            copyMat = copyBlk.getType();
            pasteBlk = copyIter.next();
            pasteMat = pasteBlk.getType();

            if (!copyMat.equals(pasteMat))
            {
                pasteBlk.setType(copyMat);
                pasteBlk.setData(copyBlk.getData());
            }
        }
        return true;
    }



}
