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

import static de.minestar.illuminati.IlluminatiCore.dbHandler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import de.minestar.illuminati.IlluminatiCore;
import de.minestar.illuminati.Settings;
import de.minestar.minestarlibrary.stats.Statistic;
import de.minestar.minestarlibrary.stats.UpdateableStatistic;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class StatisticManager implements Runnable {

    private Queue<Statistic> normalQueue = new LinkedBlockingQueue<Statistic>();

    private Queue<UpdateableStatistic> updateQueue = new LinkedBlockingQueue<UpdateableStatistic>();

    private static final int BUFFER_SIZE = Settings.BUFFER_SIZE;

    public void registerStatistic(Statistic statistic) {
        dbHandler.registerStatistic(statistic);
    }

    public synchronized void handleStatistic(Statistic statistic) {
        if (statistic instanceof UpdateableStatistic)
            updateQueue.add((UpdateableStatistic) statistic);
        else
            normalQueue.add(statistic);
    }

    @Override
    public void run() {
        // DO WE HAVE TO RUN THE QUEUE?
        if (normalQueue.size() >= BUFFER_SIZE)
            runNormalQueue();

        if (updateQueue.size() >= BUFFER_SIZE)
            runUpdateQueue();
    }

    public void flushQueue() {
        if (normalQueue.size() != 0) {
            ConsoleUtils.printInfo(IlluminatiCore.NAME, "Flush normal statistic queue. Remaining queued statistics: " + normalQueue.size());
            runNormalQueue();

        }
        if (updateQueue.size() != 0) {
            ConsoleUtils.printInfo(IlluminatiCore.NAME, "Flush updateable statistic queue. Remaining queued statistics: " + updateQueue.size());
            runUpdateQueue();
        }

    }

    private void runNormalQueue() {
        // MAP FOR ALL STATS SORTED BY THEIR CLASSES
        Map<Class<? extends Statistic>, List<Statistic>> map = new HashMap<Class<? extends Statistic>, List<Statistic>>();
        // TEMP VARIABLES
        List<Statistic> list = null;

        for (Statistic statistic : normalQueue) {
            // GET LIST FOR THIS CLASS
            list = map.get(statistic.getClass());
            // FIRST ELEMENT OF THIS CLASS - CREATE A NEW LIST
            if (list == null) {
                list = new LinkedList<Statistic>();
                map.put(statistic.getClass(), list);
            }
            // ADD STAT TO LIST
            list.add(statistic);
        }

        normalQueue.clear();

        // SAVE ALL STATS IN DATABASE
        for (List<Statistic> stats : map.values())
            dbHandler.storeNormalStatistics(stats);

    }

    private void runUpdateQueue() {
        // MAP FOR ALL STATS SORTED BY THEIR CLASSES
        Map<Class<? extends UpdateableStatistic>, List<UpdateableStatistic>> map = new HashMap<Class<? extends UpdateableStatistic>, List<UpdateableStatistic>>();
        // TEMP VARIABLES
        List<UpdateableStatistic> list = null;

        for (UpdateableStatistic statistic : updateQueue) {
            // GET LIST FOR THIS CLASS
            list = map.get(statistic.getClass());
            // FIRST ELEMENT OF THIS CLASS - CREATE A NEW LIST
            if (list == null) {
                list = new LinkedList<UpdateableStatistic>();
                map.put(statistic.getClass(), list);
            }
            // ADD STAT TO LIST
            list.add(statistic);
        }

        updateQueue.clear();

        // SAVE ALL STATS IN DATABASE
        for (List<UpdateableStatistic> stats : map.values())
            dbHandler.storeUpdateableStatistics(stats);
    }
}
