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

import org.bukkit.plugin.java.JavaPlugin;

import de.minestar.illuminati.database.DatabaseHandler;
import de.minestar.illuminati.utils.ChatUtils;

public class Illuminati extends JavaPlugin {

    private DatabaseHandler dbHandler;

    @Override
    public void onDisable() {
        dbHandler.closeConnection();
        ChatUtils.printConsoleInfo("Disabled!");
    }

    @Override
    public void onEnable() {

        File dataFolder = getDataFolder();
        dataFolder.mkdirs();
        dbHandler = new DatabaseHandler(dataFolder);

        ChatUtils.printConsoleInfo("Version " + getDescription().getVersion() + " enabled!");
    }

}
