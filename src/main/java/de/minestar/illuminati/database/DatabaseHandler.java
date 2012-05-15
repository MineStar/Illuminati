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
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.minestar.illuminati.Core;
import de.minestar.minestarlibrary.config.MinestarConfig;
import de.minestar.minestarlibrary.database.AbstractDatabaseHandler;
import de.minestar.minestarlibrary.database.DatabaseConnection;
import de.minestar.minestarlibrary.database.DatabaseType;
import de.minestar.minestarlibrary.database.DatabaseUtils;
import de.minestar.minestarlibrary.stats.Statistic;
import de.minestar.minestarlibrary.stats.StatisticType;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class DatabaseHandler extends AbstractDatabaseHandler {

    private Set<String> tables = new HashSet<String>();
    private Map<Class<? extends Statistic>, String> insertHeads = new HashMap<Class<? extends Statistic>, String>();

    public DatabaseHandler(String pluginName, File dataFolder) {
        super(pluginName, dataFolder);
        this.loadTableNames();
    }

    @Override
    protected DatabaseConnection createConnection(String pluginName, File dataFolder) throws Exception {
        File configFile = new File(dataFolder, "sqlconfig.yml");
        if (!configFile.exists())
            DatabaseUtils.createDatabaseConfig(DatabaseType.MySQL, configFile, pluginName);
        else
            return new DatabaseConnection(pluginName, DatabaseType.MySQL, new MinestarConfig(configFile));

        return null;
    }

    @Override
    protected void createStructure(String pluginName, Connection con) throws Exception {
        // WE NEED TO CREATE THE STRUCTURE AFTER ALL PLUGINS LOADED
        // THEN WE KNOW WHICH TABLES WE HAVE TO CREATE
    }

    @Override
    protected void createStatements(String pluginName, Connection con) throws Exception {
        // WE HAVE TO GENERATE THE STATMENTS DYNAMICY
    }

    // LOAD ALL TABLE NAMES FROM THE DATABASE. USED FOR isStatTableExisting
    private void loadTableNames() {
        try {
            // GET ALL TABLES
            ResultSet rs = dbConnection.getConnection().createStatement().executeQuery("SHOW TABLES");
            while (rs.next())
                tables.add(rs.getString(1));
            if (tables.isEmpty())
                tables = null;
        } catch (Exception e) {
            ConsoleUtils.printException(e, Core.NAME, "Can't load table names from database!");
        }
    }

    // ****************************************
    // ******** REGISTERING STATISTICS ********
    // ****************************************

    // REGISTER THE STATISTICS
    public void registerStatistic(Statistic statistic) {
        // DO WE HAVE TO CREATE A TABLE FOR IT?
        if (!isStatTableExisting(statistic))
            createStatTable(statistic);
        // CREATEA A HEAD FOR THE INSERT QUERY
        createQueryHead(statistic);
    }

    // CHECK IF THERE IS A TABLE FOR THE STATISTIC
    public boolean isStatTableExisting(Statistic statistic) {
        return tables != null && tables.contains(getTableName(statistic));
    }

    // GENERATE A TABLE WHICH STORES THE STATISTICS DATA
    public void createStatTable(Statistic statistic) {
        try {

            String tableName = getTableName(statistic);

            // START BUILDING SQL QUERY
            StringBuilder sBuilder = new StringBuilder("CREATE TABLE `");
            sBuilder.append(tableName);
            // ALWAYS HAVE AN ID
            sBuilder.append("` ( `id` INT NOT NULL AUTO_INCREMENT , ");
            // ITERATE THROUGH THE ATTRIBUTES
            for (Entry<String, StatisticType> entry : statistic.getHead()) {
                sBuilder.append('`');
                sBuilder.append(entry.getKey());
                sBuilder.append("` ");
                sBuilder.append(entry.getValue().getName());
                sBuilder.append(" , ");
            }
            sBuilder.append("PRIMARY KEY (`id`) ) ENGINE = InnoDB;");

            // EXECUTE THE QUERY
            dbConnection.getConnection().createStatement().execute(sBuilder.toString());
            ConsoleUtils.printInfo(Core.NAME, "Statistic '" + statistic.getName() + "' from '" + statistic.getPluginName() + "' firstly registered!");

        } catch (Exception e) {
            ConsoleUtils.printException(e, Core.NAME, "Can't create table structure for statistic '" + statistic.getName() + "' from plugin '" + statistic.getPluginName() + "'!");
        }
    }

    // CREATE THE HEAD OF THE INSERT INTO QUERY
    public void createQueryHead(Statistic statistic) {
        String tableName = getTableName(statistic);
        // CREATE THE HEAD OF THE INSERT INTO QUERY
        StringBuilder sBuilder = new StringBuilder("INSERT INTO `");
        sBuilder.append(tableName);
        sBuilder.append("`(");

        // ADD ATTRIBUTES NAME
        for (Entry<String, StatisticType> entry : statistic.getHead()) {
            sBuilder.append('`');
            sBuilder.append(entry.getKey());
            sBuilder.append("`,");
        }

        // REPLACE LAST ',' WITH )
        sBuilder.setCharAt(sBuilder.length() - 1, ')');
        sBuilder.append(" VALUES ");

        // SAVE THEM
        insertHeads.put(statistic.getClass(), sBuilder.toString());
    }

    // FORMAT: PluginName_StatisticName
    private String getTableName(Statistic statistic) {
        return statistic.getPluginName() + "_" + statistic.getName();
    }
}
