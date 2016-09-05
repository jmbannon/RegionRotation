package net.projectzombie.region_rotation.commands;

import org.bukkit.command.CommandSender;

/**
 * Created by jb on 9/4/16.
 */
public abstract class CommandExecution
{
    private final String arg;
    private final String permission;
    private final int nrArgs;
    private final String commandArgs;

    public CommandExecution(final String arg,
                               final String permission,
                               final int numberOfArgs,
                               final String commandArgs)
    {
        this.arg = arg;
        this.permission = permission;
        this.nrArgs = numberOfArgs;
        this.commandArgs = commandArgs;
    }

    static public final String getNoPermissionMsg()
    {
        return "You do not have permission to perform this command.";
    }

    final protected String getCommandArgs() {
        return this.commandArgs;
    }

    final protected String getPermission() {
        return this.permission;
    }

    final protected boolean isCommand(final String args[])
    {
        return args.length >= 1 && args[0].equalsIgnoreCase(arg);
    }

    final protected boolean hasPermission(final CommandSender sender)
    {
        return sender.hasPermission(RRText.formatPermission(this.permission));
    }

    final protected boolean hasValidArgLength(final String args[])
    {
        return args.length >= this.nrArgs;
    }
}
