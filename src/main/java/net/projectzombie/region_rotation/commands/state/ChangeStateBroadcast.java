package net.projectzombie.region_rotation.commands.state;

import net.projectzombie.region_rotation.commands.RRText;
import net.projectzombie.region_rotation.modules.StateController;
import org.bukkit.Bukkit;
import org.bukkit.World;

import static net.projectzombie.region_rotation.commands.RRText.*;

/**
 * Created by maxgr on 9/7/2016.
 */
public class ChangeStateBroadcast extends StateExecution
{
    public static final String BACKUP_STATE_REFERENCE = "backup";
    private static ChangeStateBroadcast CMD = new ChangeStateBroadcast();
    static protected ChangeStateBroadcast cmd() { return CMD; }

    private ChangeStateBroadcast()
    {
        super(CHANGE_BROADCAST_CMD, CHANGE_BROADCAST_PERM, 5, CHANGE_BROADCAST_HELP);
    }

    static private String _ChangeBCArgDNE(final String dneName)
    {
        StringBuilder stb = new StringBuilder();
        stb.append("Cannot change state broadcast: ");
        stb.append(dneName);
        stb.append(' ');
        stb.append("does not exist.");
        return stb.toString();
    }

    static private String _ChangeBCStateDNE(final String regionName)
    {
        return _ChangeBCArgDNE(RRText.formatAltStateName(regionName));
    }

    static private String _ChangeBCBaseStateDNE(final String regionName)
    {
        return _ChangeBCArgDNE(RRText.formatBaseStateName(regionName));
    }

    static private String _ChangeBCWorldDNE(final String worldName)
    {
        return _ChangeBCArgDNE(RRText.formatWorldName(worldName));
    }
    static private String _ChangeBCFailure()
    {
        return "An error occurred when changing the broadcast.";
    }

    static private String _ChangeBCSuccess(final String baseStateName, final String stateName,
                                           final String broadcastMsg)
    {
        final StringBuilder stb = new StringBuilder();
        stb.append("Successfully changed broadcast msg of ");
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

        final boolean success;

        if (world == null) {
            return _ChangeBCWorldDNE(args[2]);
        }
        else if (!controller.baseStateExists(baseStateName)) {
            return _ChangeBCBaseStateDNE(baseStateName);
        }
        else {
            if (stateName.equalsIgnoreCase(BACKUP_STATE_REFERENCE)) {
                success = controller.changeBaseStateBroadcast(baseStateName, broadcastMsg);
            } else {
                if (!controller.altStateExists(baseStateName, stateName)) {
                    return _ChangeBCStateDNE(stateName);
                } else {
                    success = controller.changeStateBroadcast(baseStateName,
                                                              stateName, broadcastMsg);
                }
            }
            if (success) {
                return _ChangeBCSuccess(baseStateName, stateName, broadcastMsg);
            } else {
                return _ChangeBCFailure();
            }
        }
    }
}
