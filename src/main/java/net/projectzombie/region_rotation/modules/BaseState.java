package net.projectzombie.region_rotation.modules;

import java.util.HashMap;

/**
 * Created by jmbannon on 8/9/16.
 * BaseState represents a WorldGuard region that can be rotated (i.e. switched with) alternative
 * regions of the exact same size. It requires a backup region of itself (i.e. a duplicated
 * region placed outside the map) in case of an error when rotating regions.
 */
public class BaseState extends RegionState
{
    /** AltState's belong to a single BaseState: a BaseState can rotate to an AltState. */
    static private class AltState extends RegionState
    {
        public AltState(final String regionName,
                        final String worldName)
        {
            super(regionName, worldName);
        }
    }

    /** Used to store valid AltStates that the BaseState can switch to. */
    private HashMap<String, AltState> altStates;

    /** If anything is corrupted in the BaseState, it can reset to backupBaseState. */
    private AltState backupBaseState;

    /** Keeps track of the current AltState. */
    private String currentState;

    /** Whether the class was initialized with valid parameters. */
    private final boolean isValid;

    /**
     * @param regionName WorldGuard region name of BaseState region
     * @param worldName World name that region is located in.
     * @param backupBaseState Backup State for BaseState.
     */
    private BaseState(final String regionName,
                     final String worldName,
                     final AltState backupBaseState)
    {
        super(regionName, worldName);
        this.altStates = new HashMap<>();
        this.backupBaseState = backupBaseState;
        this.currentState = regionName;
        this.isValid = this.isValid()
                && this.backupBaseState.isValid()
                && this.canRotate(this.backupBaseState);
    }

    /**
     * Initializes BaseState with a backup state. Highly advised to check if BaseState is valid
     * before use.
     *
     * @param regionName WorldGuard region name of BaseState region
     * @param worldName World name that region is located in.
     * @param backupRegionName WorldGuard region name of BaseState's backup-region.
     * @param backupRegionWorldName World name that backup-region is located in.
     */
    public BaseState(final String regionName,
                     final String worldName,
                     final String backupRegionName,
                     final String backupRegionWorldName)
    {
        this(regionName, worldName, new AltState(backupRegionName, backupRegionWorldName));
    }

    /** {@inheritDoc} */
    @Override public boolean isValid() { return this.isValid; }

    /**
     * Adds an AltState that can be rotated with BaseState.
     * @param altRegionName WorldGuard region name of the AltState.
     * @param altRegionWorldName World name of the AltState region.
     * @return True if the AltState was valid and added successfully. False otherwise.
     */
    public boolean addAltState(final String altRegionName,
                               final String altRegionWorldName)
    {
        final AltState altState = new AltState(altRegionName, altRegionWorldName);
        if (this.canRotate(altState))
        {
            altStates.put(altRegionName, altState);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes an AltState from the BaseState.
     * @param altRegionName WorldGuard region name of the AltState.
     */
    public void removeAltState(final String altRegionName)
    {
        altStates.remove(altRegionName);
    }

    /**
     * Resets the BaseState to the backup-region.
     * @return True if the BaseState was reset successfully. False otherwise.
     */
    public boolean resetState()
    {
        return _rotateState(backupBaseState);
    }

    /**
     * Rotates the BaseState with the specified AltState.
     * @param altStateName Name of the AltState to rotate into the BaseState.
     * @return True if the BaseState was rotated successfully. False otherwise.
     */
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

        return this.copyFrom(swapState);
    }
}
