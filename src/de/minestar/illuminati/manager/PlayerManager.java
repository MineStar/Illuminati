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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import de.minestar.illuminati.database.DatabaseHandler;
import de.minestar.illuminati.utils.ChatUtils;

public class PlayerManager {

    // Store autogenerate key from login table update to store logout time/
    // group in same entry
    private HashMap<String, Integer> tableIDs;

    private DatabaseHandler dbHandler;

    private File dataFolder;

    public PlayerManager(DatabaseHandler dbHandler, File dataFolder) {
        this.dbHandler = dbHandler;
        this.dataFolder = dataFolder;
        loadLogins();
    }

    public void handleLogin(Player p) {

        int id = dbHandler.addLogin(p);
        if (id == -1)
            ChatUtils.printConsoleError("Can't add login information for User '" + p.getName() + "'!");
        else
            tableIDs.put(p.getName(), Integer.valueOf(id));
    }

    public void handleLogout(Player p) {

        Integer id = tableIDs.remove(p.getName());
        if (id == null)
            return;
        if (!dbHandler.addLogout(p, id))
            ChatUtils.printConsoleError("Can't add logout information for User '" + p.getName() + "'!");
    }

    /**
     * When server is stopping all player log out, but they do not fire a log
     * out event! So we write manually all information from the HashMap into the
     * database
     * 
     * @param s
     */
    public void logoutAll(Server s) {

        for (Entry<String, Integer> entry : tableIDs.entrySet()) {
            if (!dbHandler.addLogout(s.getPlayerExact(entry.getKey()), entry.getValue()))
                ChatUtils.printConsoleError("Can't add logout information for User=" + entry.getKey() + " and ID=" + entry.getValue() + "!");
        }
    }

    /**
     * When server is reloading all login information are lost. This method
     * store them in a single temponary file to restore them when loading the
     * plugin
     * 
     * @param dataFolder
     *            The data folder of the plugin
     */
    public void writeLogins() {

        File temp = new File(dataFolder, "temp.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
            for (Entry<String, Integer> entry : tableIDs.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue());
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            ChatUtils.printConsoleException(e, "Can't temponary save all logged in players!");
        }
    }

    /**
     * When server is reloading all login information are lsot. This method
     * restore them from the temponary file created when the server is reloading
     * 
     * @param dataFolder
     *            The data folder of the plugin
     */
    public void loadLogins() {

        tableIDs = new HashMap<String, Integer>();

        File temp = new File(dataFolder, "temp.txt");
        if (!temp.exists())
            return;

        try {

            BufferedReader bReader = new BufferedReader(new FileReader(temp));
            String line = "";
            while ((line = bReader.readLine()) != null) {
                String[] split = line.split(" ");
                tableIDs.put(split[0], Integer.valueOf(split[1]));
            }
            bReader.close();

            // remove file
            if (!temp.delete())
                temp.deleteOnExit();
        } catch (Exception e) {
            ChatUtils.printConsoleException(e, "Can't load temponary login infos!");
        }
    }
}
