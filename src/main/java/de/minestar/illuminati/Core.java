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

import org.bukkit.plugin.PluginManager;

import de.minestar.illuminati.database.DatabaseHandler;
import de.minestar.illuminati.listener.PListener;
import de.minestar.illuminati.listener.ServerCommandListener;
import de.minestar.illuminati.manager.PlayerManager;
import de.minestar.minestarlibrary.AbstractCore;

public class Core extends AbstractCore {

    private DatabaseHandler dbHandler;
    private PlayerManager pManager;

    private static Core instance;

    public Core() {
        super("Illuminati");
    }

    @Override
    protected boolean createManager() {
        File dataFolder = getDataFolder();
        dataFolder.mkdirs();

        dbHandler = new DatabaseHandler(NAME, dataFolder);
        pManager = new PlayerManager(dbHandler, dataFolder);
        return true;
    }

    @Override
    protected boolean registerEvents(PluginManager pm) {
        pm.registerEvents(new PListener(pManager), this);
        pm.registerEvents(new ServerCommandListener(pManager), this);
        return true;
    }

    @Override
    protected boolean commonEnable() {
        instance = this;
        return true;
    }

    @Override
    protected boolean commonDisable() {
        dbHandler.closeConnection();
        dbHandler = null;
        pManager = null;
        return true;
    }

    public static Core getInstance() {
        return instance;
    }
}
