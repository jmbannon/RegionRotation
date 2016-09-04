/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.projectzombie.region_rotation.main;

import static net.projectzombie.region_rotation.commands.RRText.*;

import net.projectzombie.region_rotation.commands.Commands;
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
    static private JavaPlugin PLUGIN;

    static public JavaPlugin plugin()
    { return PLUGIN; }

    @Override
    public void onEnable()
    {
        PLUGIN = this;
        StateController.init();

        PluginManager pm = getServer().getPluginManager();
        for (String perm : PERMISSIONS)
            pm.addPermission(new Permission(perm));

        this.getCommand(COMMAND_ROOT).setExecutor(new Commands());
        this.getLogger().info("RegionRotation enabled!");
    }

    @Override
    public void onDisable()
    {
        StateController.instance().saveBaseStates();
        this.getLogger().info("RegionRotation disabled!");
    }
}
