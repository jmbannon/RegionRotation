package net.projectzombie.region_rotation.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
                        final UUID worldUID)
        {
            super(regionName, worldUID);
        }
    }

    public String getBackupBaseStateID()
    { return toSaveID(backupBaseState.getRegionName(), backupBaseState.getWorldUID()); }

    /** Used to store valid AltStates that the BaseState can switch to. */
    private HashMap<String, AltState> altStates;

    /** If anything is corrupted in the BaseState, it can reset to backupBaseState. */
    private AltState backupBaseState;

    /** Keeps track of the current AltState. */
    private String currentState;

    /**
     * @param regionName WorldGuard region name of BaseState region
     * @param worldUID World UUID that region is located in.
     * @param backupBaseState Backup State for BaseState.
     */
    private BaseState(final String regionName,
                     final UUID worldUID,
                     final AltState backupBaseState)
    {
        super(regionName, worldUID);
        this.altStates = new HashMap<>();
        this.backupBaseState = backupBaseState;
        this.currentState = regionName;
    }

    /**
     * Initializes BaseState with a backup state. Highly advised to check if BaseState is valid
     * before use.
     *
     * @param regionName WorldGuard region name of BaseState region
     * @param worldUID World UUID that region is located in.
     * @param backupRegionName WorldGuard region name of BaseState's backup-region.
     * @param backupRegionWorldUID World UUID that backup-region is located in.
     */
    public BaseState(final String regionName,
                     final UUID worldUID,
                     final String backupRegionName,
                     final UUID backupRegionWorldUID)
    {
        this(regionName, worldUID, new AltState(backupRegionName, backupRegionWorldUID));
    }

    /**
     * Adds an AltState that can be rotated with BaseState.
     * @param altRegionName WorldGuard region name of the AltState.
     * @param altRegionWorldUID World UUID of the AltState region.
     * @return True if the AltState was valid and added successfully. False otherwise.
     */
    public boolean addAltState(final String altRegionName,
                               final UUID altRegionWorldUID)
    {
        final AltState altState = new AltState(altRegionName, altRegionWorldUID);
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
     * Checks to see if BaseState contains a valid AltState.
     * @param altRegionName WorldGuard region name of alt state.
     * @return True if BaseState
     */
    public boolean containsAltState(final String altRegionName)
    {
        return altStates.containsKey(altRegionName) && altStates.get(altRegionName).isValid();
    }

    public List<String> getAltStateIDs()
    {
        List<String> holder = new ArrayList<>();
        for (AltState state : altStates.values())
            holder.add(toSaveID(state.getRegionName(), state.getWorldUID()));

        return holder;
    }

    /**
     * Resets the BaseState to the backup-region.
     * @return True if the BaseState was reset successfully. False otherwise.
     */
    public boolean resetState()
    {
        final boolean resetAir = true;
        if (_rotateState(backupBaseState, resetAir))
        {
            this.currentState = this.getRegionName();
            return true;
        }
        return false;
    }

    /**
     * Rotates the BaseState with the specified AltState.
     * @param altStateName Name of the AltState to rotate into the BaseState.
     * @return True if the BaseState was rotated successfully. False otherwise.
     */
    public boolean rotateState(final String altStateName,
                               final boolean rotateAir)
    {
        final AltState altState = altStates.get(altStateName);
        if (_rotateState(altState, rotateAir))
        {
            this.currentState = altState.getRegionName();
            return true;
        }
        return false;
    }

    /**
     * Returns the current region within the confines of the BaseState region.
     * @return Current RegionState.
     */
    public RegionState getCurrentState()
    {
        if (this.currentState.equals(this.getRegionName()))
        {
            return this;
        }
        else if (altStates.containsKey(this.currentState))
        {
            return altStates.get(this.currentState);
        }
        else
        {
            return null;
        }
    }

    /**
     * Rotates a BaseState to the specified swapState, and CPs air if rotateAir is true.
     * @param swapState The state becoming the new current.
     * @param rotateAir If air is to be CPed over.
     * @return If the rotation was successful.
     */
    private boolean _rotateState(final AltState swapState,
                                 final boolean rotateAir)
    {
        if (swapState == null)
        {
            return false;
        }
        else if (this.currentState.equals(swapState.getRegionName()))
        {
            return true;
        }
        return this.copyFrom(swapState, rotateAir);
    }

    /**
     * Used mostly for debug, it prints a nice and easy read of what the BaseState holds.
     * @return Readable BaseState info.
     */
    public String toString()
    {
        final String rAndWSeparator = "@";

        StringBuilder builder = new StringBuilder();

        // Main Region adding
        builder.append("Main R: ");
        builder.append(getRegionName());
        builder.append(rAndWSeparator);
        builder.append(getWorld().getName());

        // Current region adding
        builder.append(", Current: ");
        builder.append(getCurrentState().getRegionName());
        builder.append(rAndWSeparator);
        builder.append(getCurrentState().getWorld().getName());

        // Backup state adding.
        builder.append(", Backup: ");
        builder.append(backupBaseState.getRegionName());
        builder.append(rAndWSeparator);
        builder.append(backupBaseState.getWorld().getName());

        // Alts states being added.
        if (altStates.isEmpty())
            builder.append(", No alts.");
        else
        {
            builder.append(", Alts: ");
            for (AltState alt : altStates.values())
            {
                builder.append("-"); // Starter
                builder.append(alt.getRegionName());
                builder.append(rAndWSeparator);
                builder.append(alt.getWorld().getName());
                builder.append(" "); // Spacing the alts
            }
        }

        return builder.toString();
    }

    /** Used for storing a BaseState or AltState, serves as an ID for it. */
    public static String toSaveID(String regionNameOut, UUID worldUIDOut)
    { return regionNameOut + "," + worldUIDOut; }

    /** Used to read stuff from disc, should be the toSaveID() result. */
    public static String toRegion(String iD)
    { return iD.split(",").length > 1 ? iD.split(",")[0] : null; }

    /** Used to read stuff from disc, should be the toSaveID() result. */
    public static UUID toWorldUID(String iD)
    { return iD.split(",").length > 1 ? UUID.fromString(iD.split(",")[1]) : null; }
}
