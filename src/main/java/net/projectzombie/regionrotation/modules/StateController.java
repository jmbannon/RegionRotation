package net.projectzombie.regionrotation.modules;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Chest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by jb on 8/10/16.
 */
public class StateController
{
    private final String name;
    private final HashMap<String, BaseState> states;
    private final StateBuffer buffer;

    protected StateController(final String name,
                              final StateBuffer buffer)
    {
        this.name = name;
        this.buffer = buffer;
        this.states = new HashMap<>();

        if (buffer.isValid()) {
            Set<BaseState> baseStates = buffer.readBaseStates();
            if (baseStates != null) {
                for (BaseState baseState : baseStates) {
                    states.put(baseState.getRegionName(), baseState);
                }
            }
        }
    }

    /** @return Name of StateController. */
    public String getName() {
        return this.name;
    }

    public String getBaseStateInfo(final String regionName) {
        final BaseState toInfo = states.get(regionName);
        if (toInfo != null) {
            return toInfo.toString();
        } else {
            return null;
        }
    }

    /**  @Return If all BaseStates were saved successful. */
    protected boolean saveBaseStates() {
        return buffer.writeBaseStates(states.values());
    }

    /** @return ArrayList of all BaseState display names. In the form of 'regionName@regionWorld. */
    public ArrayList<String> getBaseStateDisplayNames()
    {
        final ArrayList<String> toRet = new ArrayList<>();
        states.values().forEach(baseState -> toRet.add(baseState.getDisplayName()));
        return toRet;
    }

    /** @return ArrayList of all BaseState region names. */
    public ArrayList<String> getBaseStateRegionNames()
    {
        final ArrayList<String> toRet = new ArrayList<>();
        states.values().forEach(baseState -> toRet.add(baseState.getRegionName()));
        return toRet;
    }

    /**
     * @param baseStateName BaseState WG region name.
     * @return ArrayList of all AltState WG region names.
     */
    public ArrayList<String> getAltStateRegionNames(final String baseStateName)
    {
        final BaseState baseState = states.get(baseStateName);
        if (baseState != null) {
            return baseState.getAltStateRegionNames();
        } else {
            return null;
        }
    }

    /** @return HashMap with the form {@literal <BaseStateRegionName, AltStateRegionNames>}. */
    public HashMap<String, ArrayList<String>> getBaseStateStringMap()
    {
        final HashMap<String, ArrayList<String>> toRet = new HashMap<>();
        this.getBaseStateRegionNames().forEach(name ->
                toRet.put(name, this.getAltStateRegionNames(name)));
        return toRet;
    }

    /** @return ArrayList of Chests in the BaseState region (i.e. whatever is the current state). */
    public ArrayList<Chest> getCurrentStateChests(final String baseStateName)
    {
        final BaseState baseState = states.get(baseStateName);
        if (baseState != null) {
            return baseState.getRegionChests();
        } else {
            return null;
        }
    }

    /**
     * Adds a BaseState to the StateController
     *
     * @param state BaseState to create.
     * @return True if successful. False if failed.
     */
    private boolean _addBaseState(final BaseState state) {
        if (state.isValid() && buffer.writeBaseState(state)) {
            states.put(state.getRegionName(), state);
            return true;
        }
        return false;
    }

    public boolean regionExists(final String regionName,
                                final World world) {
        final RegionManager rm = WGBukkit.getRegionManager(world);
        if (rm != null) {
            return rm.hasRegion(regionName);
        } else {
            return false;
        }
    }

    public boolean baseStateExists(final String regionName) {
        return states.containsKey(regionName);
    }

    public boolean altStateExists(final String baseStateName, final String altStateName) {
        final BaseState baseState = states.get(baseStateName);
        return baseState != null && baseState.containsAltState(altStateName);
    }

    /**
     * Adds a BaseState to the StateController. Must have a backup region.
     * @param regionName BaseState WG region name.
     * @param world World of BaseState region.
     * @param backupRegionName Backup WG region name.
     * @param backupWorld World of backup region.
     * @return True if the BaseState was added successfully. False otherwise.
     */
    public boolean addBaseState(final String regionName,
                                final World world,
                                final String backupRegionName,
                                final World backupWorld) {
        if (world != null && backupWorld != null) {
            return _addBaseState(new BaseState(
                    regionName,
                    world.getUID(),
                    backupRegionName,
                    backupWorld.getUID()));
        } else {
            return false;
        }
    }

