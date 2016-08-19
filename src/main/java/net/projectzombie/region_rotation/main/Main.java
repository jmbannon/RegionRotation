/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.projectzombie.region_rotation.main;

import static net.projectzombie.region_rotation.controller.BaseStateText.*;

import net.projectzombie.region_rotation.controller.BaseStateCommands;
import net.projectzombie.region_rotation.file.FileBufferController;
import net.projectzombie.region_rotation.modules.StateController;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author jesse
 */
public class Main extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        // Start up read ins and sorts.
        FileBufferController.init(this);
        StateController.init(this);

        // Adding perms.
        String[] perms = {ADD_BASESTATE_PERM, ADD_ALT_BASESTATE_PERM, REMOVE_BASESTATE_PERM,
                          RESET_BASESTATE_PERM, ROTATE_BASESTATE_PERM, INFO_PERM};
        PluginManager pM = getServer().getPluginManager();
        for (String perm : perms)
            pM.addPermission(new Permission(perm));

        // Channeling Region Rotation commands to BaseStateCommands.
        this.getCommand(COMMAND_ROOT).setExecutor(new BaseStateCommands());
    }

    @Override
    public void onDisable()
    {
        StateController.instance().saveBaseStatesToDisc();
        this.getLogger().info("DynamicRegions disabled!");
    }
}
