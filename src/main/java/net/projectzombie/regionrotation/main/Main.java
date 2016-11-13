/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.projectzombie.regionrotation.main;

import static net.projectzombie.regionrotation.commands.RRText.*;

import net.projectzombie.regionrotation.commands.Commands;
import net.projectzombie.regionrotation.modules.StateControllers;
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
        StateControllers.onEnable();

        PluginManager pm = getServer().getPluginManager();
        for (String perm : PERMISSIONS)
            pm.addPermission(new Permission(perm));

        this.getCommand(COMMAND_ROOT).setExecutor(new Commands());
        this.getLogger().info("RegionRotation enabled!");
    }

    @Override
    public void onDisable()
    {
        StateControllers.onDisable();
        this.getLogger().info("RegionRotation disabled!");
    }
}
