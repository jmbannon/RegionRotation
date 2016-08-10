package net.projectzombie.dynamic_regions.modules;

import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;

import java.util.HashMap;

/**
 * Created by jb on 8/9/16.
 */
public class BaseState extends RegionState
{
    private HashMap<String, AltState> altStates;

    public BaseState(final String regionName,
                     final String worldName)
    {
        super(regionName, worldName);
    }

    public boolean rotateState(final String altStateName) {
        final AltState swapState = altStates.get(altStateName);
        if (swapState == null)
        {
            return false;
        }

        if (swapState.isCuboidRegion() && this.isCuboidRegion())
        {
            final CuboidRegion cuboidBaseRegion = this.getCuboidRegion();
            final CuboidRegion cuboidSwapRegion = swapState.getCuboidRegion();
            // TODO: copy/paste regions
        }
        else if (swapState.isPolygonRegion() && this.isPolygonRegion())
        {
            final Polygonal2DRegion polygonBaseRegion = this.getPolygonRegion();
            final Polygonal2DRegion polygonSwapRegion = swapState.getPolygonRegion();
            // TODO: copy/paste regions
        }
        return false;
    }
}
