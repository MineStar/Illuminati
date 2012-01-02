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

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.minestar.illuminati.database.DatabaseHandler;
import de.minestar.illuminati.utils.ChatUtils;

public class JoinQuitListener extends PlayerListener {

    // Store autogenerate key from login table update to store logout time/
    // group in same entry
    private HashMap<String, Integer> tableIDs = new HashMap<String, Integer>();

    private DatabaseHandler dbHandler;

    public JoinQuitListener(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    @Override
    public void onPlayerKick(PlayerKickEvent event) {
        addLogout(event.getPlayer());
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        addLogout(event.getPlayer());
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player p = event.getPlayer();
        int id = dbHandler.addLogin(p);
        if (id == -1)
            ChatUtils.printConsoleError("Can't add login information for User '" + p.getName() + "'!");
        else
            tableIDs.put(p.getName(), Integer.valueOf(id));
    }

    private void addLogout(Player player) {
        Integer id = tableIDs.remove(player.getName());
        if (id == null)
            return;
        if (!dbHandler.addLogout(player, id))
            ChatUtils.printConsoleError("Can't add logout information for User '" + player.getName() + "'!");
    }
}
