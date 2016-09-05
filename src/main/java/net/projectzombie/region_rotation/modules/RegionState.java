/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.projectzombie.region_rotation.modules;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.UUID;

/**
 *
 * @author jbannon
 */
public abstract class RegionState extends RegionWorld
{
    private static String FILE_ID_SEP = ",";

    /** @return Constructs State ID in form of regionName + FILE_ID_SEP + worldUUID */
    static protected String toFileID(final String regionName,
                                     final UUID worldUUID)
    {
        return regionName + FILE_ID_SEP + worldUUID;
    }

    /** @return WG region name. */
    static protected String toRegion(String iD)
    {
        final String idSplit[] = iD.split(FILE_ID_SEP);
        return idSplit.length > 1 ? idSplit[0] : null;
    }

    /** @return World UUID of state. */
    static protected UUID toWorldUID(String iD)
    {
        final String idSplit[] = iD.split(FILE_ID_SEP);
        return idSplit.length > 1 ? UUID.fromString(idSplit[1]) : null;
    }

    private final String regionName;
    private final RegionType regionType;
    private final boolean isValid;
    private String rotateBroadcastMessage = null;

    protected RegionState(final String regionName,
                          final UUID worldUID)
    {
        super(worldUID);
        this.regionName = regionName;
        this.regionType = this.getProtectedRegion().getType();
        this.isValid = super.isValid() && this.regionName != null && this.getProtectedRegion() != null;
    }

    /** {@inheritDoc} */
    @Override public boolean isValid() { return this.getProtectedRegion() != null && this.isValid; }
    protected String getRegionName()      { return this.regionName;      }
    protected RegionType getRegionType()  { return this.getRegionType(); }
    protected String getRotateBroadcastMessage() { return this.rotateBroadcastMessage; }

    protected void setRotateBroadcastMessage(final String str)
    {
        rotateBroadcastMessage = str;
    }

    /**
     * Broadcasts its message to the server. TODO: Console log if fails
     */
    protected void broadcastMessage()
    {
        if (this.rotateBroadcastMessage != null) {
            Bukkit.broadcastMessage(this.rotateBroadcastMessage);
        }
    }

    protected String getInfoID() {
        return this.regionName + "@" + this.getWorld().getName();
    }

    protected ProtectedRegion getProtectedRegion()
    {
        return super.getRegionManager().getRegion(regionName);
    }

    protected boolean isCuboidRegion()
    {
        return regionType.equals(RegionType.CUBOID);
    }
    protected boolean isPolygonRegion()
    {
        return regionType.equals(RegionType.POLYGON);
    }

    protected CuboidRegion getCuboidRegion()
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

    protected Polygonal2DRegion getPolygonRegion()
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

    /** Used to get a sorted iterator of blocks from the local state. */
    protected Iterator<Block> getSortedBlockIterator()
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
        arr = new TreeSet<>(new BlockComparator());
        regionWorld = super.getLocalWorld();

        for (BlockVector block : region) {
            Block block1 = (new Location(BukkitUtil.toWorld(regionWorld),
                                                            block.getX(),
                                                            block.getY(),
                                                            block.getZ())).getBlock();
            arr.add(block1);

        }

        return arr.iterator();
    }

    /** Fetches the chests in the local state. */
    protected ArrayList<Chest> getRegionChests()
    {
        final ArrayList<Chest> toRet = new ArrayList<>();
        final Iterator<Block> blockIter = this.getSortedBlockIterator();
        Block tmp;
        while (blockIter.hasNext())
        {
            tmp = blockIter.next();
            if (tmp.getState() instanceof Chest)
            {
                toRet.add((Chest)tmp.getState());
            }
        }
        return toRet;
    }

    /** Used to check if the rhs can rotate to or from the local state. */
    protected boolean canRotate(final RegionState rhs)
    {
        if (rhs == null || !this.isValid || !rhs.isValid)
        {
            return false;
        }

        if (this.isCuboidRegion() && rhs.isCuboidRegion())
        {
            CuboidRegion lhsR = this.getCuboidRegion();
            CuboidRegion rhsR = rhs.getCuboidRegion();
            return lhsR.getLength() == rhsR.getLength()
                    && lhsR.getWidth() == rhsR.getWidth()
                    && lhsR.getHeight() == rhsR.getHeight();
        }
        else if (this.isPolygonRegion() && rhs.isPolygonRegion())
        {
            Polygonal2DRegion lhsR = this.getPolygonRegion();
            Polygonal2DRegion rhsR = rhs.getPolygonRegion();
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

    /** Used to copy blocks from the rhs state and paste them in the local state. */
    protected boolean copyFrom(final RegionState rhs,
                            final boolean pasteAir)
    {
        return _copyPaste(rhs, this, pasteAir);
    }

    /** Used to copy blocks from the local state and paste them in the rhs state. */
    protected boolean pasteTo(final RegionState rhs,
                           final boolean pasteAir)
    {
        return _copyPaste(this, rhs, pasteAir);
    }

    protected String getFileID()
    {
        return toFileID(this.regionName, this.getWorldUID());
    }

    /**
     * Used to copy and paste a selected region to another selected region that is the same size.
     * @param copyState The place the blocks are coming from.
     * @param pasteState The place the blocks are being put in.
     * @param pasteAir If air is to be CPed into the build.
     * @return If the CPing was successful.
     */
    static private boolean _copyPaste(final RegionState copyState,
                                      final RegionState pasteState,
                                      final boolean pasteAir)
    {
        if (copyState == null || !copyState.canRotate(pasteState)) {
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
            pasteBlk = pasteIter.next();
            pasteMat = pasteBlk.getType();

            if (!pasteAir && pasteMat.equals(Material.AIR))
            {
                continue;
            }
            // Data will have to change when MC adds material types for wool.
            if (copyMat.equals(pasteMat) && copyBlk.getData() == pasteBlk.getData())
            {
                pasteBlk.setType(copyMat);
                pasteBlk.setData(copyBlk.getData());
                if (copyBlk.getState() instanceof Chest)
                {
                    Chest copyChest = (Chest) copyBlk.getState();
                    Chest pasteChest = (Chest) pasteBlk.getState();
                    pasteChest.getInventory().setContents(copyChest.getInventory().getContents());
                }
                else if (copyBlk.getState() instanceof Sign)
                {
                    Sign copySign = (Sign) copyBlk.getState();
                    Sign pasteSign = (Sign) pasteBlk.getState();

                    for (int i = 0; i < copySign.getLines().length; i++)
                    {
                        pasteSign.setLine(i, copySign.getLine(i));
                    }
                    pasteSign.update();
                }
            }
        }
        return true;
    }

    /**
     * Used to create a repeatable ordering for Blocks.
     */
    private class BlockComparator implements Comparator<Block>
    {
        @Override
        public int compare(Block b1, Block b2)
        {
            if (b1.getX() != b2.getX())
                return b1.getX() > b2.getX() ? 1 : -1;
            else if (b1.getY() != b2.getY())
                return b1.getY() > b2.getY() ? 1 : -1;
            else if (b1.getZ() != b2.getZ())
                return b1.getZ() > b2.getZ() ? 1 : -1;
            else
                return 0;
        }
    }
}
