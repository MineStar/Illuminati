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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;

import de.minestar.illuminati.IlluminatiCore;
import de.minestar.minestarlibrary.database.AbstractMySQLHandler;
import de.minestar.minestarlibrary.stats.Statistic;
import de.minestar.minestarlibrary.stats.StatisticType;
import de.minestar.minestarlibrary.stats.UpdateableStatistic;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class DatabaseHandler extends AbstractMySQLHandler {

    private Set<String> tables = new HashSet<String>();

    public DatabaseHandler(String pluginName, File SQLConfigFile) {
        super(pluginName, SQLConfigFile);
        this.loadTableNames();
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

        // DATA.SIZE != HEAD.SIZE
        if (!hasEnoughValuesDefined(statistic)) {
            ConsoleUtils.printError(pluginName, "The count of values is not the same as the count of coloms for the statistic '" + statistic + "'!");
            return;
        }

        // DO WE HAVE TO CREATE A TABLE FOR IT?
        if (!isStatTableExisting(statistic))
            createStatTable(statistic);

        // CREATE PREPARED STATEMENTS
        if (statistic instanceof UpdateableStatistic)
            createUpdatebleQueries((UpdateableStatistic) statistic);
        // CREATEA A HEAD FOR THE INSERT QUERY
        else
            createQueryHead(statistic);
    }

    private boolean hasEnoughValuesDefined(Statistic statistic) {
        return statistic.getData().size() == statistic.getHead().size();
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
                sBuilder.append('`');
                sBuilder.append(uStatistic.getKeyName());
                sBuilder.append("` ");

                sBuilder.append(", ");
                sBuilder.append(uStatistic.getKeyType().getName());

                // ADD INDEX
                StatisticType type = uStatistic.getKeyType();
                // TEXT COLOMS ALWAYS NEED A KEY LENGTH - LENGTH IS 32
                if (type.equals(StatisticType.STRING))
                    sBuilder.append("INDEX `index` (`" + uStatistic.getKeyName() + "`(32)), ");
                else
                    sBuilder.append("INDEX `index` (`" + uStatistic.getKeyName() + "`), ");
            }

            sBuilder.append("PRIMARY KEY (`id`)) ENGINE = InnoDB;");

            // EXECUTE THE QUERY
            dbConnection.getConnection().createStatement().execute(sBuilder.toString());
            ConsoleUtils.printInfo(IlluminatiCore.NAME, "Statistic '" + statistic.getName() + "' from '" + statistic.getPluginName() + "' firstly registered!");

        } catch (Exception e) {
            ConsoleUtils.printException(e, IlluminatiCore.NAME, "Can't create table structure for statistic '" + statistic.getName() + "' from plugin '" + statistic.getPluginName() + "'!");
        }
    }

    // MAP FOR NORMAL QUERIES
    private Map<Class<? extends Statistic>, String> insertHeads = new HashMap<Class<? extends Statistic>, String>();

    // CREATE THE HEAD OF THE INSERT INTO QUERY
    public void createQueryHead(Statistic statistic) {
        String tableName = getTableName(statistic);
        // CREATE THE HEAD OF THE INSERT INTO QUERY
        StringBuilder sBuilder = new StringBuilder("INSERT INTO `");
        sBuilder.append(tableName);
        sBuilder.append("`(");

        // ADD ATTRIBUTES NAME
        for (Entry<String, StatisticType> entry : statistic.getHead().entrySet()) {
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

    // MAPS TO STORE PREPARED STATEMENTS OF UPDATEABLE STATISTIC

    // CONTAINS
    // "UPDATE <STATISTICTABLE> SET `x` = (`x` + VALUE(x)) ... WHERE `key` = VALUE(x)"
    // QUERIES
    // This query is executed firstly to update the value of x
    private Map<Class<? extends UpdateableStatistic>, PreparedStatement> updateQueryMap = new HashMap<Class<? extends UpdateableStatistic>, PreparedStatement>();

    // CONTAINS
    // "INSERT INTO <STATISTIC TABLE> (...) VALUES (...)"
    // QUERIES
    // This query is executed when the update query return zero row changes (the
    // entity doesn't exist)
    private Map<Class<? extends UpdateableStatistic>, PreparedStatement> insertQueryMap = new HashMap<Class<? extends UpdateableStatistic>, PreparedStatement>();

    private void createUpdatebleQueries(UpdateableStatistic uStatistic) {

        // CREATE THE UPDATE QUERY FOR THE UPDATE ABLE STATISTIC
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("UPDATE `");
        sBuilder.append(getTableName(uStatistic));
        sBuilder.append("` SET ");

        // APPEND THE SETS
        LinkedHashMap<String, StatisticType> head = uStatistic.getHead();
        for (String colName : head.keySet()) {
            sBuilder.append('`');
            sBuilder.append(colName);
            sBuilder.append("` = (`");
            sBuilder.append(colName);
            sBuilder.append("`+ ?), ");

        }

        // DELETE LAST COMMATA
        sBuilder.deleteCharAt(sBuilder.length() - 2);

        sBuilder.append("WHERE ");
        sBuilder.append(uStatistic.getKeyName());
        sBuilder.append(" = ? ");

        try {
            updateQueryMap.put(uStatistic.getClass(), dbConnection.getConnection().prepareStatement(sBuilder.toString()));
        } catch (Exception e) {
            ConsoleUtils.printException(e, pluginName, "Can't create update query for updateable statistic " + uStatistic + "!");
            return;
        }

        // RESET STRING BUILDER
        sBuilder.setLength(0);

        // CREATE INSERT INTO QUERY
        sBuilder.append("INSERT INTO `");
        sBuilder.append(getTableName(uStatistic));
        sBuilder.append("` (");

        // CREATE TABLE STRUCTURE
        Set<String> keys = head.keySet();
        for (String colName : head.keySet()) {
            sBuilder.append('`');
            sBuilder.append(colName);
            sBuilder.append("`,");
        }

        sBuilder.append('`');
        sBuilder.append(uStatistic.getKeyName());
        sBuilder.append("`)");

        // CREATE WILDCARDS TO INSERT THE VALUES
        // ONE MORE FOR THE KEY
        sBuilder.append(" VALUES (");
        for (int i = 0; i <= keys.size(); ++i)
            sBuilder.append("?,");

        sBuilder.setCharAt(sBuilder.length() - 1, ')');

        try {
            insertQueryMap.put(uStatistic.getClass(), dbConnection.getConnection().prepareStatement(sBuilder.toString()));
        } catch (Exception e) {
            System.out.println(sBuilder);
            return;
        }
    }

    // FORMAT: PluginName_StatisticName
    private String getTableName(Statistic statistic) {
        return statistic.getPluginName() + "_" + statistic.getName();
    }

    // ****************************************
    // ********* STORTING STATISTICS **********
    // ****************************************

    // **** NORMAL STATISTICS ****

    public void storeNormalStatistics(List<Statistic> stats) {
        // SHOULD NOT HAPPEN
        if (stats.isEmpty()) {
            ConsoleUtils.printError(IlluminatiCore.NAME, "List empty!");
            return;
        }

        // MISSING HEAD - SHOULD NOT HAPPEN!

        String head = insertHeads.get(stats.get(0).getClass());
        if (head == null) {
            ConsoleUtils.printError(IlluminatiCore.NAME, "No insert head for " + stats.get(0).getClass());
            return;
        }

        // ADDING THE VALUES TO THE INSERT INTO
        StringBuilder sBuilder = new StringBuilder(head);
        for (Statistic stat : stats) {
            sBuilder.append('(');
            sBuilder.append(getValueString(stat));
            sBuilder.append("),");
        }

        // REPLACE LAST ) with a commata
        sBuilder.setCharAt(sBuilder.length() - 1, ';');

        // SEND THE QUERY TO THE DATABASE
        storeNormalStatistic(sBuilder.toString());
    }

    private void storeNormalStatistic(String query) {
        try {
            dbConnection.getConnection().createStatement().executeUpdate(query);
        } catch (Exception e) {
            ConsoleUtils.printException(e, IlluminatiCore.NAME, "Can't save the statistics in the database!");
        }
    }

    private String getValueString(Statistic statistic) {
        Queue<Object> data = statistic.getData();
        StringBuilder sBuilder = new StringBuilder();
        for (Object o : data) {
            sBuilder.append(maskValue(o));
            sBuilder.append(',');
        }
        sBuilder.deleteCharAt(sBuilder.length() - 1);

        return sBuilder.toString();
    }

    // **** UPDATEABLE STATISTICS ****

    public void storeUpdateableStatistics(List<UpdateableStatistic> stats) {

        if (stats.isEmpty()) {
            ConsoleUtils.printError(pluginName, "List of updateable stats is empty!");
            return;
        }

        try {
            // DISABLE AUTO COMMIT TO FAST UP UPDATES
            dbConnection.getConnection().setAutoCommit(false);

            PreparedStatement updateStatement = null;
            PreparedStatement insertStatement = null;

            for (UpdateableStatistic uStatistic : stats) {
                updateStatement = updateQueryMap.get(uStatistic.getClass());
                if (updateStatement == null) {
                    ConsoleUtils.printError(pluginName, "Can't find an update query for the update statistic '" + uStatistic.getClass() + "'!");
                    continue;
                }

                fillStatement(updateStatement, uStatistic);

                int count = updateStatement.executeUpdate();
                // NO ENTRY FOUND WITH THIS KEY -> HAVE TO INSERT INSTEAD OF
                // UPDATING!
                if (count == 0) {
                    insertStatement = insertQueryMap.get(uStatistic.getClass());
                    if (insertStatement == null) {
                        ConsoleUtils.printError(pluginName, "Can't find a insert query for the update statistic '" + uStatistic.getClass() + "'!");
                        continue;
                    }

                    fillStatement(insertStatement, uStatistic);

                    insertStatement.execute();
                }
            }

            // COMMIT ALL CHANGES
            dbConnection.getConnection().commit();
        } catch (Exception e) {
            ConsoleUtils.printException(e, IlluminatiCore.NAME, "Can't save the updateable statistics in the database!");
        }
        // ALWAYS ACITIVE AUTO COMMIT!
        finally {
            try {
                dbConnection.getConnection().setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void fillStatement(PreparedStatement pStatement, UpdateableStatistic statistic) throws Exception {
        Queue<Object> data = statistic.getData();
        int pos = 1;
        for (Object o : data) {
            // IGNORE KEY IN UPDATE QUERY
            pStatement.setObject(pos++, o);
        }

        pStatement.setObject(pos, statistic.getKey());
    }
    // **** COMMON THINGS ****

    // REPLACER FOR ESCAPING SPECIAL CHARACTERS
    private static Pattern ESCAPE_APOSTROPHE = Pattern.compile("'");
    private static Pattern ESCAPE_BACKSLASH = Pattern.compile("\\\\");

    private String maskValue(Object o) {
        String value = o.toString();
        StringBuilder sBuilder = new StringBuilder(value.length() + 5);
        sBuilder.append('\'');

        // REPLACE A ' WITH \'
        value = ESCAPE_APOSTROPHE.matcher(value).replaceAll("\\\\'");
        // REPLACE A \ WITH \\
        value = ESCAPE_BACKSLASH.matcher(value).replaceAll("\\\\\\\\");
        sBuilder.append(value);
        sBuilder.append('\'');

        return sBuilder.toString();
    }
}
