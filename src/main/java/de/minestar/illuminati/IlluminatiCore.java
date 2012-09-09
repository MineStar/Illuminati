/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of 'Illuminati'.
 * 
 * 'Illuminati' is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * 'Illuminati' is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with 'Illuminati'.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package de.minestar.illuminati;

import java.io.File;

import org.bukkit.scheduler.BukkitScheduler;

import de.minestar.illuminati.database.DatabaseHandler;
import de.minestar.illuminati.manager.StatisticManager;
import de.minestar.minestarlibrary.AbstractCore;
import de.minestar.minestarlibrary.stats.Statistic;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class IlluminatiCore extends AbstractCore {

    public static final String NAME = "Illuminati";

    private static DatabaseHandler dbHandler;
    private static StatisticManager statManager;

    public IlluminatiCore() {
        super(NAME);
    }

    @Override
    protected boolean loadingConfigs(File dataFolder) {
        return Settings.init(dataFolder);
    }

    @Override
    protected boolean createManager() {
        dbHandler = new DatabaseHandler(NAME, new File(getDataFolder(), "sqlconfig.yml"));
        statManager = new StatisticManager(dbHandler);
        return true;
    }

    @Override
    protected boolean startThreads(BukkitScheduler scheduler) {
        scheduler.scheduleAsyncRepeatingTask(this, statManager, 20L * 60L, Settings.QUEUE_INTERVALL * 20L);
        return true;
    }

    @Override
    protected boolean commonDisable() {
        ConsoleUtils.printInfo(NAME, "Flush the queue");
        statManager.flushQueue();

        dbHandler.closeConnection();
        dbHandler = null;
        return true;
    }

    public static void registerStatistic(Class<? extends Statistic> statistic) {
        try {
            if (dbHandler != null && dbHandler.hasConnection())
                statManager.registerStatistic(statistic.newInstance());
            else
                ConsoleUtils.printError(NAME, "Can't register statistic for plugin " + statistic.getName() + "! Reason: No database connection");
        } catch (IllegalAccessException e) {
            ConsoleUtils.printException(e, NAME, "Can't create an instance of " + statistic + "!");
        } catch (InstantiationException e) {
            ConsoleUtils.printException(e, NAME, "Can't create an instance of " + statistic + "!");
        }
    }

    public static void handleStatistic(Statistic statistic) {
        statManager.handleStatistic(statistic);
    }
}
