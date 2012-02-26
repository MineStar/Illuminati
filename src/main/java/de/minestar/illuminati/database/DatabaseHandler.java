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

package de.minestar.illuminati.database;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.UtilPermissions;

import de.minestar.illuminati.Core;
import de.minestar.illuminati.data.Group;
import de.minestar.minestarlibrary.database.AbstractDatabaseHandler;
import de.minestar.minestarlibrary.database.DatabaseConnection;
import de.minestar.minestarlibrary.database.DatabaseType;
import de.minestar.minestarlibrary.database.DatabaseUtils;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class DatabaseHandler extends AbstractDatabaseHandler {

    // Prepared Statements
    private PreparedStatement addLogin;
    private PreparedStatement addLogout;
    // /Prepared Statements

    public DatabaseHandler(String pluginName, File dataFolder) {
        super(pluginName, dataFolder);
    }

    @Override
    protected DatabaseConnection createConnection(String pluginName, File dataFolder) throws Exception {
        File configFile = new File(dataFolder, "sqlconfig.yml");
        if (!configFile.exists())
            DatabaseUtils.createDatabaseConfig(DatabaseType.MySQL, configFile, pluginName);
        else {
            YamlConfiguration config = new YamlConfiguration();
            config.load(configFile);
            return new DatabaseConnection(pluginName, DatabaseType.MySQL, config);
        }
        return null;
    }

    @Override
    protected void createStructure(String pluginName, Connection con) throws Exception {
        DatabaseUtils.createStructure(getClass().getResourceAsStream("/structure.sql"), con, pluginName);
    }

    @Override
    protected void createStatements(String pluginName, Connection con) throws Exception {
        addLogin = con.prepareStatement("INSERT INTO stats (player,loginGroup,loginTime) VALUES(?,?,NOW())", Statement.RETURN_GENERATED_KEYS);
        addLogout = con.prepareStatement("UPDATE stats SET logoutGroup = ? , logoutTime = NOW() WHERE id = ?");
    }

    public int addLogin(Player player) {
        Group g = null;
        try {
            g = Group.getGroup(UtilPermissions.getGroupName(player));
            addLogin.setString(1, player.getName());
            addLogin.setInt(2, g.ordinal());
            addLogin.executeUpdate();
            ResultSet rs = addLogin.getGeneratedKeys();
            if (!rs.next())
                return -1;
            return rs.getInt(1);
        } catch (Exception e) {
            ConsoleUtils.printException(e, Core.NAME, "Can't add a login entry! PlayerName=" + player.getName() + ", Group=" + g.getName() + ", GroupID=" + g.ordinal());
        }
        return -1;
    }

    public boolean addLogout(String player, int id) {
        Group g = null;
        try {
            g = Group.getGroup(UtilPermissions.getGroupName(player, "world"));
            addLogout.setInt(1, g.ordinal());
            addLogout.setInt(2, id);
            return addLogout.executeUpdate() == 1;
        } catch (Exception e) {
            ConsoleUtils.printException(e, Core.NAME, "Can't update logout entry! ID=" + id + ", Group=" + g.getName() + ", GroupID=" + g.ordinal());
        }
        return false;
    }
}
