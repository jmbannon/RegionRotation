package net.projectzombie.region_rotation.commands.state;

import net.projectzombie.region_rotation.commands.CommandExecution;
import net.projectzombie.region_rotation.commands.RRText;
import net.projectzombie.region_rotation.modules.StateController;
import org.bukkit.command.CommandSender;

/**
 * Created by jb on 8/30/16.
 */
public abstract class StateExecution extends CommandExecution
{
    static public final StateExecution STATE_COMMANDS[] = {
            AltStateAdd.cmd(),
            AltStateRemove.cmd(),
            BaseStateAdd.cmd(),
            BaseStateRemove.cmd(),
            BaseStateReset.cmd(),
            BaseStateRotate.cmd(),
            BaseStateInfo.cmd(),
            BaseStateList.cmd(),
            SetStateBroadcast.cmd()
    };

    protected StateExecution(final String arg,
                             final String permission,
                             final int numberOfArgs,
                             final String commandArgs)
    {
        super(arg, permission, numberOfArgs, commandArgs);
    }

    protected boolean hasPermission(final CommandSender sender,
                                    final StateController controller)
    {
        return sender.hasPermission(RRText.formatPermission(controller, this.getPermission()));
    }

    /**
     * Executes the command. Will run iff first argument matches the command name.
     * @param args Command arguments.
     * @return String to send to the sender.
     */
    abstract protected String execute(final String args[],
                                      final StateController controller);

    /**
     * @param args Original command arguments.
     * @return Removes first String from array (the controller name).
     */
    static private String[] _StateArgs(final String args[]) {
        final String[] toRet = new String[args.length - 1];

        for (int i = 1; i < args.length; i++) {
            toRet[i - 1] = args[i];
        }
        return toRet;
    }

    final public boolean run(String args[],
                             final CommandSender sender,
                             final StateController controller)
    {
        args = _StateArgs(args);
        if (this.isCommand(args)) {
            final String messageToSend;
            if (!this.hasPermission(sender) && !this.hasPermission(sender, controller)) {
                messageToSend = getNoPermissionMsg();
            } else if (!this.hasValidArgLength(args)) {
                messageToSend = this.getCommandArgs();
            } else {
                messageToSend = this.execute(args, controller);
            }
            RRText.formatToSender(sender, messageToSend);
            return true;
        } else {
            return false;
        }
    }
}
