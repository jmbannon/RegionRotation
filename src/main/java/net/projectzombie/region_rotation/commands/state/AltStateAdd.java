package net.projectzombie.region_rotation.commands.state;

import net.projectzombie.region_rotation.commands.RRText;
import net.projectzombie.region_rotation.modules.StateController;
import org.bukkit.Bukkit;
import org.bukkit.World;

import static net.projectzombie.region_rotation.commands.RRText.*;

/**
 * Created by jb on 9/4/16.
 */
public class AltStateAdd extends StateExecution
{
    private static AltStateAdd CMD = new AltStateAdd();
    static protected AltStateAdd cmd() {
        return CMD;
    }

    private AltStateAdd()
    {
        super(ADD_ALT_BASESTATE_CMD, ADD_ALT_BASESTATE_PERM, 4, ADD_ALT_BASESTATE_HELP);
    }

    static private String _AddAltStateArgDNE(final String dneName)
    {
        StringBuilder stb = new StringBuilder();
        stb.append("Cannot create AltState: ");
        stb.append(RRText.formatRegionName(dneName));
        stb.append(' ');
        stb.append("does not exist.");
        return stb.toString();
    }

    static private String _AddAltStateRegionDNE(final String regionName)
    {
        return _AddAltStateArgDNE(RRText.formatRegionName(regionName));
    }

    static private String _AddAltStateWorldDNE(final String worldName)
    {
        return _AddAltStateArgDNE(RRText.formatWorldName(worldName));
    }

    static private String _AddAltStateSuccess(final String altRegionName,
                                              final String baseStateName)
    {
        final StringBuilder stb = new StringBuilder();
        stb.append("Successfully added ");
        stb.append(RRText.formatAltStateName(altRegionName));
        stb.append(" as an AltState to ");
        stb.append(RRText.formatBaseStateName(baseStateName));
        stb.append(".");
        return stb.toString();
    }

    static private String _AddAltStateFailure()
    {
        return "An error occurred when adding AltState to BaseState.";
    }

    @Override
    protected String execute(final String args[],
                             final StateController controller)
    {
        final String baseStateName = args[1];
        final String altRegionName = args[2];
        final World altRegionWorld = Bukkit.getWorld(args[3]);

        final boolean success;

        if (altRegionWorld == null) {
            return _AddAltStateWorldDNE(args[3]);
        } else if (!controller.baseStateExists(baseStateName)) {
            return _AddAltStateRegionDNE(baseStateName);
        } else if (!controller.regionExists(altRegionName, altRegionWorld)) {
            return _AddAltStateRegionDNE(altRegionName);
        } else {
            success = controller.addAltState(baseStateName, altRegionName, altRegionWorld);
            if (success) {
                return _AddAltStateSuccess(altRegionName, baseStateName);
            } else {
                return _AddAltStateFailure();
            }
        }
    }
}
