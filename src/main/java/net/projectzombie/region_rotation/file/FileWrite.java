package net.projectzombie.region_rotation.file;

import net.projectzombie.region_rotation.modules.BaseState;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by maxgr on 8/16/2016.
 */
public class FileWrite
{
    /**
     * Writes all the necessary info to fetch the BaseState in the future from disc.
     * @param baseState The BaseState being saved to disc.
     * @param world The world the BaseState resides in.
     * @return If the save was successful.
     */
    public static boolean writeBaseState(final BaseState baseState, final World world)
    {
        FileBuffer fileBuffer = FileBufferController.instance().getFile(world);
        fileBuffer.safeLoadFile(world);

        // Changing altstates into alstatesID
        List<String> altIDs = new ArrayList<>();

        // Write alts
        fileBuffer.file.set(FilePath.altStates(baseState), baseState.getAltStateIDs());

        // Write backup
        fileBuffer.file.set(FilePath.backupState(baseState), baseState.getBackupBaseStateID());

        // Write current state
        fileBuffer.file.set(FilePath.currentState(baseState), baseState.getCurrentState());

        return fileBuffer.saveFiles();
    }

    public static boolean writeBaseStates(final Collection<BaseState> baseStates)
    {
        boolean successfulSave = true;
        if (baseStates != null)
        {
            for (BaseState baseState : baseStates)
            {
                if (!writeBaseState(baseState, baseState.getWorld()))
                    successfulSave = false;
            }
        }

        return successfulSave;
    }

    /**
     * Used to erase the BaseState from disc.
     * @Return If the save was successful.
     */
    public static boolean flushBaseState(final BaseState baseState, final World world)
    {
        FileBuffer fileBuffer = FileBufferController.instance().getFile(world);
        fileBuffer.safeLoadFile(world);

        fileBuffer.file.set(FilePath.baseState(baseState), null);

        return fileBuffer.saveFiles();
    }


}
