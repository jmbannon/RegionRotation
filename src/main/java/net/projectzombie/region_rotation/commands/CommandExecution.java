package net.projectzombie.region_rotation.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * Created by jb on 8/30/16.
 */
public abstract class CommandExecution
{
    private final String arg;
    private final String permission;
    private final int nrArgs;
    private final String commandArgs;

    protected CommandExecution(final String arg,
                               final String permission,
                               final int numberOfArgs,
                               final String commandArgs)
    {
        this.arg = arg;
        this.permission = permission;
        this.nrArgs = numberOfArgs;
        this.commandArgs = commandArgs;
    }

    static private String _noPermission()
    {
        return "You do not have permission to perform this command.";
    }

    protected final boolean isCommand(final String args[])
    {
        return args.length >= 1 && args[0].equalsIgnoreCase(arg);
    }

    protected final boolean hasPermission(final CommandSender sender)
    {
        return sender.hasPermission(this.permission);
    }

    protected final boolean hasValidArgLength(final String args[])
    {
        return args.length >= this.nrArgs;
    }

    /**
     * Executes the command. Will run iff first argument matches the command name.
     * @param args Command arguments.
     * @param sender Sender of the command.
     * @return String to send to the sender.
     */
    abstract protected String execute(final String args[],
                                      final CommandSender sender);



    final protected boolean run(final String args[],
                                final CommandSender sender)
    {
        if (this.isCommand(args)) {
            final String messageToSend;
            if (!this.hasPermission(sender)) {
                messageToSend = _noPermission();
            } else if (!this.hasValidArgLength(args)) {
                messageToSend = this.commandArgs;
            } else {
                messageToSend = this.execute(args, sender);
            }
            RRText.formatToSender(sender, messageToSend);
            return true;
        } else {
            return false;
        }
    }
}
