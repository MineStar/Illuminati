/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of 'Illuminati'.
 * 
 * 'Illuminati' is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * 'Illuminati' is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with 'Illuminati'.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package de.minestar.illuminati;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.inventory.InventoryListener;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.server.ServerListener;
import org.bukkit.event.vehicle.VehicleListener;
import org.bukkit.event.weather.WeatherListener;
import org.bukkit.event.world.WorldListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.bukkit.gemo.commands.CommandList;

import de.minestar.illuminati.commands.*;
import de.minestar.illuminati.database.DatabaseHandler;
import de.minestar.illuminati.listener.PListener;
import de.minestar.illuminati.listener.ServerCommandListener;
import de.minestar.illuminati.listener.inTime.FBlockListener;
import de.minestar.illuminati.listener.inTime.FEntityListener;
import de.minestar.illuminati.listener.inTime.FInventoryListener;
import de.minestar.illuminati.listener.inTime.FPlayerListener;
import de.minestar.illuminati.listener.inTime.FServerListener;
import de.minestar.illuminati.listener.inTime.FVehicleListener;
import de.minestar.illuminati.listener.inTime.FWeatherListener;
import de.minestar.illuminati.listener.inTime.FWorldListener;
import de.minestar.illuminati.listener.inTime.LBlockListener;
import de.minestar.illuminati.listener.inTime.LEntityListener;
import de.minestar.illuminati.listener.inTime.LInventoryListener;
import de.minestar.illuminati.listener.inTime.LPlayerListener;
import de.minestar.illuminati.listener.inTime.LServerListener;
import de.minestar.illuminati.listener.inTime.LVehicleListener;
import de.minestar.illuminati.listener.inTime.LWeatherListener;
import de.minestar.illuminati.listener.inTime.LWorldListener;
import de.minestar.illuminati.manager.PlayerManager;
import de.minestar.illuminati.manager.TickManager;
import de.minestar.illuminati.manager.TimeManager;
import de.minestar.illuminati.utils.ChatUtils;

public class Illuminati extends JavaPlugin {

    private DatabaseHandler dbHandler;
    private PlayerManager pManager;
    private CommandList commandList;

    private static TimeManager timeManager; 
    private static TickManager tickManager;
    @Override
    public void onDisable() {
        dbHandler.closeConnection();
        dbHandler = null;
        pManager = null;
        ChatUtils.printConsoleInfo("Disabled!");
    }

    @Override
    public void onEnable() {

        File dataFolder = getDataFolder();
        dataFolder.mkdirs();

        // INIT COMMANDS
        this.initCommands();

        // REGISTER MANAGERS
        timeManager = new TimeManager();
        dbHandler = new DatabaseHandler(dataFolder);
        pManager = new PlayerManager(dbHandler, dataFolder);

        // REGISTER EVENTS
        PluginManager pm = getServer().getPluginManager();
        PlayerListener pListener = new PListener(pManager);
        pm.registerEvent(Type.PLAYER_JOIN, pListener, Priority.Highest, this);
        pm.registerEvent(Type.PLAYER_QUIT, pListener, Priority.Highest, this);
        pm.registerEvent(Type.PLAYER_KICK, pListener, Priority.Highest, this);
        pm.registerEvent(Type.PLAYER_COMMAND_PREPROCESS, pListener, Priority.Highest, this);
        pm.registerEvent(Type.SERVER_COMMAND, new ServerCommandListener(pManager), Priority.Normal, this);

        // register all events for plugin overwatch
        registerEvents(pm);

        // CREATE TICK-TASK
        tickManager = new TickManager();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, tickManager, 20, 1);