    /**
     * @param baseStateName Name of the BaseState WG Region.
     * @return True if the BaseState is rotated. False otherwise.
     */
    public boolean isRotated(final String baseStateName) {
        final BaseState state = states.get(baseStateName);
        return state != null && state.isRotated();
    }

    /**
     * Adds an AltState to specified BaseState.
     * @param baseStateRegionName BaseState WG region name.
     * @param altRegionName AltState WG region name.
     * @param altRegionWorld World of AltState region.
     * @return True if the AltState was added successfully. False otherwise.
     */
    public boolean addAltState(final String baseStateRegionName,
                               final String altRegionName,
                               final World altRegionWorld) {
        final BaseState state = states.get(baseStateRegionName);

        if (state != null
                && state.isValid()
                && altRegionWorld != null
                && state.addAltState(altRegionName, altRegionWorld.getUID())) {
            this.states.put(baseStateRegionName, state);
            return this.buffer.writeBaseState(state);
        } else {
            return false;
        }
    }

    public boolean removeBaseState(final String baseStateRegionName) {
        BaseState toRemove = states.remove(baseStateRegionName);
        if (toRemove != null) {
            return buffer.eraseBaseState(toRemove);
        } else {
            return false;
        }
    }

    public boolean removeAltState(final String baseStateName,
                                  final String altStateName) {
        final BaseState baseState = states.get(baseStateName);
        if (baseState != null) {
            final boolean success = baseState.removeAltState(altStateName);
            if (success) {
                return buffer.eraseAltState(baseState, altStateName);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean changeStateBroadcast(final String baseStateName, final String altStateName,
                                        final String broadcastMessage)
    {
        final BaseState baseState = states.get(baseStateName);
        return baseState != null &&
               baseState.changeStateBroadcast(altStateName, broadcastMessage) &&
               this.buffer.writeBaseState(baseState);
    }

    public boolean changeBaseStateBroadcast(final String baseStateName,
                                            final String broadcastMessage)
    {
        final BaseState baseState = states.get(baseStateName);
        return baseState != null && baseState.changeBaseStateBroadcast(broadcastMessage) &&
                this.buffer.writeBaseState(baseState);
    }

    public Location getBaseStateLocation(final String baseStateName)
    {
        final BaseState baseState = states.get(baseStateName);
        return (baseState != null) ? baseState.getLocation() : null;
    }

    public Location getAltStateLocation(final String baseStateName,
                                        final String altStateName)
    {
        final BaseState baseState = states.get(baseStateName);
        if (baseState != null) {
            final RegionState alt = baseState.getAltState(altStateName);
            return (alt != null) ? alt.getLocation() : null;
        } else {
            return null;
        }
    }

    /**
     * Resets the BaseState by rotating to its backup state.
     *
     * @param baseStateName BaseState to reset.
     * @return True if successful. False if failed.
     */
    public boolean resetBaseState(final String baseStateName,
                                  final boolean broadcast) {
        if (states.containsKey(baseStateName)) {
            final BaseState baseState = states.get(baseStateName);
            final boolean success = baseState.resetState();
            if (broadcast && success) {
                baseState.broadcastMessage();
            }
            return success;
        }
        return false;
    }

    /**
     * Rotates an AltState into a BaseState given their respective region names.
     *
     * @param baseStateName    WorldGuard region name of BaseState.
     * @param altStateName     WorldGuard region name of AltState.
     * @param rotateAir        Boolean to copy air to BaseState.
     * @param broadcastMessage Boolean to broadcast AltState message on success.
     * @return True if successful. False if failed.
     */
    public boolean rotateBaseStateBroadcast(final String baseStateName,
                                            final String altStateName,
                                            final boolean rotateAir,
                                            final boolean broadcastMessage) {
        if (states.containsKey(baseStateName)) {
            final BaseState baseState = states.get(baseStateName);
            final boolean success = baseState.rotateState(altStateName, rotateAir);
            if (success) {
                buffer.writeBaseState(baseState);
                if (broadcastMessage) {
                    baseState.getCurrentState().broadcastMessage();
                }
            }
            return success;
        }
        return false;
    }

    /** @return True if the StateController has a valid buffer. False otherwise. */
    public boolean isValid() {
        return this.buffer.isValid();
    }

    /** @return True if the StateController buffer has been deleted entirely. False otherwise. */
    public boolean destroy() {
        return this.buffer.destroy();
    }
}
