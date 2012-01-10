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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.event.Event;

public class TimeManager {
    private HashMap<String, Long> maxTimes = new HashMap<String, Long>();
    private HashMap<String, Long> minTimes = new HashMap<String, Long>();
    private HashMap<String, Long> EventStartTime = new HashMap<String, Long>();
    private HashMap<String, Long> eventCount = new HashMap<String, Long>();
    private HashMap<String, Long> totalTimes = new HashMap<String, Long>();

    public void EventHasStarted(Event event) {
        this.EventHasStarted(event.getClass().getCanonicalName());
    }

    public void EventHasEnded(Event event) {
        this.EventHasEnded(event.getClass().getCanonicalName());
    }

    public void EventHasStarted(String name) {
        this.EventStartTime.put(name, System.nanoTime());
    }

    public ArrayList<String> getEventNames(String partial) {
        partial = partial.toLowerCase();
        ArrayList<String> eventList = new ArrayList<String>();
        for (Map.Entry<String, Long> entry : this.maxTimes.entrySet()) {
            if (entry.getKey().toLowerCase().contains(partial) || partial.equalsIgnoreCase("all")) {
                eventList.add(entry.getKey());
            }
        }
        return eventList;
    }

    public void EventHasEnded(String name) {
        long thisTime = System.nanoTime();
        long difference = thisTime - this.EventStartTime.get(name);
        this.updateMaxTime(name, difference);
        this.updateMinTime(name, difference);
        // UPDATE EVENTCOUNT
        long currentCount = 0;
        if (this.eventCount.get(name) != null) {
            currentCount = this.eventCount.get(name);
        }
        currentCount++;
        this.eventCount.put(name, currentCount);
        this.updateTotalTimes(name, difference);
    }

    private void updateTotalTimes(String name, long thisTime) {
        long oldTime = 0;
        if (this.totalTimes.containsKey(name)) {
            oldTime = this.totalTimes.get(name);
        }
        oldTime += thisTime;
        this.totalTimes.put(name, oldTime);
    }

    public long getAverageTime(String name) {
        if (!this.eventCount.containsKey(name)) {
            return 0l;
        }
        long totalTime = this.totalTimes.get(name);
        return (totalTime / this.getEventCount(name));
    }

    private void updateMaxTime(String name, long thisTime) {
        long oldTime = this.getMaxTime(name);
        if (oldTime <= thisTime) {
            this.setMaxTime(name, thisTime);
        }
    }

    private void updateMinTime(String name, long thisTime) {
        long oldTime = this.getMinTime(name);
        if (thisTime <= oldTime) {
            this.setMinTime(name, thisTime);
        }
    }

    public void updateMaxTime(Event event, long thisTime) {
        this.updateMaxTime(event.getClass().getCanonicalName(), thisTime);
    }

    public void updateMinTime(Event event, long thisTime) {
        this.updateMinTime(event.getClass().getCanonicalName(), thisTime);
    }

    public long getEventCount(String name) {
        if (!maxTimes.containsKey(name))
            return 0;
        return eventCount.get(name);
    }

    public long getMaxTime(String name) {
        if (!maxTimes.containsKey(name))
            setMaxTime(name, Long.MIN_VALUE);
        return maxTimes.get(name);
    }

    public long getMinTime(String name) {
        if (!minTimes.containsKey(name))
            setMinTime(name, Long.MAX_VALUE);
        return minTimes.get(name);
    }

    private void setMaxTime(String name, long thisTime) {
        maxTimes.put(name, thisTime);
    }

    private void setMinTime(String name, long thisTime) {
        minTimes.put(name, thisTime);
    }
}
