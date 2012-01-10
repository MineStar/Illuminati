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
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.bukkit.gemo.commands.ExtendedCommand;
import com.bukkit.gemo.utils.ChatUtils;

import de.minestar.illuminati.Illuminati;

public class cmdShowInfo extends ExtendedCommand {

    public cmdShowInfo(String pluginName, String syntax, String arguments, String node) {
        super(pluginName, syntax, arguments, node);
        this.description = "Show info";
    }

    @Override
    public void execute(String[] args, Player player) {
        // GET EVENTNAME
        double factor = 1;
        String unit = "ns";
        DecimalFormat formater = new DecimalFormat("#.###");
        if(args.length >= 2) {
            if(args[1].equalsIgnoreCase("ms")) {
                factor = 1000000;
                unit = "ms";
            }
        }
        
        ChatUtils.printInfo(player, pluginName, ChatColor.YELLOW, "List of Events:");
        ChatUtils.printInfo(player, pluginName, ChatColor.GREEN, "EventName : Eventcount - MinTime / MaxTime / Average");
        ArrayList<String> eventList = Illuminati.getTimeManager().getEventNames(args[0]);

        for (String event : eventList) {
            ChatUtils.printInfo(player, pluginName, ChatColor.GRAY, event.replace("org.bukkit.event.", "") + " : " + Illuminati.getTimeManager().getEventCount(event) + " - "+ formater.format(((double)Illuminati.getTimeManager().getMinTime(event)/factor)) + unit + " / " + formater.format(((double)Illuminati.getTimeManager().getMaxTime(event)/factor)) + unit + " / " + formater.format(((double)Illuminati.getTimeManager().getAverageTime(event)/factor)) + unit);
        }
    }
}
