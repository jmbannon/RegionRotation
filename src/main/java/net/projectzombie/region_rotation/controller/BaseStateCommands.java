package net.projectzombie.region_rotation.controller;

import com.sk89q.worldguard.bukkit.WGBukkit;
import net.projectzombie.region_rotation.modules.BaseState;
import net.projectzombie.region_rotation.modules.StateController;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static net.projectzombie.region_rotation.controller.BaseStateText.*;

/**
 * Location of all the commands for BaseState changes in-game.
 * @author Gephery
 */
public class BaseStateCommands implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0)
        {
            if (args[0].equals(ADD_BASESTATE_CMD) && sender.hasPermission(ADD_BASESTATE_PERM))
            {
                boolean success = false;
                BaseState baseState = null;
                if (args.length == 5)
                {
                    // Convert all the inputs
                    String regionName = args[1];
                    World world = Bukkit.getWorld(args[2]);
                    String backupRegionName = args[3];
                    World backupWorld = Bukkit.getWorld(args[4]);

                    if (world != null && backupWorld != null
                        && WGBukkit.getRegionManager(world).getRegion(regionName) != null
                        && WGBukkit.getRegionManager(backupWorld)
                                    .getRegion(backupRegionName) != null)
                    {
                        baseState = new BaseState(regionName,
                                                  world.getUID(),
                                                  backupRegionName,
                                                  backupWorld.getUID());
                        sender.sendMessage(baseState.isValid() + "");
                        success = StateController.instance().addBaseState(baseState);
                    }
                }
                sender.sendMessage(addBaseState(baseState, success));
            }
            else if (args[0].equals(ADD_ALT_BASESTATE_CMD) &&
                    sender.hasPermission(ADD_ALT_BASESTATE_PERM))
            {
                boolean success = false;
                if (args.length == 4)
                {
                    String regionName = args[1];
                    String altRegionName = args[2];
                    World altRegionWorld = Bukkit.getWorld(args[3]);
                    if (altRegionWorld != null)
                    {
                        success = StateController.instance().addAltState(regionName,
                                altRegionName,
                                altRegionWorld.getUID());
                    }
                }
                sender.sendMessage(addAltState(success));
            }
            else if (args[0].equals(REMOVE_BASESTATE_CMD) &&
                     sender.hasPermission(REMOVE_BASESTATE_PERM))
            {
                boolean success = false;
                if (args.length == 2)
                {
                    String regionName = args[1];
                    success = StateController.instance().removeBaseStateFully(regionName);
                }
                sender.sendMessage(removeBaseState(success));
            }
            else if (args[0].equals(RESET_BASESTATE_CMD) &&
                    sender.hasPermission(RESET_BASESTATE_PERM))
            {
                boolean success = false;
                if (args.length == 3)
                {
                    String regionName = args[1];
                    boolean broadcast = Boolean.valueOf(args[2]);
                    success = StateController.instance().resetBaseState(regionName, broadcast);
                    BaseState baseState = StateController.instance().getBaseState(regionName);
                    sender.sendMessage(success + ": " + baseState.getCurrentState().getRegionName()
                                        + ": " + baseState.getBackupBaseStateID());
                }
                sender.sendMessage(resetBaseState(success));
            }
            else if (args[0].equals(ROTATE_BASESTATE_CMD) &&
                    sender.hasPermission(ROTATE_BASESTATE_PERM))
            {
                boolean success = false;
                if (args.length == 5)
                {
                    String regionName = args[1];
                    String altRegionName = args[2];
                    boolean rotateAir = Boolean.valueOf(args[3]);
                    boolean broadcast = Boolean.valueOf(args[4]);

                    success = StateController.instance().rotateBaseStateBroadcast(regionName,
                                                                                  altRegionName,
                                                                                  rotateAir,
                                                                                  broadcast);
                }
                sender.sendMessage(rotateBaseState(success));
            }
            else if (args[0].equals(INFO_CMD) &&
                    sender.hasPermission(INFO_PERM))
            {
                boolean success = false;
                BaseState baseState = null;
                if (args.length == 2)
                {
                    String regionName = args[1];
                    baseState = StateController.instance().getBaseState(regionName);
                    success = baseState != null;
                }
                sender.sendMessage(info(success, baseState));
            }
        }
        else
        {
            sender.sendMessage(commandHelp());
        }
        return true;
    }
}
