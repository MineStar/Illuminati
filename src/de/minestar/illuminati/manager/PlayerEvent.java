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

package de.minestar.illuminati.manager;

import org.bukkit.entity.Player;

import de.minestar.illuminati.database.DatabaseHandler;
import de.minestar.illuminati.utils.ChatUtils;

public class PlayerEvent implements Runnable {
    private boolean isLogin = true;
    private Player player = null;
    private String playerName = "";
    private DatabaseHandler dbHandler;

    public PlayerEvent(boolean isLogin, Player player, DatabaseHandler dbHandler) {
        this.isLogin = isLogin;
        this.player = player;
        this.playerName = player.getName();
        this.dbHandler = dbHandler;
    }

    public void run() {
        if (this.isLogin) {
            int ID = dbHandler.addLogin(player);
            if (ID == -1)
                ChatUtils.printConsoleError("Can't add login information for User '" + player.getName() + "'!");
            else {
                PlayerManager.setID(player, ID);
            }

        } else {
            Integer ID = PlayerManager.removeID(playerName);

            if (ID == -1)
                return;
            if (!dbHandler.addLogout(playerName, ID))
                ChatUtils.printConsoleError("Can't add logout information for User '" + player.getName() + "'!");
        }
    }

}
