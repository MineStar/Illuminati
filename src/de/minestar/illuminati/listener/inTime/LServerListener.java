/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of Illuminati.
 * 
 * Illuminati is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * Illuminati is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Illuminati.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.illuminati.listener.inTime;

import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.server.ServerListener;

import de.minestar.illuminati.manager.TimeManager;

public class LServerListener extends ServerListener {

    private TimeManager tManager;

    public LServerListener(TimeManager tManager) {
        this.tManager = tManager;
    }

    @Override
    public void onMapInitialize(MapInitializeEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPluginDisable(PluginDisableEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPluginEnable(PluginEnableEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onServerCommand(ServerCommandEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onServerListPing(ServerListPingEvent event) {
        tManager.EventHasEnded(event);
    }
}
