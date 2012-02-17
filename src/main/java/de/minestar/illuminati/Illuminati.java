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
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import de.minestar.illuminati.database.DatabaseHandler;
import de.minestar.illuminati.listener.PListener;
import de.minestar.illuminati.listener.ServerCommandListener;
import de.minestar.illuminati.manager.PlayerManager;
import de.minestar.illuminati.utils.ChatUtils;

public class Illuminati extends JavaPlugin {

    private DatabaseHandler dbHandler;
    private PlayerManager pManager;

    private static Illuminati instance;

    public void onDisable() {
        dbHandler.closeConnection();
        dbHandler = null;
        pManager = null;
        ChatUtils.printConsoleInfo("Disabled!");
    }

    public void onEnable() {
        instance = this;

        File dataFolder = getDataFolder();
        dataFolder.mkdirs();

        // REGISTER MANAGERS
        dbHandler = new DatabaseHandler(dataFolder);
        pManager = new PlayerManager(dbHandler, dataFolder);

        // REGISTER EVENTS
        Listener pListener = new PListener(pManager);
        Bukkit.getPluginManager().registerEvents(pListener, this);
        Bukkit.getPluginManager().registerEvents(new ServerCommandListener(pManager), this);

        // PRINT INFO
        ChatUtils.printConsoleInfo("Version " + getDescription().getVersion() + " enabled!");
    }

    public static Illuminati getInstance() {
        return instance;
    }
}
