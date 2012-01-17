/*
 * Copyright (C) 2011 GeMo
 * 
 * This file is part of FalseBook and FalseBookChat.
 * 
 * FalseBookChat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * FalseBookChat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with FalseBookChat.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.illuminati.commands;

import java.text.DecimalFormat;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.MyEventStatistic;
import org.bukkit.plugin.SimplePluginManager;

import com.bukkit.gemo.commands.ExtendedCommand;
import com.bukkit.gemo.utils.ChatUtils;

public class cmdShowInfo extends ExtendedCommand {

    public cmdShowInfo(String pluginName, String syntax, String arguments, String node) {
        super(pluginName, syntax, arguments, node);
        this.description = "Show info";
    }

    @Override
    public void execute(String[] args, CommandSender sender) {
        // GET EVENTNAME
        double factor = 1;
        String unit = "ns";
        DecimalFormat formater = new DecimalFormat("#.###");
        if (args.length >= 2) {
            if (args[1].equalsIgnoreCase("ms")) {
                factor = 1000000;
                unit = "ms";
            }
        }

        ChatUtils.printInfo(sender, pluginName, ChatColor.YELLOW, "List of Events:");
        ChatUtils.printInfo(sender, pluginName, ChatColor.GREEN, "EventName : Eventcount - MinTime / MaxTime / Average");
        MyEventStatistic stat = null;
        for (Map.Entry<String, MyEventStatistic> entry : SimplePluginManager.statisticMap.entrySet()) {
            if (entry.getKey().toLowerCase().contains(args[0].toLowerCase())) {
                stat = entry.getValue();
                ChatUtils.printInfo(sender, pluginName, ChatColor.GRAY, entry.getKey() + " : " + stat.eventCount + " - " + formater.format(((double) stat.minTime / factor)) + unit + " / " + formater.format(((double) stat.maxTime / factor)) + unit + " / " + formater.format(((double) (stat.allTime / stat.eventCount) / factor)) + unit);
            }
        }
    }
}
