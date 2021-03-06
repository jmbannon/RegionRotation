package net.projectzombie.regionrotation.commands;

import net.projectzombie.regionrotation.commands.controller.ControllerExecution;
import net.projectzombie.regionrotation.commands.state.*;
import net.projectzombie.regionrotation.modules.StateController;
import net.projectzombie.regionrotation.modules.StateControllers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static net.projectzombie.regionrotation.commands.RRText.*;
import static net.projectzombie.regionrotation.commands.controller.ControllerExecution.CONTROLLER_COMMANDS;
import static net.projectzombie.regionrotation.commands.state.StateExecution.STATE_COMMANDS;

/**
 * Used to handle commands and branch them out to their respective channels.
 * @author jmbannon
 */
public class Commands implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        StateController controller;
        boolean ranCommand;

        if (args.length == 0) {
            RRText.formatToSender(sender, CONTROLLER_COMMAND_LIST);
        } else {
            for (ControllerExecution cmd : CONTROLLER_COMMANDS) {
                ranCommand = cmd.run(args, sender);
                if (ranCommand) {
                    return true;
                }
            }
            controller = StateControllers.get(args[0]);
            if (controller != null) {
                for (StateExecution cmd : STATE_COMMANDS) {
                    ranCommand = cmd.run(args, sender, controller);
                    if (ranCommand) {
                        return true;
                    }
                }
                RRText.formatToSender(sender, formatStateCommand(controller, STATE_COMMAND_LIST));
            } else {
                RRText.formatToSender(sender, formatControllerCommand(CONTROLLER_COMMAND_LIST));
            }
        }
        return true;
    }
}
