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
 * @Author Gephery
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

        List<String> altStateIDs = fileBuffer.file.getStringList(FilePath.altStates(regionName, worldUID));

        String backupBaseStateID = fileBuffer.file.getString(FilePath.baseState(regionName, worldUID));
        String backupBaseRegion = BaseState.toRegion(backupBaseStateID);
        UUID backupBaseUID = BaseState.toWorldUID(backupBaseStateID);

        String currentState = fileBuffer.file.getString(FilePath.currentState(regionName, worldUID));

        // Constructing baseState base.
        BaseState baseState = new BaseState(regionName, worldUID, backupBaseRegion, backupBaseUID);

        // Adding all the altStates
        if (altStateIDs != null)
        {
            for (String altStateID : altStateIDs)
                baseState.addAltState(BaseState.toRegion(altStateID), BaseState.toWorldUID(altStateID));
        }

        return baseState;
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
        FileBuffer fileBuffer = FileBufferController.instance().getFile(world);
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
     * @return
     */
    public static Set<BaseState> readBaseStates()
    {
        Set<BaseState> baseStates = new HashSet<>();
        for (World world : Bukkit.getWorlds())
            baseStates.addAll(readBaseStates(world));

        return baseStates;
    }
}
