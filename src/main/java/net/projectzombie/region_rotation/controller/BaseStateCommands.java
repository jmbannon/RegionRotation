package net.projectzombie.region_rotation.controller;

import net.projectzombie.region_rotation.modules.StateController;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Set;

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
            if (args[0].equalsIgnoreCase(ADD_BASESTATE_CMD) && sender.hasPermission(ADD_BASESTATE_PERM))
            {
                String regionName = null;
                boolean success = false;
                if (args.length == 5)
                {
                    // Convert all the inputs
                    regionName = args[1];
                    World world = Bukkit.getWorld(args[2]);
                    String backupRegionName = args[3];
                    World backupWorld = Bukkit.getWorld(args[4]);

                    success = StateController.instance().addBaseState(regionName, world, backupRegionName, backupWorld);
                }
                // TODO fix
                sender.sendMessage(addBaseState(regionName, success));
            }
            else if (args[0].equalsIgnoreCase(ADD_ALT_BASESTATE_CMD) &&
                    sender.hasPermission(ADD_ALT_BASESTATE_PERM))
            {
                boolean success = false;
                if (args.length == 4)
                {
                    String regionName = args[1];
                    String altRegionName = args[2];
                    World altRegionWorld = Bukkit.getWorld(args[3]);

                    success = StateController.instance().addAltState(regionName,
                            altRegionName,
                            altRegionWorld);
                }
                sender.sendMessage(addAltState(success));
            }
            else if (args[0].equalsIgnoreCase(REMOVE_BASESTATE_CMD) &&
                     sender.hasPermission(REMOVE_BASESTATE_PERM))
            {
                boolean success = false;
                if (args.length == 2)
                {
                    String regionName = args[1];
                    success = StateController.instance().removeBaseState(regionName);
                }
                sender.sendMessage(removeBaseState(success));
            }
            else if (args[0].equalsIgnoreCase(RESET_BASESTATE_CMD) &&
                    sender.hasPermission(RESET_BASESTATE_PERM))
            {
                boolean success = false;
                if (args.length == 3)
                {
                    String regionName = args[1];
                    boolean broadcast = Boolean.valueOf(args[2]);
                    success = StateController.instance().resetBaseState(regionName, broadcast);
                }
                sender.sendMessage(resetBaseState(success));
            }
            else if (args[0].equalsIgnoreCase(ROTATE_BASESTATE_CMD) &&
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
            else if (args[0].equalsIgnoreCase(INFO_BASESTATE_CMD) &&
                    sender.hasPermission(INFO_BASESTATE_PERM))
            {
                boolean success = false;
                String baseStateInfo = null;
                String regionName = null;

                if (args.length == 2)
                {
                    regionName = args[1];
                    baseStateInfo = StateController.instance().getBaseStateInfo(regionName);
                    success = baseStateInfo != null;
                }
                sender.sendMessage(info(success, regionName));
            }
            else if (args[0].equalsIgnoreCase(LIST_BASESTATE_CMD) &&
                    sender.hasPermission(LIST_BASESTATE_PERM))
            {
                String baseStates = "";
                if (args.length == 1)
                {
                    final Set<String> baseStateNames = StateController.instance().getBaseStateNames();
                    if (baseStateNames != null) {
                        for (String baseStateN : StateController.instance().getBaseStateNames())
                            baseStates += ", " + baseStateN;
                        if (baseStates.startsWith(", "))
                            baseStates = baseStates.substring(2);
                    }
                }
                sender.sendMessage(listBaseState(baseStates));
            }
        }
        else
        {
            sender.sendMessage(commandHelp());
        }
        return true;
    }
}
