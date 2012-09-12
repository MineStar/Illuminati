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

import static de.minestar.illuminati.IlluminatiCore.NAME;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import de.minestar.illuminati.IlluminatiCore;
import de.minestar.minestarlibrary.database.DatabaseConnection;
import de.minestar.minestarlibrary.stats.StatisticType;
import de.minestar.minestarlibrary.stats.UpdateableStatistic;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

/**
 * Class to handle all incoming {@link UpdateableStatistic}
 * 
 * @author Meldanor
 * 
 */
public class UpdateableStatisticHandler {

    private DatabaseConnection dbConnection;

    public UpdateableStatisticHandler(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

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

    public void registerStatistic(UpdateableStatistic uStatistic, String tableName) {

        // CREATE THE UPDATE QUERY FOR THE UPDATE ABLE STATISTIC
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("UPDATE `");
        sBuilder.append(tableName);
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
            ConsoleUtils.printException(e, NAME, "Can't create update query for updateable statistic " + uStatistic + "!");
            return;
        }

        // RESET STRING BUILDER
        sBuilder.setLength(0);

        // CREATE INSERT INTO QUERY
        sBuilder.append("INSERT INTO `");
        sBuilder.append(tableName);
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

    public void storeStatistics(List<UpdateableStatistic> stats) {

        if (stats.isEmpty()) {
            ConsoleUtils.printError(NAME, "List of updateable stats is empty!");
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
                    ConsoleUtils.printError(NAME, "Can't find an update query for the update statistic '" + uStatistic.getClass() + "'!");
                    continue;
                }

                fillStatement(updateStatement, uStatistic);

                int count = updateStatement.executeUpdate();
                // NO ENTRY FOUND WITH THIS KEY -> HAVE TO INSERT INSTEAD OF
                // UPDATING!
                if (count == 0) {
                    insertStatement = insertQueryMap.get(uStatistic.getClass());
                    if (insertStatement == null) {
                        ConsoleUtils.printError(NAME, "Can't find a insert query for the update statistic '" + uStatistic.getClass() + "'!");
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
        // ALWAYS ACTIVATE AUTO COMMIT!
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

}
