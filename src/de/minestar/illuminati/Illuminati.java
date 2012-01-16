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
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.MyEventStatistic;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.bukkit.gemo.commands.CommandList;

import de.minestar.illuminati.commands.cmdIllu;
import de.minestar.illuminati.commands.cmdShowInfo;
import de.minestar.illuminati.database.DatabaseHandler;
import de.minestar.illuminati.listener.PListener;
import de.minestar.illuminati.listener.ServerCommandListener;
import de.minestar.illuminati.manager.PlayerManager;
import de.minestar.illuminati.manager.TickManager;
import de.minestar.illuminati.utils.ChatUtils;

public class Illuminati extends JavaPlugin {

    private DatabaseHandler dbHandler;
    private PlayerManager pManager;
    private CommandList commandList;
    private static TickManager tickManager;
    private long startTime = 0l;

    private static Illuminati instance;

    @Override
    public void onDisable() {
        dbHandler.closeConnection();
        dbHandler = null;
        pManager = null;
        String backslash = "\\";
        this.saveStatistics(backslash + backslash + "192.168.178.2" + backslash + "htdocs" + backslash + "illuminati" + backslash);
        this.saveStatistics("plugins/Illuminati/");
        ChatUtils.printConsoleInfo("Disabled!");
    }

    private void saveStatistics(String path) {
        try {
            long endTime = System.nanoTime();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd_HH-mm_ss");
            String dateiName = path + dateformat.format(new Date()) + ".csv";

            FileOutputStream schreibeStrom = new FileOutputStream(dateiName);
            String headline = "Eventname;EventCount;MinTime;MaxTime;Total Time;Average Time;";
            schreibeStrom.write(headline.getBytes());
            schreibeStrom.write(System.getProperty("line.separator").getBytes());

            MyEventStatistic stat = null;
            for (Map.Entry<String, MyEventStatistic> entry : SimplePluginManager.statisticMap.entrySet()) {
                try {
                    stat = entry.getValue();
                    String text = entry.getKey() + ";" + stat.eventCount + ";" + stat.minTime + ";" + stat.maxTime + ";" + stat.allTime + ";" + (stat.allTime / stat.eventCount) + ";";
                    schreibeStrom.write(text.getBytes());
                    schreibeStrom.write(System.getProperty("line.separator").getBytes());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            schreibeStrom.write(System.getProperty("line.separator").getBytes());
            schreibeStrom.write(System.getProperty("line.separator").getBytes());
            schreibeStrom.write(("Startzeit in NS:; " + this.startTime + ";").getBytes());
            schreibeStrom.write(System.getProperty("line.separator").getBytes());
            schreibeStrom.write(("Endzeit in NS:; " + endTime + ";").getBytes());
            schreibeStrom.write(System.getProperty("line.separator").getBytes());
            schreibeStrom.write(("Gesamtzeit in NS:; " + (endTime - startTime) + ";").getBytes());
            schreibeStrom.write(System.getProperty("line.separator").getBytes());
            long seconds = (((endTime - startTime) / 1000000) / 1000);
            long hours = seconds / 60 / 60;
            seconds -= hours * 60 * 60;
            long minutes = seconds / 60;
            seconds -= minutes * 60;
            schreibeStrom.write(("Uptime:; " + hours + ":" + minutes + ":" + seconds + ";").getBytes());
            schreibeStrom.write(System.getProperty("line.separator").getBytes());
            schreibeStrom.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        instance = this;

        File dataFolder = getDataFolder();
        dataFolder.mkdirs();

        // INIT COMMANDS
        this.initCommands();

        // REGISTER MANAGERS
        dbHandler = new DatabaseHandler(dataFolder);
        pManager = new PlayerManager(dbHandler, dataFolder);

        // REGISTER EVENTS
        PluginManager pm = getServer().getPluginManager();
        PlayerListener pListener = new PListener(pManager);
        pm.registerEvent(Type.PLAYER_JOIN, pListener, Priority.Highest, this);
        pm.registerEvent(Type.PLAYER_QUIT, pListener, Priority.Highest, this);
        pm.registerEvent(Type.PLAYER_KICK, pListener, Priority.Highest, this);
        pm.registerEvent(Type.PLAYER_COMMAND_PREPROCESS, pListener, Priority.Highest, this);
        pm.registerEvent(Type.SERVER_COMMAND, new ServerCommandListener(pManager), Priority.Normal, this);

        // RESET STATISTIC
        this.startTime = System.nanoTime();
        SimplePluginManager.statisticMap = new ConcurrentHashMap<String, MyEventStatistic>();

        // CREATE TICK-TASK
        tickManager = new TickManager();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, tickManager, 20, 1);

        // PRINT INFO
        ChatUtils.printConsoleInfo("Version " + getDescription().getVersion() + " enabled!");
    }

    public static TickManager getTickManager() {
        return tickManager;
    }

    private void initCommands() {
        /* @formatter:off */
        // Add an command to this list to register it in the plugin       
        com.bukkit.gemo.commands.Command[] commands = new com.bukkit.gemo.commands.Command[] {
                new cmdIllu("[Illuminati]", "/illu", "", "illuminati.use", false, new com.bukkit.gemo.commands.Command[] {
                        new cmdShowInfo("[Illuminati]", "show", "<EventName> [ns/ms/s]", "illuminati.use")
                })
        };
        /* @formatter:on */
        // store the commands in the hash map
        commandList = new CommandList("[FB-Chat]", commands);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        commandList.handleCommand(sender, label, args);
        return true;
    }

    public static Illuminati getInstance() {
        return instance;
    }
}
