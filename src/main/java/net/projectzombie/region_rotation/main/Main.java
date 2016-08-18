/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.projectzombie.region_rotation.main;

import static net.projectzombie.region_rotation.controller.BaseStateText.COMMAND_ROOT;

import net.projectzombie.region_rotation.controller.BaseStateCommands;
import net.projectzombie.region_rotation.file.FileBufferController;
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
        FileBufferController.init((JavaPlugin) this);
        StateController.init(this);

        this.getCommand(COMMAND_ROOT).setExecutor(new BaseStateCommands());
    }

    @Override
    public void onDisable()
    {
        StateController.instance().saveBaseStatesToDisc();
        this.getLogger().info("DynamicRegions disabled!");
    }
}
