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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import de.minestar.illuminati.database.DatabaseHandler;
import de.minestar.minestarlibrary.stats.Statistic;

public class StatisticManager {

    private DatabaseHandler dbHandler;

    private Queue<Statistic> queue = new LinkedBlockingQueue<Statistic>();

    private static final int BUFFER_SIZE = 64;

    public StatisticManager(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public void registerStatistic(Statistic statistic) {
        dbHandler.registerStatistic(statistic);
    }

    public void handleStatistic(Statistic statistic) {
        queue.add(statistic);
        // DO WE HAVE TO RUN THE QUEUE?
        if (queue.size() >= BUFFER_SIZE)
            runQueue();

    }

    public void flushQueue() {
        if (queue.size() == 0)
            return;
        else
            runQueue();
    }

    private void runQueue() {
        // MAP FOR ALL STATS SORTED BY THEIR CLASSES
        Map<Class<? extends Statistic>, List<Statistic>> map = new HashMap<Class<? extends Statistic>, List<Statistic>>();
        // TEMP VARIABLES
        List<Statistic> list = null;
        Statistic stat = null;

        // FLUSHING THE QUEUE
        while (!queue.isEmpty()) {
            // GET FIRST STAT
            stat = queue.poll();
            // GET LIST FOR THIS CLASS
            list = map.get(stat.getClass());
            // FIRST ELEMENT OF THIS CLASS - CREATE A NEW LIST
            if (list == null) {
                list = new LinkedList<Statistic>();
                map.put(stat.getClass(), list);
            }
            // ADD STAT TO LIST
            list.add(stat);
        }

        // SAVE ALL STATS IN DATABASE
        for (List<Statistic> stats : map.values())
            dbHandler.storeStatistics(stats);

    }

}
