package net.projectzombie.region_rotation.file;

import net.projectzombie.region_rotation.modules.BaseState;
import org.bukkit.World;

import java.util.List;
import java.util.UUID;

/**
 * Created by maxgr on 8/16/2016.
 */
public class FileRead
{
    /**
     * Reads in a specific BaseState.
     * @param regionName The region the BaseState belongs to.
     * @param world The world which the BaseState resides in.
     * @return
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
}
