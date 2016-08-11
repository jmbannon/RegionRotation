/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.projectzombie.region_rotation.main;

import net.projectzombie.region_rotation.modules.StateController;
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
        StateController.init(this);
    }

    @Override
    public void onDisable()
    {
        this.getLogger().info("DynamicRegions disabled!");
    }
}
