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

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.minestar.illuminati.manager.PlayerManager;

public class PListener implements Listener {

    private PlayerManager pManager;

    public PListener(PlayerManager pManager) {
        this.pManager = pManager;
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        addLogout(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        addLogout(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        pManager.handleLogin(event.getPlayer());
    }

    private void addLogout(Player p) {
        pManager.handleLogout(p);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().isOp()) {
            if (event.getMessage().toLowerCase().startsWith("/stop"))
                pManager.logoutAll(event.getPlayer().getServer());
            else if (event.getMessage().toLowerCase().startsWith("/reload"))
                pManager.writeLogins();
        }
    }
}
