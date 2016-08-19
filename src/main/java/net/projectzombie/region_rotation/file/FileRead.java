package net.projectzombie.region_rotation.file;

import net.projectzombie.region_rotation.modules.BaseState;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Used to read in BaseStates.
 * @author Gephery
 */
public class FileRead
{
    /**
     * Reads in a specific BaseState.
     * @param regionName The region the BaseState belongs to.
     * @param world The world which the BaseState resides in.
     * @return Base state from disc.
     */
    public static BaseState readBaseState(String regionName, World world)
    {
        FileBuffer fileBuffer = FileBufferController.instance().getFile(world);
        fileBuffer.safeLoadFile(world);
        UUID worldUID = world.getUID();
        BaseState baseState = null;
        String pathToBaseState = FilePath.baseState(regionName, worldUID);
        String pathToBackupS = FilePath.backupState(regionName, worldUID);
        String pathToCurrentS = FilePath.currentState(regionName, worldUID);
        String pathToAltState = FilePath.altStates(regionName, worldUID);
        if (fileBuffer.isSafePath(pathToBaseState))
            {
            List<String> altStateIDs = fileBuffer.file.getStringList(pathToAltState);

            String backupBaseStateID = fileBuffer.file.getString(pathToBackupS);
            String backupBaseRegion = BaseState.toRegion(backupBaseStateID);
            UUID backupBaseUID = BaseState.toWorldUID(backupBaseStateID);

            String currentState = fileBuffer.file.getString(pathToCurrentS);

            // Constructing baseState base.
            baseState = new BaseState(regionName, worldUID, backupBaseRegion, backupBaseUID);

            // Adding all the altStates
            if (altStateIDs != null)
            {
                for (String altStateID : altStateIDs)
                    baseState.addAltState(BaseState.toRegion(altStateID), BaseState.toWorldUID(altStateID));
            }
            baseState.rotateState(currentState, false); // Don't erase built things.
        }
        return baseState != null ? baseState : null;
    }

    /**
     * Reads in a specific BaseState.
     * @param regionName The region the BaseState belongs to.
     * @param worldUID The world which the BaseState resides in.
     * @return Base state from disc.
     */
    public static BaseState readBaseState(String regionName, UUID worldUID)
    {
        World world = Bukkit.getWorld(worldUID);
        return readBaseState(regionName, world);
    }

    public static Set<String> readBaseStateNames(World world)
    {
        FileBuffer fileBuffer = FileBufferController.instance().getFile(world);
        fileBuffer.safeLoadFile(world);
        String pathToBaseStates = FilePath.baseStates();
        return  fileBuffer.isSafePath(pathToBaseStates) ?
                fileBuffer.file.getConfigurationSection(pathToBaseStates).getKeys(false) :
                null;
    }

    /**
     * Reads a particular worlds BaseStates.
     * @param world The world to be checked.
     * @return Set of BaseStates in that world.
     */
    public static Set<BaseState> readBaseStates(World world)
    {
        FileBuffer fileBuffer = FileBufferController
                                .instance()
                                .getFile(world);
        fileBuffer.safeLoadFile(world);
        Set<BaseState> baseStates = new HashSet<>();
        Set<String> baseStateNames = readBaseStateNames(world);
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
     * Reads all BaseStates in every world.
     * @return All the BaseStates on the server.
     */
    public static Set<BaseState> readBaseStates()
    {
        Set<BaseState> baseStates = new HashSet<>();
        for (World world : Bukkit.getWorlds())
        {
            Set<BaseState> tempBaseStates = readBaseStates(world);
            if (tempBaseStates != null)
                baseStates.addAll(readBaseStates(world));
        }

        return baseStates;
    }
}
