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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import de.minestar.illuminati.IlluminatiCore;
import de.minestar.minestarlibrary.database.DatabaseConnection;
import de.minestar.minestarlibrary.stats.Statistic;
import de.minestar.minestarlibrary.stats.StatisticType;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

/**
 * Class to handle all incoming {@link Statistic}
 * 
 * @author Meldanor
 * 
 */
public class NormalStatisticHandler {

    private DatabaseConnection dbConnection;

    public NormalStatisticHandler(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // MAP FOR NORMAL QUERIES
    private Map<Class<? extends Statistic>, String> insertHeads = new HashMap<Class<? extends Statistic>, String>();

    // CREATE THE HEAD OF THE INSERT INTO QUERY
    public void registerStatistic(Statistic statistic, String tableName) {
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

    public void storeStatistics(List<Statistic> stats) {
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
        try {
            dbConnection.getConnection().createStatement().executeUpdate(sBuilder.toString());
        } catch (Exception e) {
            ConsoleUtils.printException(e, IlluminatiCore.NAME, "Can't save the statistics in the database!");
        }
    }

    private String getValueString(Statistic statistic) {
        Queue<Object> data = statistic.getData();
        StringBuilder sBuilder = new StringBuilder();
        for (Object o : data) {
            if (o instanceof Boolean) {
                o = ((Boolean) o) ? 1 : 0;
            }
            sBuilder.append(maskValue(o));
            sBuilder.append(',');
        }
        sBuilder.deleteCharAt(sBuilder.length() - 1);

        return sBuilder.toString();
    }

    // REPLACER FOR ESCAPING SPECIAL CHARACTERS
    private final static Pattern ESCAPE_APOSTROPHE = Pattern.compile("'");
    private final static Pattern ESCAPE_BACKSLASH = Pattern.compile("\\\\");

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
