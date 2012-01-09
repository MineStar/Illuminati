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

public class TimeManager {
    private HashMap<String, Long> maxTimes = new HashMap<String, Long>();
    private HashMap<String, Long> minTimes = new HashMap<String, Long>();

    public void updateMaxTime(String name, long thisTime) {
        long oldTime = this.getMaxTime(name);
        if (oldTime < thisTime) {
            this.setMaxTime(name, thisTime);
        }
    }

    public void updateMinTime(String name, long thisTime) {
        long oldTime = this.getMinTime(name);
        if (thisTime < oldTime) {
            this.setMinTime(name, thisTime);
        }
    }

    private long getMaxTime(String name) {
        if (!maxTimes.containsKey(name))
            setMaxTime(name, System.currentTimeMillis());
        return maxTimes.get(name);
    }

    private long getMinTime(String name) {
        if (!minTimes.containsKey(name))
            setMinTime(name, System.currentTimeMillis());
        return minTimes.get(name);
    }

    private void setMaxTime(String name, long thisTime) {
        maxTimes.put(name, thisTime);
    }

    private void setMinTime(String name, long thisTime) {
        minTimes.put(name, thisTime);
    }
}
