package net.projectzombie.regionrotation.commands.state;

import net.projectzombie.regionrotation.commands.RRText;
import net.projectzombie.regionrotation.modules.StateController;
import org.bukkit.Bukkit;
import org.bukkit.World;

import static net.projectzombie.regionrotation.commands.RRText.*;

/**
 * Created by maxgr on 9/7/2016.
 */
public class SetStateBroadcast extends StateExecution
{
    public static final String BACKUP_STATE_REFERENCE = "backup";
    public static final String UNSET_BROADCAST_REFERENCE = "unset";
    private static SetStateBroadcast CMD = new SetStateBroadcast();
    static protected SetStateBroadcast cmd() { return CMD; }

    private SetStateBroadcast()
    {
        super(SET_BROADCAST_CMD, SET_BROADCAST_PERM, 5, SET_BROADCAST_HELP);
    }

    static private String _SetBCArgDNE(final String dneName)
    {
        StringBuilder stb = new StringBuilder();
        stb.append("Cannot set state broadcast: ");
        stb.append(dneName);
        stb.append(' ');
        stb.append("does not exist.");
        return stb.toString();
    }
    
    static private String _SetBCStateDNE(final String regionName)
    {
        return _SetBCArgDNE(RRText.formatAltStateName(regionName));
    }

    static private String _SetBCBaseStateDNE(final String baseStateName)
    {
        return _SetBCArgDNE(RRText.formatBaseStateName(baseStateName));
    }

    static private String _SetBCWorldDNE(final String worldName)
    {
        return _SetBCArgDNE(RRText.formatWorldName(worldName));
    }
    static private String _SetBCFailure()
    {
        return "An error occurred when setting the broadcast.";
    }

    static private String _SetBCSuccess(final String baseStateName, final String stateName,
                                           final String broadcastMsg)
    {
        final StringBuilder stb = new StringBuilder();
        stb.append("Successfully set broadcast msg of ");
        stb.append(RRText.formatBaseStateName(baseStateName));
        stb.append(':');
        stb.append(RRText.formatAltStateName(stateName));
        stb.append(" to: ");
        stb.append(broadcastMsg);
        return stb.toString();
    }

    @Override
    protected String execute(String[] args, StateController controller)
    {
        final String baseStateName = args[1];
        final World world = Bukkit.getWorld(args[2]);
        final String stateName = args[3];

        StringBuilder builder = new StringBuilder();
        for (int i = getNrArgs() - 1; i < args.length; i++) {
            builder.append(" ");
            builder.append(args[i]);
        }
        String broadcastMsg = builder.toString().trim();
        broadcastMsg = !broadcastMsg.equalsIgnoreCase(UNSET_BROADCAST_REFERENCE) ? broadcastMsg : null;

        final boolean success;

        if (world == null) {
            return _SetBCWorldDNE(args[2]);
        }
        else if (!controller.baseStateExists(baseStateName)) {
            return _SetBCBaseStateDNE(baseStateName);
        }
        else {
            if (stateName.equalsIgnoreCase(BACKUP_STATE_REFERENCE)) {
                success = controller.changeBaseStateBroadcast(baseStateName, broadcastMsg);
            } else {
                if (!controller.altStateExists(baseStateName, stateName)) {
                    return _SetBCStateDNE(stateName);
                } else {
                    success = controller.changeStateBroadcast(baseStateName,
                                                              stateName, broadcastMsg);
                }
            }
            if (success) {
                return _SetBCSuccess(baseStateName, stateName, broadcastMsg);
            } else {
                return _SetBCFailure();
            }
        }
    }
}