        // PRINT INFO
        ChatUtils.printConsoleInfo("Version " + getDescription().getVersion() + " enabled!");
    }

    public static TimeManager getTimeManager() {
        return timeManager;
    }
    
    public static TickManager getTickManager() {
        return tickManager;
    }

    private void registerEvents(PluginManager pm) {
        // First Block Listener
        BlockListener bListener = new FBlockListener(timeManager);
        pm.registerEvent(Type.BLOCK_BREAK, bListener, Priority.Lowest, this);
        pm.registerEvent(Type.BLOCK_BURN, bListener, Priority.Lowest, this);
        pm.registerEvent(Type.BLOCK_CANBUILD, bListener, Priority.Lowest, this);
        pm.registerEvent(Type.BLOCK_DAMAGE, bListener, Priority.Lowest, this);
        pm.registerEvent(Type.BLOCK_DISPENSE, bListener, Priority.Lowest, this);
        pm.registerEvent(Type.BLOCK_FADE, bListener, Priority.Lowest, this);
        pm.registerEvent(Type.BLOCK_FORM, bListener, Priority.Lowest, this);
        pm.registerEvent(Type.BLOCK_FROMTO, bListener, Priority.Lowest, this);
        pm.registerEvent(Type.BLOCK_IGNITE, bListener, Priority.Lowest, this);
        pm.registerEvent(Type.BLOCK_PHYSICS, bListener, Priority.Lowest, this);
        pm.registerEvent(Type.BLOCK_PISTON_EXTEND, bListener, Priority.Lowest, this);
        pm.registerEvent(Type.BLOCK_PISTON_RETRACT, bListener, Priority.Lowest, this);
        pm.registerEvent(Type.BLOCK_PLACE, bListener, Priority.Lowest, this);
        pm.registerEvent(Type.BLOCK_SPREAD, bListener, Priority.Lowest, this);
        pm.registerEvent(Type.REDSTONE_CHANGE, bListener, Priority.Lowest, this);
        pm.registerEvent(Type.LEAVES_DECAY, bListener, Priority.Lowest, this);
        pm.registerEvent(Type.SIGN_CHANGE, bListener, Priority.Lowest, this);

        // First Entity Listener
        EntityListener eListener = new FEntityListener(timeManager);
        pm.registerEvent(Type.CREATURE_SPAWN, eListener, Priority.Lowest, this);
        pm.registerEvent(Type.CREEPER_POWER, eListener, Priority.Lowest, this);
        pm.registerEvent(Type.ENDERMAN_PICKUP, eListener, Priority.Lowest, this);
        pm.registerEvent(Type.ENDERMAN_PLACE, eListener, Priority.Lowest, this);
        pm.registerEvent(Type.ENTITY_COMBUST, eListener, Priority.Lowest, this);
        pm.registerEvent(Type.ENTITY_DAMAGE, eListener, Priority.Lowest, this);
        pm.registerEvent(Type.ENTITY_DEATH, eListener, Priority.Lowest, this);
        pm.registerEvent(Type.ENTITY_EXPLODE, eListener, Priority.Lowest, this);
        pm.registerEvent(Type.ENTITY_INTERACT, eListener, Priority.Lowest, this);
        pm.registerEvent(Type.ENTITY_PORTAL_ENTER, eListener, Priority.Lowest, this);
        pm.registerEvent(Type.ENTITY_REGAIN_HEALTH, eListener, Priority.Lowest, this);
        pm.registerEvent(Type.ENTITY_TAME, eListener, Priority.Lowest, this);
        pm.registerEvent(Type.ENTITY_TARGET, eListener, Priority.Lowest, this);
        pm.registerEvent(Type.EXPLOSION_PRIME, eListener, Priority.Lowest, this);
        pm.registerEvent(Type.FOOD_LEVEL_CHANGE, eListener, Priority.Lowest, this);
        pm.registerEvent(Type.ITEM_SPAWN, eListener, Priority.Lowest, this);
        pm.registerEvent(Type.PAINTING_BREAK, eListener, Priority.Lowest, this);
        pm.registerEvent(Type.PAINTING_PLACE, eListener, Priority.Lowest, this);
        pm.registerEvent(Type.PIG_ZAP, eListener, Priority.Lowest, this);
        pm.registerEvent(Type.PROJECTILE_HIT, eListener, Priority.Lowest, this);
        pm.registerEvent(Type.SLIME_SPLIT, eListener, Priority.Lowest, this);

        // First Inventory Listener
        InventoryListener iListener = new FInventoryListener(timeManager);
        pm.registerEvent(Type.FURNACE_BURN, iListener, Priority.Lowest, this);
        pm.registerEvent(Type.FURNACE_SMELT, iListener, Priority.Lowest, this);

        // First Player Listener
        PlayerListener pListener = new FPlayerListener(timeManager);
        pm.registerEvent(Type.PLAYER_ITEM_HELD, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_ANIMATION, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_BED_ENTER, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_BED_LEAVE, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_BUCKET_EMPTY, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_BUCKET_FILL, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_CHANGED_WORLD, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_CHAT, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_COMMAND_PREPROCESS, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_DROP_ITEM, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_EGG_THROW, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_FISH, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_GAME_MODE_CHANGE, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_INTERACT, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_INTERACT_ENTITY, pListener, Priority.Lowest, this);
//        pm.registerEvent(Type.PLAYER_INVENTORY, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.INVENTORY_OPEN, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_JOIN, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_KICK, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_LOGIN, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_MOVE, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_PICKUP_ITEM, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_PORTAL, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_PRELOGIN, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_QUIT, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_RESPAWN, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_TELEPORT, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_TOGGLE_SNEAK, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_TOGGLE_SPRINT, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_VELOCITY, pListener, Priority.Lowest, this);

        // First Server Listener
        ServerListener sListener = new FServerListener(timeManager);
        pm.registerEvent(Type.MAP_INITIALIZE, sListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLUGIN_DISABLE, sListener, Priority.Lowest, this);
        pm.registerEvent(Type.PLUGIN_ENABLE, sListener, Priority.Lowest, this);
        pm.registerEvent(Type.SERVER_COMMAND, sListener, Priority.Lowest, this);
        pm.registerEvent(Type.SERVER_LIST_PING, sListener, Priority.Lowest, this);

        // First Vehicle Listener
        VehicleListener vListener = new FVehicleListener(timeManager);
        pm.registerEvent(Type.VEHICLE_COLLISION_BLOCK, vListener, Priority.Lowest, this);
        pm.registerEvent(Type.VEHICLE_COLLISION_ENTITY, vListener, Priority.Lowest, this);
        pm.registerEvent(Type.VEHICLE_CREATE, vListener, Priority.Lowest, this);
        pm.registerEvent(Type.VEHICLE_DAMAGE, vListener, Priority.Lowest, this);
        pm.registerEvent(Type.VEHICLE_DESTROY, vListener, Priority.Lowest, this);
        pm.registerEvent(Type.VEHICLE_ENTER, vListener, Priority.Lowest, this);
        pm.registerEvent(Type.VEHICLE_EXIT, vListener, Priority.Lowest, this);
        pm.registerEvent(Type.VEHICLE_MOVE, vListener, Priority.Lowest, this);
        pm.registerEvent(Type.VEHICLE_UPDATE, vListener, Priority.Lowest, this);

        // First Weather Listener
        WeatherListener wListener = new FWeatherListener(timeManager);
        pm.registerEvent(Type.WEATHER_CHANGE, wListener, Priority.Lowest, this);
        pm.registerEvent(Type.LIGHTNING_STRIKE, wListener, Priority.Lowest, this);
        pm.registerEvent(Type.THUNDER_CHANGE, wListener, Priority.Lowest, this);

        // First World Listener
        WorldListener woListener = new FWorldListener(timeManager);
        pm.registerEvent(Type.CHUNK_LOAD, woListener, Priority.Lowest, this);
        pm.registerEvent(Type.CHUNK_POPULATED, woListener, Priority.Lowest, this);
        pm.registerEvent(Type.CHUNK_UNLOAD, woListener, Priority.Lowest, this);
        pm.registerEvent(Type.PORTAL_CREATE, woListener, Priority.Lowest, this);
        pm.registerEvent(Type.SPAWN_CHANGE, woListener, Priority.Lowest, this);
        pm.registerEvent(Type.STRUCTURE_GROW, woListener, Priority.Lowest, this);
        pm.registerEvent(Type.WORLD_INIT, woListener, Priority.Lowest, this);
        pm.registerEvent(Type.WORLD_LOAD, woListener, Priority.Lowest, this);
        pm.registerEvent(Type.WORLD_SAVE, woListener, Priority.Lowest, this);
        pm.registerEvent(Type.WORLD_UNLOAD, woListener, Priority.Lowest, this);

        // Initiate Second Type of Listener - The Last Listener

        // Last Block Listener
        bListener = new LBlockListener(timeManager);
        pm.registerEvent(Type.BLOCK_BREAK, bListener, Priority.Monitor, this);
        pm.registerEvent(Type.BLOCK_BURN, bListener, Priority.Monitor, this);
        pm.registerEvent(Type.BLOCK_CANBUILD, bListener, Priority.Monitor, this);
        pm.registerEvent(Type.BLOCK_DAMAGE, bListener, Priority.Monitor, this);
        pm.registerEvent(Type.BLOCK_DISPENSE, bListener, Priority.Monitor, this);
        pm.registerEvent(Type.BLOCK_FADE, bListener, Priority.Monitor, this);
        pm.registerEvent(Type.BLOCK_FORM, bListener, Priority.Monitor, this);
        pm.registerEvent(Type.BLOCK_FROMTO, bListener, Priority.Monitor, this);
        pm.registerEvent(Type.BLOCK_IGNITE, bListener, Priority.Monitor, this);
        pm.registerEvent(Type.BLOCK_PHYSICS, bListener, Priority.Monitor, this);
        pm.registerEvent(Type.BLOCK_PISTON_EXTEND, bListener, Priority.Monitor, this);
        pm.registerEvent(Type.BLOCK_PISTON_RETRACT, bListener, Priority.Monitor, this);
        pm.registerEvent(Type.BLOCK_PLACE, bListener, Priority.Monitor, this);
        pm.registerEvent(Type.BLOCK_SPREAD, bListener, Priority.Monitor, this);
        pm.registerEvent(Type.REDSTONE_CHANGE, bListener, Priority.Monitor, this);
        pm.registerEvent(Type.LEAVES_DECAY, bListener, Priority.Monitor, this);
        pm.registerEvent(Type.SIGN_CHANGE, bListener, Priority.Monitor, this);

        // Last Entity Listener
        eListener = new LEntityListener(timeManager);
        pm.registerEvent(Type.CREATURE_SPAWN, eListener, Priority.Monitor, this);
        pm.registerEvent(Type.CREEPER_POWER, eListener, Priority.Monitor, this);
        pm.registerEvent(Type.ENDERMAN_PICKUP, eListener, Priority.Monitor, this);
        pm.registerEvent(Type.ENDERMAN_PLACE, eListener, Priority.Monitor, this);
        pm.registerEvent(Type.ENTITY_COMBUST, eListener, Priority.Monitor, this);
        pm.registerEvent(Type.ENTITY_DAMAGE, eListener, Priority.Monitor, this);
        pm.registerEvent(Type.ENTITY_DEATH, eListener, Priority.Monitor, this);
        pm.registerEvent(Type.ENTITY_EXPLODE, eListener, Priority.Monitor, this);
        pm.registerEvent(Type.ENTITY_INTERACT, eListener, Priority.Monitor, this);
        pm.registerEvent(Type.ENTITY_PORTAL_ENTER, eListener, Priority.Monitor, this);
        pm.registerEvent(Type.ENTITY_REGAIN_HEALTH, eListener, Priority.Monitor, this);
        pm.registerEvent(Type.ENTITY_TAME, eListener, Priority.Monitor, this);
        pm.registerEvent(Type.ENTITY_TARGET, eListener, Priority.Monitor, this);
        pm.registerEvent(Type.EXPLOSION_PRIME, eListener, Priority.Monitor, this);
        pm.registerEvent(Type.FOOD_LEVEL_CHANGE, eListener, Priority.Monitor, this);
        pm.registerEvent(Type.ITEM_SPAWN, eListener, Priority.Monitor, this);
        pm.registerEvent(Type.PAINTING_BREAK, eListener, Priority.Monitor, this);
        pm.registerEvent(Type.PAINTING_PLACE, eListener, Priority.Monitor, this);
        pm.registerEvent(Type.PIG_ZAP, eListener, Priority.Monitor, this);
        pm.registerEvent(Type.PROJECTILE_HIT, eListener, Priority.Monitor, this);
        pm.registerEvent(Type.SLIME_SPLIT, eListener, Priority.Monitor, this);

        // Last Inventory Listener
        iListener = new LInventoryListener(timeManager);
        pm.registerEvent(Type.FURNACE_BURN, iListener, Priority.Monitor, this);
        pm.registerEvent(Type.FURNACE_SMELT, iListener, Priority.Monitor, this);

        // Last Player Listener
        pListener = new LPlayerListener(timeManager);
        pm.registerEvent(Type.PLAYER_ITEM_HELD, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_ANIMATION, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_BED_ENTER, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_BED_LEAVE, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_BUCKET_EMPTY, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_BUCKET_FILL, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_CHANGED_WORLD, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_CHAT, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_COMMAND_PREPROCESS, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_DROP_ITEM, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_EGG_THROW, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_FISH, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_GAME_MODE_CHANGE, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_INTERACT, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_INTERACT_ENTITY, pListener, Priority.Monitor, this);
//        pm.registerEvent(Type.PLAYER_INVENTORY, pListener, Priority.Lowest, this);
        pm.registerEvent(Type.INVENTORY_OPEN, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_JOIN, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_KICK, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_LOGIN, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_MOVE, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_PICKUP_ITEM, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_PORTAL, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_PRELOGIN, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_QUIT, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_RESPAWN, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_TELEPORT, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_TOGGLE_SNEAK, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_TOGGLE_SPRINT, pListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_VELOCITY, pListener, Priority.Monitor, this);

        // Last Server Listener
        sListener = new LServerListener(timeManager);
        pm.registerEvent(Type.MAP_INITIALIZE, sListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLUGIN_DISABLE, sListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLUGIN_ENABLE, sListener, Priority.Monitor, this);
        pm.registerEvent(Type.SERVER_COMMAND, sListener, Priority.Monitor, this);
        pm.registerEvent(Type.SERVER_LIST_PING, sListener, Priority.Monitor, this);

        // Last Vehicle Listener
        vListener = new LVehicleListener(timeManager);
        pm.registerEvent(Type.VEHICLE_COLLISION_BLOCK, vListener, Priority.Monitor, this);
        pm.registerEvent(Type.VEHICLE_COLLISION_ENTITY, vListener, Priority.Monitor, this);
        pm.registerEvent(Type.VEHICLE_CREATE, vListener, Priority.Monitor, this);
        pm.registerEvent(Type.VEHICLE_DAMAGE, vListener, Priority.Monitor, this);
        pm.registerEvent(Type.VEHICLE_DESTROY, vListener, Priority.Monitor, this);
        pm.registerEvent(Type.VEHICLE_ENTER, vListener, Priority.Monitor, this);
        pm.registerEvent(Type.VEHICLE_EXIT, vListener, Priority.Monitor, this);
        pm.registerEvent(Type.VEHICLE_MOVE, vListener, Priority.Monitor, this);
        pm.registerEvent(Type.VEHICLE_UPDATE, vListener, Priority.Monitor, this);

        // Last Weather Listener
        wListener = new LWeatherListener(timeManager);
        pm.registerEvent(Type.WEATHER_CHANGE, wListener, Priority.Monitor, this);
        pm.registerEvent(Type.LIGHTNING_STRIKE, wListener, Priority.Monitor, this);
        pm.registerEvent(Type.THUNDER_CHANGE, wListener, Priority.Monitor, this);

        // Last World Listener
        woListener = new LWorldListener(timeManager);
        pm.registerEvent(Type.CHUNK_LOAD, woListener, Priority.Monitor, this);
        pm.registerEvent(Type.CHUNK_POPULATED, woListener, Priority.Monitor, this);
        pm.registerEvent(Type.CHUNK_UNLOAD, woListener, Priority.Monitor, this);
        pm.registerEvent(Type.PORTAL_CREATE, woListener, Priority.Monitor, this);
        pm.registerEvent(Type.SPAWN_CHANGE, woListener, Priority.Monitor, this);
        pm.registerEvent(Type.STRUCTURE_GROW, woListener, Priority.Monitor, this);
        pm.registerEvent(Type.WORLD_INIT, woListener, Priority.Monitor, this);
        pm.registerEvent(Type.WORLD_LOAD, woListener, Priority.Monitor, this);
        pm.registerEvent(Type.WORLD_SAVE, woListener, Priority.Monitor, this);
        pm.registerEvent(Type.WORLD_UNLOAD, woListener, Priority.Monitor, this);

        // Achievement gained
    }

    private void initCommands() {
        /* @formatter:off */
        // Add an command to this list to register it in the plugin       
        com.bukkit.gemo.commands.Command[] commands = new com.bukkit.gemo.commands.Command[] {
                new cmdIllu("[Illuminati]", "/illu", "", "illuminati.use", false, new com.bukkit.gemo.commands.Command[] {
                        new cmdShowInfo("[Illuminati]", "show", "<EventName> [ns/ms/s]", "illuminati.use")
                })
        };
        /* @formatter:on */
        // store the commands in the hash map
        commandList = new CommandList("[FB-Chat]", commands);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        commandList.handleCommand(sender, label, args);
        return true;
    }
}
