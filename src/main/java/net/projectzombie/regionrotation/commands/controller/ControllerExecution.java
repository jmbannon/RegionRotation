package net.projectzombie.regionrotation.commands.controller;

import net.projectzombie.regionrotation.commands.CommandExecution;
import net.projectzombie.regionrotation.commands.RRText;
import org.bukkit.command.CommandSender;

/**
 * Created by jb on 9/5/16.
 */
public abstract class ControllerExecution extends CommandExecution
{
    static public ControllerExecution[] CONTROLLER_COMMANDS = {
        ControllerCreate.cmd(),
        ControllerDestroy.cmd(),
        ControllerList.cmd(),
    };

    protected ControllerExecution(final String arg,
                             final String permission,
                             final int numberOfArgs,
                             final String commandArgs)
    {
        super(arg, permission, numberOfArgs, commandArgs);
    }

    /**
     * Executes the command. Will run iff first argument matches the command name.
     * @param args Command arguments.
     * @return String to send to the sender.
     */
    abstract protected String execute(final String args[]);

    final public boolean run(final String args[],
                                final CommandSender sender)
    {
        if (this.isCommand(args)) {
            final String messageToSend;
            if (!this.hasPermission(sender)) {
                messageToSend = getNoPermissionMsg();
            } else if (!this.hasValidArgLength(args)) {
                messageToSend = RRText.formatControllerCommand(this.getCommandArgs());
            } else {
                messageToSend = this.execute(args);
            }
            RRText.formatToSender(sender, messageToSend);
            return true;
        } else {
            return false;
        }
    }
}
