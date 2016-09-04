package net.projectzombie.region_rotation.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static net.projectzombie.region_rotation.commands.RRText.*;

/**
 * Location of all the commands for BaseState changes in-game.
 * @author Gephery
 */
public class Commands implements CommandExecutor
{
    static private final CommandExecution COMMANDS[] = {
            AltStateAdd.cmd(),
            AltStateRemove.cmd(),
            BaseStateAdd.cmd(),
            BaseStateRemove.cmd(),
            BaseStateReset.cmd(),
            BaseStateRotate.cmd(),
            BaseStateInfo.cmd(),
            BaseStateList.cmd()
    };

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean ranCommand = false;

        for (CommandExecution cmd : COMMANDS) {
            ranCommand = cmd.run(args, sender);
            if (ranCommand) {
                break;
            }
        }
        if (!ranCommand) {
            RRText.formatToSender(sender, COMMAND_LIST);
        }
        return true;
    }
}
