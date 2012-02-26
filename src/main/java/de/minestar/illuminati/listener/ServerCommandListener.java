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

package de.minestar.illuminati.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import de.minestar.illuminati.manager.PlayerManager;

public class ServerCommandListener implements Listener {

    private PlayerManager pManager;

    public ServerCommandListener(PlayerManager pManager) {
        this.pManager = pManager;
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        if (event.getCommand().equalsIgnoreCase("stop"))
            pManager.logoutAll(event.getSender().getServer());
        else if (event.getCommand().equalsIgnoreCase("reload")) {
            pManager.writeLogins();
        }
    }
}
