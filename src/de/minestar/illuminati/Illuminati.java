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

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.minestar.illuminati.database.DatabaseHandler;
import de.minestar.illuminati.listener.PListener;
import de.minestar.illuminati.listener.ServerCommandListener;
import de.minestar.illuminati.manager.PlayerManager;
import de.minestar.illuminati.utils.ChatUtils;

public class Illuminati extends JavaPlugin {

    private DatabaseHandler dbHandler;
    private PlayerManager pManager;

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

        dbHandler = new DatabaseHandler(dataFolder);
        pManager = new PlayerManager(dbHandler, dataFolder);

        PluginManager pm = getServer().getPluginManager();

        PlayerListener pListener = new PListener(pManager);
        pm.registerEvent(Type.PLAYER_JOIN, pListener, Priority.Highest, this);
        pm.registerEvent(Type.PLAYER_QUIT, pListener, Priority.Highest, this);
        pm.registerEvent(Type.PLAYER_KICK, pListener, Priority.Highest, this);
        pm.registerEvent(Type.PLAYER_COMMAND_PREPROCESS, pListener, Priority.Highest, this);

        pm.registerEvent(Type.SERVER_COMMAND, new ServerCommandListener(pManager), Priority.Normal, this);

        ChatUtils.printConsoleInfo("Version " + getDescription().getVersion() + " enabled!");
    }
}
