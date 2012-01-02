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

import org.bukkit.configuration.file.YamlConfiguration;

import de.minestar.illuminati.utils.ChatUtils;

public class DatabaseHandler {

    private DatabaseConnection dbConnection;

    // Prepared Statements

    // /Prepared Statements

    public DatabaseHandler(File dataFolder) {
        try {
            init(dataFolder);
        } catch (Exception e) {
            ChatUtils.printConsoleException(e, "Can't create a database connection!");
        }
    }

    private void init(File dataFolder) throws Exception {
        createConnection(dataFolder);
        checkStructure();
        createStatements();
    }

    private void createConnection(File dataFolder) throws Exception {
        YamlConfiguration config = new YamlConfiguration();
        File configFile = new File(dataFolder, "sqlconfig.yml");
        if (!configFile.exists()) {
            configFile.createNewFile();
            ChatUtils.printConsoleError("Can't find SQL Configuration in " + configFile.toString() + "! Creating a default one! Please restart server after configured SQL connection!");
            config.load(configFile);
            config.set("host", "");
            config.set("port", "");
            config.set("database", "");
            config.set("user", "");
            config.set("password", "");
            config.save(configFile);
            return;
        }
        String[] conInfos = {config.getString("host"), config.getString("port"), config.getString("database"), config.getString("user"), config.getString("password")};
        for (String info : conInfos)
            if (info == null || info.isEmpty()) {
                ChatUtils.printConsoleError("SQL configuration is incomplete! Please complete connection information in " + configFile.toString() + " and restart server!");
            }
        dbConnection = new DatabaseConnection(conInfos[0], conInfos[1], conInfos[2], conInfos[3], conInfos[4]);
        conInfos = null;
        System.gc();
    }

    private void checkStructure() throws Exception {
        Connection con = dbConnection.getConnection();
        // @formatter:off
        con.createStatement().execute(
                "CREATE  TABLE IF NOT EXISTS `mc_illuminati`.`stats` (" +
                "  `id` INT NOT NULL AUTO_INCREMENT ," +
                "  `player` VARCHAR(45) NOT NULL ," +
                "  `loginGroup` INT NOT NULL ," +
                "  `loginTime` DATETIME NOT NULL ," +
                "  `logoutGroup` INT NOT NULL ," +
                "  `logoutTime` DATETIME NOT NULL ," +
                "  PRIMARY KEY (`id`) )" +
                "ENGINE = InnoDB;");
        // @formatter: on
    }

    private void createStatements() throws Exception {

    }

    public void closeConnection() {
        dbConnection.closeConnection();
    }

}
