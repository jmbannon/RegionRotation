package net.projectzombie.region_rotation.modules;

import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;

import java.util.HashMap;

/**
 * Created by jb on 8/9/16.
 */
public class BaseState extends RegionState
{
    private HashMap<String, AltState> altStates;
    private AltState backupBaseState;
    private String currentState;

    public BaseState(final String regionName,
                     final AltState backupBaseState,
                     final String worldName)
    {
        super(regionName, worldName);
        this.backupBaseState = backupBaseState;
        this.currentState = regionName;
    }

    public BaseState(final String regionName,
                     final String worldName,
                     final String backupRegionName,
                     final String backupRegionWorldName)
    {
        this(regionName, new AltState(backupRegionName, backupRegionWorldName), worldName);
    }

    public boolean resetBaseState()
    {
        return _rotateState(backupBaseState);
    }

    public boolean rotateState(final String altStateName)
    {
        return _rotateState(altStates.get(altStateName));
    }

    private boolean _rotateState(final AltState swapState)
    {
        if (swapState == null)
        {
            return false;
        }
        else if (this.currentState.equals(swapState.getRegionName()))
        {
            return true;
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
