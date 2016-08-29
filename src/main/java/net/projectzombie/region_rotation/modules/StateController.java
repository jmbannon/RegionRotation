package net.projectzombie.region_rotation.modules;

import net.projectzombie.region_rotation.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by jb on 8/10/16.
 */
public class StateController
{
    static private final String DEFAULT_FILE_NAME = "region_rotation.yml";
    private static StateController STATE_CONTROLLER_SINGLETON = null;

    /** Initializes the StateController. Must be called first in Main. */
    static public void init()
    {
        // TODO: ability to create multiple state controllers
        STATE_CONTROLLER_SINGLETON = new StateController(
                new StateBuffer(Main.plugin().getDataFolder(), DEFAULT_FILE_NAME));
    }

    /** @return Initialized StateController. */
    static public StateController instance() { return STATE_CONTROLLER_SINGLETON; }

    private final HashMap<String, BaseState> states;
    private final StateBuffer buffer;

    /**
     * Constructs the singleton StateController and initializes the static plugins.
     */
    private StateController(final StateBuffer buffer)
    {
        this.buffer = buffer;
        this.states = new HashMap<>();

        // Adding baseStates from disc.
        if (buffer.isValid()) {
            Set<BaseState> baseStates = buffer.readBaseStates();
            if (baseStates != null) {
                for (BaseState baseState : baseStates) {
                    states.put(baseState.getRegionName(), baseState);
                }
            }
        }
    }

    public String getBaseStateInfo(final String regionName)
    {
        final BaseState toInfo = states.get(regionName);
        if (toInfo != null)
        {
            return toInfo.toString();
        }
        else
        {
            return null;
        }
    }

    /** To be used onDisable() to ensure all BaseStates are there after restart. *
     * @Return If the save was successful.
     */
    public boolean saveBaseStates()
    {
        return buffer.writeBaseStates(states.values());
    }

    public Set<String> getBaseStateNames()
    {
        return buffer.readBaseStateNames();
    }


    /**
     * Adds a BaseState to the StateController
     * @param state BaseState to add.
     * @return True if successful. False if failed.
     */
    private boolean _addBaseState(final BaseState state)
    {
        if (state.isValid() && buffer.writeBaseState(state))
        {
            states.put(state.getRegionName(), state);
            return true;
        }
        return false;
    }

    public boolean addBaseState(final String regionName,
                                final World world,
                                final String backupRegionName,
                                final World backupWorld)
    {
        if (world != null && backupWorld != null)
        {
            return _addBaseState(new BaseState(
                    regionName,
                    world.getUID(),
                    backupRegionName,
                    backupWorld.getUID()));
        }
        else
        {
            return false;
        }
    }

    public boolean addAltState(final String regionName,
                               final String altRegionName,
                               final World altRegionWorld)
    {
        final BaseState state = states.get(regionName);

        if (state != null
                && state.isValid()
                && altRegionWorld != null
                && state.addAltState(altRegionName, altRegionWorld.getUID()))
        {
            this.states.put(regionName, state);
            return this.buffer.writeBaseState(state);
        } else {
            return false;
        }
    }

    public boolean removeBaseState(final String baseStateRegionName)
    {
        BaseState toRemove = states.remove(baseStateRegionName);
        if (toRemove != null) {
            return buffer.flushBaseState(toRemove);
        } else {
            return false;
        }
    }

    /**
     * Resets the BaseState by rotating to its backup state.
     * @param baseStateName BaseState to reset.
     * @return True if successful. False if failed.
     */
    public boolean resetBaseState(final String baseStateName,
                                  final boolean broadcast)
    {
        if (states.containsKey(baseStateName))
        {
            final BaseState baseState = states.get(baseStateName);
            final boolean success = baseState.resetState();
            if (broadcast && success)
            {
                baseState.broadcastMessage();
            }
            return success;
        }
        return false;
    }

    /**
     * Rotates an AltState into a BaseState given their respective region names.
     * @param baseStateName WorldGuard region name of BaseState.
     * @param altStateName WorldGuard region name of AltState.
     * @param rotateAir Boolean to copy air to BaseState.
     * @param broadcastMessage Boolean to broadcast AltState message on success.
     * @return True if successful. False if failed.
     */
    public boolean rotateBaseStateBroadcast(final String baseStateName,
                                            final String altStateName,
                                            final boolean rotateAir,
                                            final boolean broadcastMessage)
    {
        if (states.containsKey(baseStateName))
        {
            final BaseState baseState = states.get(baseStateName);
            final boolean success = baseState.rotateState(altStateName, rotateAir);
            if (success)
            {
                buffer.writeBaseState(baseState);
                if (broadcastMessage)
                {
                    baseState.getCurrentState().broadcastMessage();;
                }
            }
            return success;
        }
        return false;
    }


}
