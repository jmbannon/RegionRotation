package net.projectzombie.regionrotation.commands.state;

import net.projectzombie.regionrotation.commands.RRText;
import net.projectzombie.regionrotation.modules.StateController;
import org.bukkit.Bukkit;
import org.bukkit.World;

import static net.projectzombie.regionrotation.commands.RRText.ADD_BASESTATE_CMD;
import static net.projectzombie.regionrotation.commands.RRText.ADD_BASESTATE_PERM;
import static net.projectzombie.regionrotation.commands.RRText.ADD_BASESTATE_HELP;


/**
 * Created by jb on 8/30/16.
 */
public class BaseStateAdd extends StateExecution
{
    private static BaseStateAdd CMD = new BaseStateAdd();
    static protected BaseStateAdd cmd() {
        return CMD;
    }

    private BaseStateAdd()
    {
        super(ADD_BASESTATE_CMD, ADD_BASESTATE_PERM, 5, ADD_BASESTATE_HELP);
    }

    static private String _BaseStateAddArgDNE(final String dneName)
    {
        StringBuilder stb = new StringBuilder();
        stb.append("Cannot create BaseState: ");
        stb.append(dneName);
        stb.append(' ');
        stb.append("does not exist.");
        return stb.toString();
    }

    static private String _BaseStateAddRegionDNE(final String regionName)
    {
        return _BaseStateAddArgDNE(RRText.formatRegionName(regionName));
    }

    static private String _BaseStateAddWorldDNE(final String worldName)
    {
        return _BaseStateAddArgDNE(RRText.formatWorldName(worldName));
    }

    static private String _BaseStateAddSuccess(final String regionName)
    {
        final StringBuilder stb = new StringBuilder();
        stb.append("Successfully added ");
        stb.append(RRText.formatRegionName(regionName));
        stb.append(" as a BaseState.");
        return stb.toString();
    }

    static private String _BaseStateAddFailure()
    {
        return "An error occurred when creating BaseState.";
    }

    @Override
    protected String execute(final String args[],
                             final StateController controller)
    {
        final String regionName = args[1];
        final World world = Bukkit.getWorld(args[2]);
        final String backupRegionName = args[3];
        final World backupWorld = Bukkit.getWorld(args[4]);
        final boolean success;

        if (world == null) {
            return _BaseStateAddWorldDNE(args[2]);
        } else if (backupWorld == null) {
            return _BaseStateAddWorldDNE(args[4]);
        } else if (!controller.regionExists(regionName, world)) {
            return _BaseStateAddRegionDNE(regionName);
        } else if (!controller.regionExists(backupRegionName, world)) {
            return _BaseStateAddRegionDNE(backupRegionName);
        } else {
            success = controller.addBaseState(regionName, world, backupRegionName, backupWorld);
            if (success) {
                return _BaseStateAddSuccess(regionName);
            } else {
                return _BaseStateAddFailure();
            }
        }
    }
}
