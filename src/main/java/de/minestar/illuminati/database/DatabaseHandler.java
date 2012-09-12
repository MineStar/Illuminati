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
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import de.minestar.illuminati.IlluminatiCore;
import de.minestar.minestarlibrary.database.AbstractMySQLHandler;
import de.minestar.minestarlibrary.stats.Statistic;
import de.minestar.minestarlibrary.stats.StatisticType;
import de.minestar.minestarlibrary.stats.UpdateableStatistic;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class DatabaseHandler extends AbstractMySQLHandler {

    private Set<String> tables = new HashSet<String>();

    // HANDLER FOR DIFFERENT KINDS OF STATISTICS
    private UpdateableStatisticHandler updateStatisticHandler;
    private NormalStatisticHandler normalStatisticHandler;

    public DatabaseHandler(String pluginName, File SQLConfigFile) {
        super(pluginName, SQLConfigFile);
        this.loadTableNames();
        this.normalStatisticHandler = new NormalStatisticHandler(dbConnection);
        this.updateStatisticHandler = new UpdateableStatisticHandler(dbConnection);
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
            ConsoleUtils.printException(e, IlluminatiCore.NAME, "Can't load table names from database!");
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

        // CREATE PREPARED STATEMENTS
        if (statistic instanceof UpdateableStatistic)
            updateStatisticHandler.registerStatistic((UpdateableStatistic) statistic, getTableName(statistic));
        // CREATEA A HEAD FOR THE INSERT QUERY
        else
            normalStatisticHandler.registerStatistic(statistic, getTableName(statistic));
    }

    // CHECK IF THERE IS A TABLE FOR THE STATISTIC
    private boolean isStatTableExisting(Statistic statistic) {
        return tables != null && tables.contains(getTableName(statistic).toLowerCase());
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
            for (Entry<String, StatisticType> entry : statistic.getHead().entrySet()) {
                sBuilder.append('`');
                sBuilder.append(entry.getKey());
                sBuilder.append("` ");
                sBuilder.append(entry.getValue().getName());
                sBuilder.append(" , ");
            }

            // IF STATISTIC IS UPDATEABLE ADD A KEY FOR FASTER SEARCH
            if (statistic instanceof UpdateableStatistic) {
                UpdateableStatistic uStatistic = (UpdateableStatistic) statistic;

                // ADD KEY TYPE
                sBuilder.append('`');
                sBuilder.append(uStatistic.getKeyName());
                sBuilder.append("` ");
                sBuilder.append(uStatistic.getKeyType().getName());
                sBuilder.append(", ");

                // ADD INDEX
                StatisticType type = uStatistic.getKeyType();
                // TEXT COLOMS ALWAYS NEED A KEY LENGTH - LENGTH IS 32
                if (type.equals(StatisticType.STRING))
                    sBuilder.append(" INDEX `index` (`" + uStatistic.getKeyName() + "`(32)), ");
                else
                    sBuilder.append(" INDEX `index` (`" + uStatistic.getKeyName() + "`), ");
            }

            sBuilder.append("PRIMARY KEY (`id`)) ENGINE = InnoDB;");

            // EXECUTE THE QUERY
            dbConnection.getConnection().createStatement().execute(sBuilder.toString());
            ConsoleUtils.printInfo(IlluminatiCore.NAME, "Statistic '" + statistic.getName() + "' from '" + statistic.getPluginName() + "' firstly registered!");

        } catch (Exception e) {
            ConsoleUtils.printException(e, IlluminatiCore.NAME, "Can't create table structure for statistic '" + statistic.getName() + "' from plugin '" + statistic.getPluginName() + "'!");
        }
    }

    // FORMAT: PluginName_StatisticName
    private String getTableName(Statistic statistic) {
        return statistic.getPluginName() + "_" + statistic.getName();
    }

    // ****************************************
    // ********* STORTING STATISTICS **********
    // ****************************************

    public void storeNormalStatistics(List<Statistic> stats) {
        normalStatisticHandler.storeStatistics(stats);
    }

    public void storeUpdateableStatistics(List<UpdateableStatistic> stats) {
        updateStatisticHandler.storeStatistics(stats);
    }
}
