/*
 * Copyright (C) 2011 GeMo
 * 
 * This file is part of FalseBook and FalseBookBlock.
 * 
 * FalseBookBlock is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * FalseBookBlock is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with FalseBookBlock.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.illuminati.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.commands.SuperCommand;
import com.bukkit.gemo.utils.ChatUtils;

public class cmdIllu extends SuperCommand {

    public cmdIllu(String pluginName, String syntax, String arguments, String node, boolean hasFunction, Command[] subCommands) {
        super(pluginName, syntax, arguments, node, hasFunction, subCommands);
    }

    @Override
    public void execute(String[] args, Player player) {
        ChatUtils.printLine(player, ChatColor.AQUA, "-------------- [ Illuminati Help ] --------------");
        Command[] commands = getSubCommands();
        for (Command command : commands)
            ChatUtils.printLine(player, ChatColor.GRAY, command.getHelpMessage());
    }

    @Override
    public void run(String[] args, Player player) {
        if (!runSubCommand(args, player))
            super.run(args, player);
    }
}
