package net.projectzombie.regionrotation.modules;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by jb on 8/25/16.
 */
public class StateBuffer
{
    private static final String BROADCAST_PLACEHOLDER = "NULL";

    private File file;
    private YamlConfiguration yml;
    private boolean isValid;

    private boolean _init(final File file)
    {
        if (!file.exists())
        {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }

        if (file.isFile()) {
            this.file = file;
            this.yml = YamlConfiguration.loadConfiguration(this.file);
            this.isValid = true;
            return this.saveFile();
        } else {
            return false;
        }
    }

    protected StateBuffer(final File file)
    {
        this.isValid = _init(file);
    }

    protected StateBuffer(final String filePath)
    {
        this.isValid = _init(new File(filePath));
    }

    protected StateBuffer(final File dir, final String fileName)
    {
        if (dir.isDirectory() || dir.mkdir()) {
            this.isValid = _init(new File(dir.getPath() + File.separatorChar + fileName));
        } else {
            this.isValid = false;
        }
    }

    protected StateBuffer(final String dirPath, final String fileName)
    {
        File dir = new File(dirPath);
        if (dir.isDirectory() || dir.mkdir()) {
            this.isValid = _init(new File(dir.getPath() + File.separatorChar + fileName));
        } else {
            this.isValid = false;
        }
    }

    protected boolean isValid() {
        return this.isValid;
    }

    /**
     * Reads in a specific BaseState.
     * @param regionName The region the BaseState belongs to.
     * @param world The world which the BaseState resides in.
     * @return Base state from disc.
     */
    protected BaseState readBaseState(String regionName, World world)
    {
        UUID worldUID = world.getUID();
        BaseState baseState = null;

        String pathToBaseState = BaseState.baseStatePath(regionName, worldUID);
        String pathToBackupS = BaseState.backupStatePath(regionName, worldUID);
        String pathToCurrentS = BaseState.currentStatePath(regionName, worldUID);
        String pathToAltStates = BaseState.altStatesPath(regionName, worldUID);

        if (this.yml.contains(pathToBaseState))
        {
            Set<String> altStateIDs = this.yml.getConfigurationSection(pathToAltStates).getKeys(false);
            String backupBaseStateID = this.yml.getConfigurationSection(pathToBackupS).getKeys(false).iterator().next();
            String backupBaseRegion = BaseState.toRegion(backupBaseStateID);
            UUID backupBaseUID = BaseState.toWorldUID(backupBaseStateID);

            String currentState = this.yml.getString(pathToCurrentS);

            // Constructing baseState base.
            baseState = new BaseState(regionName, worldUID, backupBaseRegion, backupBaseUID);

            String backupBroadcast = this.yml.getString(baseState.getBackupBroadcastPath());
            backupBroadcast = !backupBroadcast.equals(BROADCAST_PLACEHOLDER) ? backupBroadcast : null;

            // Adding all the altStates
            if (altStateIDs != null)
            {
                for (String altStateID : altStateIDs) {
                    String altRegionName = BaseState.toRegion(altStateID);
                    String altBroadcast = !this.yml.getString(baseState.getAltStatePath(altStateID)).equals(BROADCAST_PLACEHOLDER) ?
                                          this.yml.getString(baseState.getAltStatePath(altStateID)) : null;
                    baseState.addAltState(altRegionName,
                            BaseState.toWorldUID(altStateID));
                    baseState.changeStateBroadcast(altRegionName, altBroadcast);
                }
            }
            baseState.rotateState(currentState, false); // Don't erase built things.
            baseState.changeBaseStateBroadcast(backupBroadcast);
        }

        return baseState;
    }

    /**
     * Reads in a specific BaseState.
     * @param regionName The region the BaseState belongs to.
     * @param worldUID The world which the BaseState resides in.
     * @return Base state from disc.
     */
    protected BaseState readBaseState(String regionName, UUID worldUID)
    {
        World world = Bukkit.getWorld(worldUID);
        return readBaseState(regionName, world);
    }

    protected Set<String> readBaseStateNames()
    {
        String pathToBaseStates = BaseState.path();
        return this.yml.contains(pathToBaseStates) ?
                this.yml.getConfigurationSection(pathToBaseStates).getKeys(false) :
                null;
    }

    /**
     * Reads a particular worlds BaseStates.
     * @return Set of BaseStates in that world.
     */
    protected Set<BaseState> readBaseStates()
    {
        Set<BaseState> baseStates = new HashSet<>();
        Set<String> baseStateNames = readBaseStateNames();
        if (baseStateNames != null)
        {
            for (String baseStateID : baseStateNames)
            {
                baseStates.add(readBaseState(BaseState.toRegion(baseStateID),
                        BaseState.toWorldUID(baseStateID)));
            }
        }

        return baseStates;
    }

    /**
     * Writes all the necessary info to fetch the BaseState in the future from disc.
     * @param baseState The BaseState being saved to disc.
     */
    protected boolean writeBaseState(final BaseState baseState)
    {
        // Write alts
        Map<String, String> alts = baseState.getAltMapOfIDToBroadcast();
        //this.yml.set(baseState.getAltStatePath(), baseState.getAltStateFileIDs());
        for (String altID : alts.keySet()) {
            this.yml.set(baseState.getAltStatePath(altID), alts.get(altID) != null ? alts.get(altID) : BROADCAST_PLACEHOLDER);
        }

        // Write backup
        this.yml.set(baseState.getBackupStatePath(), baseState.getBackupStateFileID());
        this.yml.set(baseState.getBackupBroadcastPath(), baseState.getBackupBroadcast() != null ?
                                                         baseState.getBackupBroadcast() :
                                                         BROADCAST_PLACEHOLDER);

        // Write current state
        this.yml.set(baseState.getCurrentStatePath(), baseState.getCurrentState().getRegionName());

        return this.saveFile();
    }

    /**
     * Writes a Collection of BaseStates to disc.
     * @param baseStates The BaseStates being saved.
     */
    protected boolean writeBaseStates(final Collection<BaseState> baseStates)
    {
        if (baseStates != null)
        {
            for (BaseState baseState : baseStates)
            {
                writeBaseState(baseState);
            }
        }
        return this.saveFile();
    }

    /**
     * Used to erase the BaseState from disc.
     */
    protected boolean eraseBaseState(final BaseState baseState)
    {
        this.yml.set(baseState.getPath(), null);
        return this.saveFile();
    }

    protected boolean eraseAltState(final BaseState baseState,
                                    final String altStateName)
    {
        if (baseState.getAltStateFileIDs().contains(altStateName)) {
            baseState.removeAltState(altStateName);
        }
        return this.writeBaseState(baseState);
    }

    /**
     * Saves yml to disc.
     * @return If the save was successful.
     */
    protected boolean saveFile()
    {
        if (this.isValid) {
            try {
                this.yml.save(file);
                return true;
            } catch (IOException exc) {
                exc.printStackTrace();
                this.isValid = false;
                return false;
            }
        } else {
            return false;
        }
    }

    protected boolean destroy() {
        this.isValid = false;
        if (file != null) {
            return file.delete();
        } else {
            return true;
        }
    }
}
