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

import org.bukkit.event.Event;

public class TimeManager {
    private HashMap<String, Long> maxTimes = new HashMap<String, Long>();
    private HashMap<String, Long> minTimes = new HashMap<String, Long>();
    private HashMap<String, Long> EventStartTime = new HashMap<String, Long>();
    private HashMap<String, Long> eventCount = new HashMap<String, Long>();

    public void EventHasStarted(Event event) {
        this.EventHasStarted(event.getClass().getCanonicalName());
    }

    public void EventHasEnded(Event event) {
        this.EventHasEnded(event.getClass().getCanonicalName());
    }

    public void EventHasStarted(String name) {
        this.EventStartTime.put(name, System.nanoTime());
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

    private long getMaxTime(String name) {
        if (!maxTimes.containsKey(name))
            setMaxTime(name, System.nanoTime());
        return maxTimes.get(name);
    }

    private long getMinTime(String name) {
        if (!minTimes.containsKey(name))
            setMinTime(name, System.nanoTime());
        return minTimes.get(name);
    }

    private void setMaxTime(String name, long thisTime) {
        maxTimes.put(name, thisTime);
    }

    private void setMinTime(String name, long thisTime) {
        minTimes.put(name, thisTime);
    }
}
